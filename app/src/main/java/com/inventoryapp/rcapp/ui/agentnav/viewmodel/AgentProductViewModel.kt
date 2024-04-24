package com.inventoryapp.rcapp.ui.agentnav.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.inventoryapp.rcapp.data.model.AgentProduct
import com.inventoryapp.rcapp.data.model.AgentUser
import com.inventoryapp.rcapp.data.repository.AgentRepository
import com.inventoryapp.rcapp.util.Resource
import com.inventoryapp.rcapp.util.SharedPrefConstants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AgentProductViewModel @Inject constructor(
    private val repository: AgentRepository,
    private val appPreferences: SharedPreferences
) : ViewModel() {

//    private val _idAgentProduct = MutableStateFlow(appPreferences.getString(SharedPrefConstants.USER_ID,null))
//    val idAgentProduct = _idAgentProduct.asStateFlow()
    val idAgent = appPreferences.getString(SharedPrefConstants.USER_ID,null)
    val agentName = appPreferences.getString(SharedPrefConstants.USER_NAME, null)

    // Add agent product
    private val _agentProductFlow = MutableStateFlow<Resource<FirebaseFirestore>?>(null)
    val agentProductFlow: StateFlow<Resource<FirebaseFirestore>?> = _agentProductFlow

    fun addAgentProduct(agentProduct: AgentProduct) = viewModelScope.launch {
        val result = repository.addAgentProduct(product = agentProduct){
        }
        _agentProductFlow.value = result
    }

    private val _agentProductSearch = MutableStateFlow<Resource<List<AgentProduct>>>(Resource.Loading)

    private val _agentProducts = MutableLiveData<Resource<List<AgentProduct>>>()
    val agentProducts: LiveData<Resource<List<AgentProduct>>> get() = _agentProducts

    fun fetchAgentProducts() {
        viewModelScope.launch {
            _agentProducts.value = Resource.Loading
            val result = repository.getAgentProduct()
            _agentProducts.value = result
        }
    }

    fun getSession(result: (AgentUser?) -> Unit){
        repository.getSession(result)
    }

    //first state whether the search is happening or not
    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    //second state the text typed by the user
    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private val _agentProductList = MutableStateFlow(emptyList<AgentProduct>())
    val agentProductList = searchText
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

    private val _agentProductListForReqOrder = MutableStateFlow(emptyList<AgentProduct>())
    val agentProductListForReqOrder = searchText
        .combine(_agentProductListForReqOrder) { text, agentProduct ->//combine searchText with _contriesList
            if (text.isBlank()) { //return the entery list of countries if not is typed
                agentProduct
            }
            agentProduct.filter { agentProduct ->// filter and return a list of countries based on the text the user typed
                agentProduct.productName!!.uppercase().contains(text.trim().uppercase())
            }
        }.stateIn(//basically convert the Flow returned from combine operator to StateFlow
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),//it will allow the StateFlow survive 5 seconds before it been canceled
            initialValue = _agentProductListForReqOrder.value
        )

    init {
        viewModelScope.launch {
            _agentProductSearch.value = repository.getAgentProduct()
            // Update filtered list based on initial data
            _agentProductList.value = mapToAgentProductList(_agentProductSearch.value) ?: emptyList()
        }
    }

    private fun mapToAgentProductList(resource: Resource<List<AgentProduct>>): List<AgentProduct>? {
        return when (resource) {
            is Resource.Success -> resource.result
            else -> null
        }
    }

    fun onSearchTextChange(text: String) {
        _searchText.value = text
    }

    fun onToogleSearch() {
        _isSearching.value = !_isSearching.value
        if (!_isSearching.value) {
            onSearchTextChange("")
        }
    }
}
