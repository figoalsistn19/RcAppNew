package com.inventoryapp.rcapp.ui.auth

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source
import com.inventoryapp.rcapp.data.model.AgentUser
import com.inventoryapp.rcapp.data.model.InternalUser
import com.inventoryapp.rcapp.data.model.VerifAccountStatus.*
import com.inventoryapp.rcapp.data.repository.InternalRepository
import com.inventoryapp.rcapp.ui.nav.ROUTE_HOME
import com.inventoryapp.rcapp.ui.nav.ROUTE_MAIN_AGENT_SCREEN
import com.inventoryapp.rcapp.ui.nav.ROUTE_MAIN_INTERNAL_SCREEN
import com.inventoryapp.rcapp.util.FireStoreCollection
import com.inventoryapp.rcapp.util.Resource
import com.inventoryapp.rcapp.util.SharedPrefConstants
import com.inventoryapp.rcapp.util.await
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: InternalRepository,
    private val firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth,
    private val appPreferences: SharedPreferences
) : ViewModel()  {

    val userRole = appPreferences.getString(SharedPrefConstants.USER_STATUS, null)
    private val _loginFlow = MutableStateFlow<Resource<FirebaseUser>?>(null)
    val loginFlow: StateFlow<Resource<FirebaseUser>?> = _loginFlow

    private val _registerFlow = MutableStateFlow<Resource<FirebaseUser>?>(null)
    val registerFlow: StateFlow<Resource<FirebaseUser>?> = _registerFlow

    val currentUser: FirebaseUser?
        get() = repository.currentUser

    val email: String?
        get() = repository.currentUser?.email
    fun resetPassword() = viewModelScope.launch {
        firebaseAuth.sendPasswordResetEmail(email!!)
    }


//    init {
//        if (repository.currentUser != null){
//            _loginFlow.value = Resource.Success(repository.currentUser!!)
//        }
//    }

    fun loginUser(email: String, password: String) = viewModelScope.launch {
        _loginFlow.value = Resource.Loading
        val result = repository.login(email, password)
//        _navigateToScreen.value = result.toString()
        _loginFlow.value = result
    }

    fun registerUser(name: String, email: String, password: String, user: InternalUser) = viewModelScope.launch {
        _registerFlow.value = Resource.Loading
        val result = repository.register(name,email,password, user = user)
        _registerFlow.value = result
    }

    fun checkUserLoggedIn() {
        if (currentUser != null) {
            navigateToCorrectScreen(currentUser!!.uid)
        }
    }

    private fun navigateToCorrectScreenForLogin(userId: String)= viewModelScope.launch {
        try {
            val source = Source.DEFAULT
            // Periksa apakah pengguna adalah internal user atau agent user
            val internalUserDoc = firestore.collection(FireStoreCollection.INTERNALUSER).document(userId).get(source).await()
            val agentUserDoc = firestore.collection(FireStoreCollection.AGENTUSER).document(userId).get(source).await()

            if (agentUserDoc.exists()) {
                // Pengguna adalah internal user
                val agentUser = agentUserDoc.toObject(AgentUser::class.java)
                appPreferences.edit().putString(SharedPrefConstants.USER_STATUS, agentUser?.verificationStatus.toString()).apply()
                navigateToAgentUserScreen(agentUser!!)
            } else if (internalUserDoc.exists()) {
                // Pengguna adalah agent user
                val internalUser = internalUserDoc.toObject(InternalUser::class.java)
                appPreferences.edit().putString(SharedPrefConstants.USER_ROLE_INTERNAL, internalUser?.userRole.toString()).apply()
                navigateToInternalUserScreen(internalUser!!)
            }
        } catch (e: Exception) {
            // Penanganan jika terjadi kesalahan saat mengambil data dari Firestore
            e.printStackTrace()
        }
    }

     private fun navigateToCorrectScreen(userId: String)= viewModelScope.launch {
        try {
            val source = Source.SERVER
            // Periksa apakah pengguna adalah internal user atau agent user
            val internalUserDoc = firestore.collection(FireStoreCollection.INTERNALUSER).document(userId).get(source).await()
            val agentUserDoc = firestore.collection(FireStoreCollection.AGENTUSER).document(userId).get(source).await()

            if (agentUserDoc.exists()) {
                // Pengguna adalah internal user
                val agentUser = agentUserDoc.toObject(AgentUser::class.java)
                appPreferences.edit().putString(SharedPrefConstants.USER_STATUS, agentUser?.verificationStatus.toString()).apply()
                navigateToAgentUserScreen(agentUser!!)
            } else if (internalUserDoc.exists()) {
                // Pengguna adalah agent user
                val internalUser = internalUserDoc.toObject(InternalUser::class.java)
                appPreferences.edit().putString(SharedPrefConstants.USER_ROLE_INTERNAL, internalUser?.userRole.toString()).apply()
                navigateToInternalUserScreen(internalUser!!)
            }
        } catch (e: Exception) {
            // Penanganan jika terjadi kesalahan saat mengambil data dari Firestore
            e.printStackTrace()
        }
    }

    private val _navigateToScreen = MutableLiveData<String?>()
    val navigateToScreen: LiveData<String?> = _navigateToScreen

    fun navigateToScreen(screen: String) {
        _navigateToScreen.value = screen
    }
    private fun navigateToInternalUserScreen(internalUser: InternalUser?) {
        // Lakukan navigasi ke layar internal user dengan data internalUser
        _navigateToScreen.value = ROUTE_MAIN_INTERNAL_SCREEN
    }

    private fun navigateToAgentUserScreen(agentUser: AgentUser?) {
        // Lakukan navigasi ke layar agent user dengan data agentUser
        val role = agentUser?.verificationStatus
        when(role){
            APPROVED -> _navigateToScreen.value = ROUTE_MAIN_AGENT_SCREEN
            PENDING -> _navigateToScreen.value = ROUTE_HOME
            else -> _navigateToScreen.value = "login"
        }
//        _navigateToScreen.value = ROUTE_LOGIN_AGENT
        // Misalnya: navController.navigate(ROUTE_AGENT_USER_SCREEN, agentUser)
    }

    fun getSession(result: (InternalUser?) -> Unit){
        repository.getSession(result)
    }

    fun logout(){
        repository.logout()
        _registerFlow.value = null
        _loginFlow.value = null
    }

}