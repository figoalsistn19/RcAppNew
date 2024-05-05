package com.inventoryapp.rcapp.ui.agentnav.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.inventoryapp.rcapp.data.model.AgentStockTransaction
import com.inventoryapp.rcapp.data.model.OfferingForAgent
import com.inventoryapp.rcapp.data.repository.AgentRepository
import com.inventoryapp.rcapp.util.Resource
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
class AgentTransactionViewModel @Inject constructor(
    private val repository: AgentRepository
) : ViewModel() {

    //for add product in
    private val _addProductInFlow = MutableStateFlow<Resource<FirebaseFirestore>?>(null)
    val addProductInFlow: StateFlow<Resource<FirebaseFirestore>?> = _addProductInFlow

    fun addProductIn(transaction: AgentStockTransaction, idProduct: String, offering: OfferingForAgent) = viewModelScope.launch {

        val result = repository.addAgentStockTransaction(transaction, idProduct, offering) {
        }
        _addProductInFlow.value = result
    }

    //get agent transaction
    private val _agentTransactionInSearch = MutableStateFlow<Resource<List<AgentStockTransaction>>>(Resource.Loading)

    private val _agentTransactionsIn = MutableLiveData<Resource<List<AgentStockTransaction>>>()
    val agentTransactionsIn: LiveData<Resource<List<AgentStockTransaction>>> get() = _agentTransactionsIn

    fun fetchStockIn() {
        viewModelScope.launch {
            _agentTransactionsIn.value = Resource.Loading
            val result = repository.getAgentTransaction()
            _agentTransactionsIn.value = result
        }
    }

    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    //second state the text typed by the user
    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private val _agentTransactionInList = MutableStateFlow(emptyList<AgentStockTransaction>())
    private val _agentTransactionOutList = MutableStateFlow(emptyList<AgentStockTransaction>())

    val agentTransactionList = searchText
        .combine(_agentTransactionInList) { text, agentProduct ->//combine searchText with _contriesList
            if (text.isBlank()) { //return the entery list of countries if not is typed
                agentProduct
            }
            agentProduct.filter { agentProduct ->// filter and return a list of countries based on the text the user typed
                agentProduct.productName!!.uppercase().contains(text.trim().uppercase())
            }
        }.stateIn(//basically convert the Flow returned from combine operator to StateFlow
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),//it will allow the StateFlow survive 5 seconds before it been canceled
            initialValue = _agentTransactionInList.value
        )

    val agentTransactionOutList = searchText
        .combine(_agentTransactionOutList) { text, agentProduct ->//combine searchText with _contriesList
            if (text.isBlank()) { //return the entery list of countries if not is typed
                agentProduct
            }
            agentProduct.filter { agentProduct ->// filter and return a list of countries based on the text the user typed
                agentProduct.productName!!.uppercase().contains(text.trim().uppercase())
            }
        }.stateIn(//basically convert the Flow returned from combine operator to StateFlow
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),//it will allow the StateFlow survive 5 seconds before it been canceled
            initialValue = _agentTransactionOutList.value
        )
    init {
        viewModelScope.launch {
            _agentTransactionInSearch.value = repository.getAgentTransaction()
            // Update filtered list based on initial data
            _agentTransactionInList.value = mapToAgentProductList(_agentTransactionInSearch.value)?.filter { it.transactionType == "IN" }
                ?: emptyList()
            _agentTransactionOutList.value = mapToAgentProductList(_agentTransactionInSearch.value)?.filter { it.transactionType == "OUT" }
                ?: emptyList()
        }
    }

    private fun mapToAgentProductList(resource: Resource<List<AgentStockTransaction>>): List<AgentStockTransaction>? {
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

    /// AGENT TRANSACTION IN DONE
}