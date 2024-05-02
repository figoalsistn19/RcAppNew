package com.inventoryapp.rcapp.ui.internalnav.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.inventoryapp.rcapp.data.model.AgentProduct
import com.inventoryapp.rcapp.data.model.AgentUser
import com.inventoryapp.rcapp.data.model.VerifAccountStatus
import com.inventoryapp.rcapp.data.repository.AgentRepository
import com.inventoryapp.rcapp.data.repository.InternalRepository
import com.inventoryapp.rcapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@Suppress("UNUSED_EXPRESSION")
@HiltViewModel
class AgentUserViewModel @Inject constructor(
    private val repository: InternalRepository,
    private val agentRepo: AgentRepository
): ViewModel() {

    private val _agentUserSearch = MutableStateFlow<Resource<List<AgentUser>>>(Resource.Loading)

    private val _agentUsers = MutableLiveData<Resource<List<AgentUser>>>()
    val agentUsers: LiveData<Resource<List<AgentUser>>> get() = _agentUsers

    fun fetchUsers() =
        viewModelScope.launch {
            _agentUsers.value = Resource.Loading
            val result = repository.getAgentUsers()
            _agentUsers.value = result
        }

    private val _isSearchingAgent = MutableStateFlow(false)
    val isSearchingAgent = _isSearchingAgent.asStateFlow()

    //second state the text typed by the user
    private val _searchTextAgent = MutableStateFlow("")
    val searchTextAgent = _searchTextAgent.asStateFlow()

    //filter for list order by sales
    private val _agentUsersList = MutableStateFlow(emptyList<AgentUser>())
    val agentUsersList = searchTextAgent
        .combine(_agentUsersList) { text, orders ->//combine searchText with _contriesList
            if (text.isBlank()) { //return the entery list of countries if not is typed
                orders
            }
            orders.filter { orderss->// filter and return a list of countries based on the text the user typed
                orderss.name!!.uppercase().contains(text.trim().uppercase())
            }
        }.stateIn(//basically convert the Flow returned from combine operator to StateFlow
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),//it will allow the StateFlow survive 5 seconds before it been canceled
            initialValue = _agentUsersList.value
        )

    init {
        viewModelScope.launch {
            _agentUserSearch.value = repository.getAgentUsers()
            // Update filtered list based on initial data
            _agentUsersList.value = mapToAgentList(_agentUserSearch.value) ?: emptyList()
        }
    }

    private fun mapToAgentList(resource: Resource<List<AgentUser>>): List<AgentUser>? {
        return when (resource) {
            is Resource.Success -> resource.result
            else -> null
        }
    }
    fun onSearchTextChange(text: String) {
        _searchTextAgent.value = text
    }

    fun onToogleSearch() {
        _isSearchingAgent.value = !_isSearchingAgent.value
        if (!_isSearchingAgent.value) {
            onSearchTextChange("")
        }
    }

    // UPDATE STATUS AGENT
    private val _updateStatusAgentFlow = MutableStateFlow<Resource<FirebaseFirestore>?>(null)
    val updateStatusAgentFlow: StateFlow<Resource<FirebaseFirestore>?> = _updateStatusAgentFlow

    fun updateStatusAgent(idUser: String, status: VerifAccountStatus)= viewModelScope.launch {
        val result = repository.updateStatusAgent(idUser, status){
        }
        _updateStatusAgentFlow.value = result
    }

    //GET AGENT PRODUCT HERE
    private val _agentProductSearch = MutableStateFlow<Resource<List<AgentProduct>>>(Resource.Loading)

    private val _agentProducts = MutableLiveData<Resource<List<AgentProduct>>>()
    val agentProducts: LiveData<Resource<List<AgentProduct>>> get() = _agentProducts

    fun fetchAgentProducts(idUser: String) {
        viewModelScope.launch {
            _agentProducts.value = Resource.Loading
            val result = repository.getAgentProduct(idUser)
            _agentProductSearch.value = repository.getAgentProduct(idUser)
            // Update filtered list based on initial data
            _agentProductList.value = mapToAgentProductList(_agentProductSearch.value) ?: emptyList()
            _agentProducts.value = result
        }
    }

//    fun getSession(result: (AgentUser?) -> Unit){
//        repository.getSession(result)
//    }
    private val _isSearchingAgentProduct = MutableStateFlow(false)
    val isSearchingAgentProduct = _isSearchingAgentProduct.asStateFlow()

    //second state the text typed by the user
    private val _searchTextAgentProduct = MutableStateFlow("")
    val searchTextAgentProduct = _searchTextAgentProduct.asStateFlow()

    private val _agentProductList = MutableStateFlow(emptyList<AgentProduct>())
    val agentProductList = searchTextAgentProduct
        .combine(_agentProductList) { text, agentProduct ->//combine searchText with _contriesList
            if (text.isBlank()) { //return the entery list of countries if not is typed
                agentProduct
            }
            agentProduct.filter { agentProduct ->// filter and return a list of countries based on the text the user typed
                agentProduct.productName!!.uppercase().contains(text.trim().uppercase())
            }
        }.stateIn(//basically convert the Flow returned from combine operator to StateFlow
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),//it will allow the StateFlow survive 5 seconds before it been canceled
            initialValue = _agentProductList.value
        )

//    init {
//        viewModelScope.launch {
//            _agentProductSearch.value = agentRepo.getAgentProduct(idUser!!)
//            // Update filtered list based on initial data
//            _agentProductList.value = mapToAgentProductList(_agentProductSearch.value) ?: emptyList()
//        }
//    }

    private fun mapToAgentProductList(resource: Resource<List<AgentProduct>>): List<AgentProduct>? {
        return when (resource) {
            is Resource.Success -> resource.result
            else -> null
        }
    }

    fun onSearchTextChangeProductAgent(text: String) {
        _searchTextAgentProduct.value = text
    }

    fun onToogleSearchProductAgent() {
        _isSearchingAgentProduct.value = !_isSearchingAgentProduct.value
        if (!_isSearchingAgentProduct.value) {
            onSearchTextChange("")
        }
    }
}