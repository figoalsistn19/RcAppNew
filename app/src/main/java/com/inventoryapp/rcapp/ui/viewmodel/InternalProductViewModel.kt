package com.inventoryapp.rcapp.ui.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.inventoryapp.rcapp.data.model.InternalProduct
import com.inventoryapp.rcapp.data.model.ProductsItem
import com.inventoryapp.rcapp.data.repository.InternalRepository
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
class InternalProductViewModel @Inject constructor(
    private val repository: InternalRepository,
    appPreferences: SharedPreferences
): ViewModel() {

    val userRole = appPreferences.getString(SharedPrefConstants.USER_ROLE_INTERNAL,null)

    private val _internalProductSearch = MutableStateFlow<Resource<List<InternalProduct>>>(Resource.Loading)

    private val _internalProducts = MutableLiveData<Resource<List<InternalProduct>>>()
    val internalProducts: LiveData<Resource<List<InternalProduct>>> get() = _internalProducts

    private val _cartData = MutableLiveData<Resource<List<ProductsItem>>>()
    val cartData: LiveData<Resource<List<ProductsItem>>> get() = _cartData
    fun fetchInternalProducts() {
        viewModelScope.launch {
            _internalProducts.value = Resource.Loading
            val result = repository.getInternalProducts()
            _internalProducts.value = result
            _internalProductSearch.value = repository.getInternalProducts()
            _internalProductList.value = mapToInternalProductList(_internalProductSearch.value) ?: emptyList()
        }
    }

    fun fetchCardData() {
        viewModelScope.launch {
            _cartData.value = Resource.Loading
            val result = repository.getCardData()
            _cartData.value = result
        }
    }

    private val _internalProductFlow = MutableStateFlow<Resource<FirebaseFirestore>?>(null)
    val internalProductFlow: StateFlow<Resource<FirebaseFirestore>?> = _internalProductFlow

    fun addInternalProduct(internalProduct: InternalProduct) = viewModelScope.launch {
        val result = repository.addInternalProduct(product = internalProduct){
        }
        _internalProductList.value = mapToInternalProductList(_internalProductSearch.value) ?: emptyList()
        _internalProductFlow.value = result
    }

    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    fun setIsSearching(value: Boolean) {
        _isSearching.value = false
    }

    //second state the text typed by the user
    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private val _internalProductList = MutableStateFlow(emptyList<InternalProduct>())
    val internalProductList = searchText
        .combine(_internalProductList) { text, orders ->//combine searchText with _contriesList
            if (text.isBlank()) { //return the entery list of countries if not is typed
                orders
            }
            orders.filter { orders ->// filter and return a list of countries based on the text the user typed
                orders.productName!!.uppercase().contains(text.trim().uppercase())
            }
        }.stateIn(//basically convert the Flow returned from combine operator to StateFlow
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),//it will allow the StateFlow survive 5 seconds before it been canceled
            initialValue = _internalProductList.value
        )
//    init {
//        viewModelScope.launch {
//            _internalProductSearch.value = repository.getInternalProducts()
//            // Update filtered list based on initial data
//            _internalProductList.value = mapToInternalProductList(_internalProductSearch.value) ?: emptyList()
//        }
//    }

    private fun mapToInternalProductList(resource: Resource<List<InternalProduct>>): List<InternalProduct>? {
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

    // UPDATE INTERNAL PRODUCT
    private val _internalProductEditFlow = MutableStateFlow<Resource<FirebaseFirestore>?>(null)
    val internalProductEditFlow: StateFlow<Resource<FirebaseFirestore>?> = _internalProductEditFlow

    fun editInternalProduct(internalProduct: InternalProduct) = viewModelScope.launch {
        val result = repository.updateInternalProduct(internalProduct){
        }
//        _internalProductList.value = mapToInternalProductList(_internalProductSearch.value) ?: emptyList()
        _internalProductEditFlow.value = result
    }

    // DELETE INTERNAL PRODUCT
    // DELETE SALES ORDER
    private val _deleteInternalProductFlow = MutableStateFlow<Resource<Boolean>?>(null)
    val deleteInternalProductFlow: StateFlow<Resource<Boolean>?> = _deleteInternalProductFlow

    fun deleteInternalProduct(idProduct: String) = viewModelScope.launch {
        _deleteInternalProductFlow.value = Resource.Loading
        val result = repository.deleteInternalProduct(idProduct)
        _deleteInternalProductFlow.value = result
    }
}