package com.inventoryapp.rcapp.ui.auth.agentauth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.inventoryapp.rcapp.data.model.AgentUser
import com.inventoryapp.rcapp.data.repository.AgentRepository
import com.inventoryapp.rcapp.ui.nav.ROUTE_LOGIN_AGENT
import com.inventoryapp.rcapp.util.Resource
import com.inventoryapp.rcapp.util.await
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthAgentViewModel @Inject constructor(
    private val repository: AgentRepository,
    private val firestore: FirebaseFirestore
) : ViewModel()  {
//    private val firestore = FirebaseFirestore.getInstance()

    private val _loginFlow = MutableStateFlow<Resource<FirebaseUser>?>(null)
    val loginFlow: StateFlow<Resource<FirebaseUser>?> = _loginFlow

    private val _registerFlow = MutableStateFlow<Resource<FirebaseUser>?>(null)
    val registerFlow: StateFlow<Resource<FirebaseUser>?> = _registerFlow

    val currentUser: FirebaseUser?
        get() = repository.currentUser

    fun checkUserLoggedIn(navController: NavController) {
        val currentUsers = currentUser
        if (currentUsers != null) {
            // Pengguna sudah login
            navigateBasedOnRole(currentUsers.uid)
        } else {
            navController.navigate(ROUTE_LOGIN_AGENT)
            // Pengguna belum login, lakukan navigasi ke layar login
            // Misalnya: navController.navigate(ROUTE_LOGIN_SCREEN)
        }
    }

    private fun navigateBasedOnRole(userId: String) {
        viewModelScope.launch {
            try {
                val userDoc = firestore.collection("users").document(userId).get().await()
                val role = userDoc.getString("role")
                if (role == "admin" || role == "user") {
                    // Jika role adalah admin atau user, navigasi ke MainScreen
                    // Misalnya: navController.navigate(ROUTE_MAIN_SCREEN)
                } else {
                    // Role tidak valid, lakukan navigasi ke layar error atau lainnya
                    // Misalnya: navController.navigate(ROUTE_ERROR_SCREEN)
                }
            } catch (e: Exception) {
                // Penanganan jika terjadi kesalahan saat mengambil data dari Firestore
                e.printStackTrace()
            }
        }
    }
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

    fun registerUser(name: String, email: String, password: String, user: AgentUser) = viewModelScope.launch {
        _registerFlow.value = Resource.Loading
        val result = repository.register(name,email,password, user = user)
        _registerFlow.value = result
    }

    fun getSession(result: (AgentUser?) -> Unit){
        repository.getSession(result)
    }

    fun logout(){
        repository.logout()
        _registerFlow.value = null
        _loginFlow.value = null
    }

}