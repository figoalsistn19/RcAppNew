package com.inventoryapp.rcapp.ui.auth.internalauth

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.inventoryapp.rcapp.data.model.AgentUser
import com.inventoryapp.rcapp.data.model.InternalUser
import com.inventoryapp.rcapp.data.model.UserRole
import com.inventoryapp.rcapp.data.repository.InternalRepository
import com.inventoryapp.rcapp.ui.nav.ROUTE_INTERNAL_SALES_SCREEN
import com.inventoryapp.rcapp.ui.nav.ROUTE_INTERNAL_STOCK_SCREEN
import com.inventoryapp.rcapp.ui.nav.ROUTE_LOGIN_AGENT
import com.inventoryapp.rcapp.ui.nav.ROUTE_MAIN_AGENT_SCREEN
import com.inventoryapp.rcapp.ui.nav.ROUTE_MAIN_INTERNAL_SCREEN
import com.inventoryapp.rcapp.ui.nav.ROUTE_OFFERING_PO_FOR_AGENT_SCREEN
import com.inventoryapp.rcapp.ui.nav.ROUTE_REGISTER_INTERNAL
import com.inventoryapp.rcapp.util.Resource
import com.inventoryapp.rcapp.util.SharedPrefConstants
import com.inventoryapp.rcapp.util.await
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthInternalViewModel @Inject constructor(
    private val repository: InternalRepository,
    private val firestore: FirebaseFirestore
) : ViewModel()  {

    private val _loginFlow = MutableStateFlow<Resource<FirebaseUser>?>(null)
    val loginFlow: StateFlow<Resource<FirebaseUser>?> = _loginFlow

    private val _registerFlow = MutableStateFlow<Resource<FirebaseUser>?>(null)
    val registerFlow: StateFlow<Resource<FirebaseUser>?> = _registerFlow

    val currentUser: FirebaseUser?
        get() = repository.currentUser

    init {
        if (repository.currentUser != null){
            _loginFlow.value = Resource.Success(repository.currentUser!!)
        }
    }

    fun loginUser(email: String, password: String) = viewModelScope.launch {
        _loginFlow.value = Resource.Loading
        val result = repository.login(email, password)
        _loginFlow.value = result
    }

    fun registerUser(name: String, email: String, password: String, user: InternalUser) = viewModelScope.launch {
        _registerFlow.value = Resource.Loading
        val result = repository.register(name,email,password, user = user)
        _registerFlow.value = result
    }

    fun checkUserLoggedIn() {
        val currentUser = currentUser
        if (currentUser != null) {
            // Pengguna sudah login
            navigateToCorrectScreen(currentUser.uid)
        } else {
            // Pengguna belum login, lakukan navigasi ke layar login
            // Misalnya: navController.navigate(ROUTE_LOGIN_SCREEN)
        }
    }

    private fun navigateToCorrectScreen(userId: String)= viewModelScope.launch {
        try {
            // Periksa apakah pengguna adalah internal user atau agent user
            val internalUserDoc = firestore.collection("InternalUser").document(userId).get().await()
            val agentUserDoc = firestore.collection("AgentUser").document(userId).get().await()

            if (internalUserDoc.exists()) {
                // Pengguna adalah internal user
                val internalUser = internalUserDoc.toObject(InternalUser::class.java)
                navigateToInternalUserScreen(internalUser)
            } else if (agentUserDoc.exists()) {
                // Pengguna adalah agent user
                val agentUser = agentUserDoc.toObject(AgentUser::class.java)
                navigateToAgentUserScreen(agentUser)
            }
        } catch (e: Exception) {
            // Penanganan jika terjadi kesalahan saat mengambil data dari Firestore
            e.printStackTrace()
        }
    }

    private fun navigateToInternalUserScreen(internalUser: InternalUser?) {
        // Lakukan navigasi ke layar internal user dengan data internalUser
        val role = internalUser?.userRole
        when(role){
            UserRole.Admin -> _navigateToScreen.value = ROUTE_MAIN_INTERNAL_SCREEN
            UserRole.FinanceManager -> _navigateToScreen.value = ROUTE_MAIN_INTERNAL_SCREEN
            UserRole.HeadOfWarehouse -> _navigateToScreen.value = ROUTE_INTERNAL_STOCK_SCREEN
            UserRole.OperationTeam -> _navigateToScreen.value = ROUTE_INTERNAL_SALES_SCREEN
            UserRole.Owner -> _navigateToScreen.value = ROUTE_MAIN_INTERNAL_SCREEN
            UserRole.Sales -> _navigateToScreen.value = ROUTE_OFFERING_PO_FOR_AGENT_SCREEN
            UserRole.SalesManager -> _navigateToScreen.value = ROUTE_INTERNAL_SALES_SCREEN
            else -> _navigateToScreen.value = ROUTE_REGISTER_INTERNAL
        }
//        _navigateToScreen.value = ROUTE_MAIN_INTERNAL_SCREEN
    }

    private fun navigateToAgentUserScreen(agentUser: AgentUser?) {
        // Lakukan navigasi ke layar agent user dengan data agentUser
        _navigateToScreen.value = ROUTE_LOGIN_AGENT
        // Misalnya: navController.navigate(ROUTE_AGENT_USER_SCREEN, agentUser)
    }
    private val _navigateToScreen = MutableLiveData<String?>()
    val navigateToScreen: LiveData<String?> = _navigateToScreen

    fun navigateToScreen(screen: String) {
        _navigateToScreen.value = screen
    }

    fun getSession(result: (InternalUser?) -> Unit){
        repository.getSession(result)
    }

//    private val _enteringFlow = MutableStateFlow<Resource<String>?>(null)
//    val enteringFlow: StateFlow<Resource<String>?> = _enteringFlow
//    fun getSessionUser() = viewModelScope.launch {
//        _enteringFlow.value = Resource.Loading
//        val result = repository.getSessionUser()
//        _enteringFlow.value = result
//    }

    fun logout(){
        repository.logout()
        _registerFlow.value = null
        _loginFlow.value = null
    }

}