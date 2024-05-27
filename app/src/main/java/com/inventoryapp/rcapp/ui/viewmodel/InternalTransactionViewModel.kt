package com.inventoryapp.rcapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.inventoryapp.rcapp.data.model.InternalStockTransaction
import com.inventoryapp.rcapp.data.repository.InternalRepository
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
class InternalTransactionViewModel @Inject constructor(
    private val repository: InternalRepository
): ViewModel()
{
    //for add product in
    private val _addProductInFlow = MutableStateFlow<Resource<FirebaseFirestore>?>(null)
    val addInternalTransactionFlow: StateFlow<Resource<FirebaseFirestore>?> = _addProductInFlow

    fun addInternalTransaction(transaction: InternalStockTransaction, idProduct: String) = viewModelScope.launch {

        val result = repository.addInternalStockTransaction(transaction, idProduct) {
        }
        _addProductInFlow.value = result
    }

    //get agent transaction
    private val _agentTransactionInSearch = MutableStateFlow<Resource<List<InternalStockTransaction>>>(Resource.Loading)

    private val _agentTransactionsIn = MutableLiveData<Resource<List<InternalStockTransaction>>>()
    val agentTransactions: LiveData<Resource<List<InternalStockTransaction>>> get() = _agentTransactionsIn

    fun fetchStockIn() {
        viewModelScope.launch {
            _agentTransactionsIn.value = Resource.Loading
            val result = repository.getInternalStockTransaction()
            _agentTransactionInSearch.value = result
            _agentTransactionsIn.value = result
            _transactionInList.value = mapToAgentProductList(_agentTransactionInSearch.value)?.filter { it.transactionType == "IN" }
                ?: emptyList()
            _transactionOutList.value = mapToAgentProductList(_agentTransactionInSearch.value)?.filter { it.transactionType == "OUT" }
                ?: emptyList()
        }
    }
    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    //second state the text typed by the user
    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private val _transactionInList = MutableStateFlow(emptyList<InternalStockTransaction>())
    private val _transactionOutList = MutableStateFlow(emptyList<InternalStockTransaction>())

    val transactionInList = searchText
        .combine(_transactionInList) { text, agentProduct ->//combine searchText with _contriesList
            if (text.isBlank()) { //return the entery list of countries if not is typed
                agentProduct
            }
            agentProduct.filter { // filter and return a list of countries based on the text the user typed
                it.productName!!.uppercase().contains(text.trim().uppercase())
            }
        }.stateIn(//basically convert the Flow returned from combine operator to StateFlow
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),//it will allow the StateFlow survive 5 seconds before it been canceled
            initialValue = _transactionInList.value
        )

    val transactionOutList = searchText
        .combine(_transactionOutList) { text, agentProduct ->//combine searchText with _contriesList
            if (text.isBlank()) { //return the entery list of countries if not is typed
                agentProduct
            }
            agentProduct.filter { agentProduct ->// filter and return a list of countries based on the text the user typed
                agentProduct.productName!!.uppercase().contains(text.trim().uppercase())
            }
        }.stateIn(//basically convert the Flow returned from combine operator to StateFlow
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),//it will allow the StateFlow survive 5 seconds before it been canceled
            initialValue = _transactionOutList.value
        )
//    init {
//        viewModelScope.launch {
//            _agentTransactionInSearch.value = repository.getInternalStockTransaction()
//            // Update filtered list based on initial data
//            _transactionInList.value = mapToAgentProductList(_agentTransactionInSearch.value)?.filter { it.transactionType == "IN" }
//                ?: emptyList()
//            _transactionOutList.value = mapToAgentProductList(_agentTransactionInSearch.value)?.filter { it.transactionType == "OUT" }
//                ?: emptyList()
//        }
//    }

    private fun mapToAgentProductList(resource: Resource<List<InternalStockTransaction>>): List<InternalStockTransaction>? {
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