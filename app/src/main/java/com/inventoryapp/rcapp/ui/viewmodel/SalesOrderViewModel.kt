package com.inventoryapp.rcapp.ui.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.inventoryapp.rcapp.data.model.SalesOrder
import com.inventoryapp.rcapp.data.model.StatusOrder
import com.inventoryapp.rcapp.data.model.UserRole
import com.inventoryapp.rcapp.data.repository.AgentRepository
import com.inventoryapp.rcapp.data.repository.InternalRepository
import com.inventoryapp.rcapp.util.FireStoreCollection
import com.inventoryapp.rcapp.util.Resource
import com.inventoryapp.rcapp.util.await
import dagger.hilt.android.lifecycle.HiltViewModel
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
class SalesOrderViewModel @Inject constructor(
    private val repository: AgentRepository,
    private val firestore: FirebaseFirestore,
    private val internalRepository: InternalRepository
) : ViewModel() {

    val currentUser: FirebaseUser?
        get() = repository.currentUser

    private val _userRole = MutableLiveData<String?>()
    val userRole: LiveData<String?> = _userRole

//    private val userRoleRef = firestore.collection(FireStoreCollection.INTERNALUSER).document(currentUser?.uid!!)
//        .get()
//        .addOnSuccessListener {
//            _userRole.value = it.getString("userRole")
//        }

    fun getUserRole() = viewModelScope.launch {
        if (currentUser != null) {
            val userRoleRef = firestore.collection(FireStoreCollection.INTERNALUSER).document(currentUser?.uid!!)
                .get().await()
            _userRole.value = userRoleRef.getString("userRole")
        }
    }


    //ADD SALES ORDER FROM REQ ORDER
    private val _addSalesOrderFlow = MutableStateFlow<Resource<FirebaseFirestore>?>(null)
    val addSalesOrderFlow: StateFlow<Resource<FirebaseFirestore>?> = _addSalesOrderFlow

    fun addSalesOrder(order: SalesOrder) = viewModelScope.launch {
        val result = repository.addSalesOrder(order){
        }
        _addSalesOrderFlow.value = result
    }

    private val _salesOrderSearch = MutableStateFlow<Resource<List<SalesOrder>>>(
        Resource.Loading)

    private val _salesOrder = MutableLiveData<Resource<List<SalesOrder>>>()
    val salesOrder: LiveData<Resource<List<SalesOrder>>> get() = _salesOrder

    fun fetchSalesOrder() {
        viewModelScope.launch {
            _salesOrder.value = Resource.Loading
            val result = repository.getSalesOrder(currentUser?.uid!!)
            _salesOrder.value = result
            _salesOrderSearch.value = result
            // Update filtered list based on initial data
            _salesOrderList.value = mapToSalesOrder(_salesOrderSearch.value)
                ?: emptyList()
        }
    }

    //second state the text typed by the user
    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()


    private val _salesOrderList = MutableStateFlow(emptyList<SalesOrder>())

    val salesOrderList = searchText
        .combine(_salesOrderList) { text, agentProduct ->//combine searchText with _contriesList
            if (text.isBlank()) { //return the entery list of countries if not is typed
                agentProduct
            }
            agentProduct.filter { salesOrder ->// filter and return a list of countries based on the text the user typed
                salesOrder.productsItem!![0].productName!!.uppercase().contains(text.trim().uppercase())
            }
        }.stateIn(//basically convert the Flow returned from combine operator to StateFlow
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),//it will allow the StateFlow survive 5 seconds before it been canceled
            initialValue = _salesOrderList.value
        )
//    init {
//        viewModelScope.launch {
//            _salesOrderSearch.value = repository.getSalesOrder(currentUser?.uid!!)
//            // Update filtered list based on initial data
//            _salesOrderList.value = mapToSalesOrder(_salesOrderSearch.value)
//                ?: emptyList()
//        }
//    }
    private fun mapToSalesOrder(resource: Resource<List<SalesOrder>>): List<SalesOrder>? {
        return when (resource) {
            is Resource.Success -> resource.result
            else -> null
        }
    }

    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()
    fun onSearchTextChange(text: String) {
        _searchText.value = text
    }

    fun onToogleSearch() {
        _isSearching.value = !_isSearching.value
        if (!_isSearching.value) {
            onSearchTextChange("")
        }
    }

    //GET SALES ORDER FOR INTERNAL
    private val _salesOrderInternalSearch = MutableStateFlow<Resource<List<SalesOrder>>>(
        Resource.Loading)

    private val _salesOrderInternal = MutableLiveData<Resource<List<SalesOrder>>>()
    val salesOrderInternal: LiveData<Resource<List<SalesOrder>>> get() = _salesOrderInternal

    fun fetchSalesOrderInternal() {
        viewModelScope.launch {
            _salesOrderInternal.value = Resource.Loading
            val result = internalRepository.getSalesOrder()
            _salesOrderInternal.value = result
            _salesOrderInternalSearch.value = result
            // Update filtered list based on initial data
            _salesOrderInternalList.value = mapToSalesOrderInternal(_salesOrderInternalSearch.value)
                ?: emptyList()
        }
    }

    private val _salesOrderInternalList = MutableStateFlow(emptyList<SalesOrder>())

    val salesOrderInternalList = searchText
        .combine(_salesOrderInternalList) { text, agentProduct ->//combine searchText with _contriesList
            if (text.isBlank()) { //return the entery list of countries if not is typed
                agentProduct
            }
            agentProduct.filter { salesOrder ->// filter and return a list of countries based on the text the user typed
                salesOrder.productsItem!![0].productName!!.uppercase().contains(text.trim().uppercase())
            }
        }.stateIn(//basically convert the Flow returned from combine operator to StateFlow
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),//it will allow the StateFlow survive 5 seconds before it been canceled
            initialValue = _salesOrderInternalList.value
        )


    val salesOrderInternalListSize = searchText
        .combine(_salesOrderInternalList) { text, agentProduct ->//combine searchText with _contriesList
            if (text.isBlank()) { //return the entery list of countries if not is typed
                agentProduct
            }
            agentProduct.filter { salesOrder ->// filter and return a list of countries based on the text the user typed
                salesOrder.statusOrder == StatusOrder.Pending
            }
        }.stateIn(//basically convert the Flow returned from combine operator to StateFlow
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),//it will allow the StateFlow survive 5 seconds before it been canceled
            initialValue = _salesOrderInternalList.value
        )
    init {
        viewModelScope.launch {
            _salesOrderInternalSearch.value = internalRepository.getSalesOrder()
            // Update filtered list based on initial data
            _salesOrderInternalList.value = mapToSalesOrderInternal(_salesOrderInternalSearch.value)
                ?: emptyList()
        }
    }
    private fun mapToSalesOrderInternal(resource: Resource<List<SalesOrder>>): List<SalesOrder>? {
        return when (resource) {
            is Resource.Success -> resource.result
            else -> null
        }
    }

    // UPDATE STATUS ORDER
    private val _updateStatusOrderFlow = MutableStateFlow<Resource<FirebaseFirestore>?>(null)
    val updateStatusOrderFlow: StateFlow<Resource<FirebaseFirestore>?> = _updateStatusOrderFlow

    fun updateStatusOrder(idOrder: String, status: StatusOrder)= viewModelScope.launch {
        _updateStatusOrderFlow.value = Resource.Loading
        val result = internalRepository.updateStatusOrder(idOrder, status.toString()){
        }
        _updateStatusOrderFlow.value = result
    }

    // DELETE SALES ORDER
    private val _deleteSalesOrderFlow = MutableStateFlow<Resource<Boolean>?>(null)
    val deleteSalesOrderFlow: StateFlow<Resource<Boolean>?> = _deleteSalesOrderFlow

    fun deleteSalesOrder(idOrder: String) = viewModelScope.launch {
        _deleteSalesOrderFlow.value = Resource.Loading
        val result = internalRepository.deleteSalesOrder(idOrder)
        _deleteSalesOrderFlow.value = result
    }
}