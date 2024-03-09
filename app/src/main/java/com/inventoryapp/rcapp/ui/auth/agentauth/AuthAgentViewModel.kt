package com.inventoryapp.rcapp.ui.auth.agentauth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.inventoryapp.rcapp.data.model.AgentUser
import com.inventoryapp.rcapp.data.repository.AuthAgentRepository
import com.inventoryapp.rcapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthAgentViewModel @Inject constructor(
    private val repository: AuthAgentRepository
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