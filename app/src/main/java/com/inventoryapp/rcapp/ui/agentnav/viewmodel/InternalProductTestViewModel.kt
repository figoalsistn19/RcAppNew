package com.inventoryapp.rcapp.ui.agentnav.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inventoryapp.rcapp.data.model.InternalProduct
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import java.util.Date

class InternalProductTestViewModel: ViewModel(){

    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    //second state the text typed by the user
    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    //third state the list to be filtered
    private val _productsList = MutableStateFlow(internalProducts)
    val productsList = searchText
        .combine(_productsList) { text, products ->//combine searchText with _contriesList
            if (text.isBlank()) { //return the entery list of countries if not is typed
                products
            }
            products.filter { products ->// filter and return a list of countries based on the text the user typed
                products.productName!!.uppercase().contains(text.trim().uppercase())
            }
        }.stateIn(//basically convert the Flow returned from combine operator to StateFlow
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),//it will allow the StateFlow survive 5 seconds before it been canceled
            initialValue = _productsList.value
        )

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

val internalProducts = listOf(
    InternalProduct("1", "Item 1",100,50,34,1000,90, updateAt = Date(), desc = "ini coba"),
    InternalProduct("2", "figo 1",100,50,34,1000,90, updateAt = Date(), desc = "ini coba"),
    InternalProduct("3", "qwdqwd 1",100,50,34,1000,90, updateAt = Date(), desc = "ini coba"),
    InternalProduct("4", "dwqd 1",100,50,34,1000,90, updateAt = Date(), desc = "ini coba"),
    InternalProduct("5", "Itewqdem 1",100,50,34,1000,90, updateAt = Date(), desc = "ini coba"),
    InternalProduct("6", "fewff 1",100,50,34,1000,90, updateAt = Date(), desc = "ini coba"),
    InternalProduct("7", "hterh 1",100,50,34,1000,90, updateAt = Date(), desc = "ini coba"),
    InternalProduct("8", "ethe 1",100,50,34,1000,90, updateAt = Date(), desc = "ini coba"),
    InternalProduct("9", "etht4j 1",100,50,34,1000,90, updateAt = Date(), desc = "ini coba"),
    InternalProduct("10", "zxzx 1",100,50,34,1000,90, updateAt = Date(), desc = "ini coba"),
    InternalProduct("11", "xsxsac 1",100,50,34,1000,90, updateAt = Date(), desc = "ini coba"),
    InternalProduct("12", "vdbdb 1",100,50,34,1000,90, updateAt = Date(), desc = "ini coba"),
    InternalProduct("13", "eowmowe 1",100,50,34,1000,90, updateAt = Date(), desc = "ini coba"),
    InternalProduct("14", "moefwm 1",100,50,34,1000,90, updateAt = Date(), desc = "ini coba"),
    InternalProduct("15", "wfmowe 1",100,50,34,1000,90, updateAt = Date(), desc = "ini coba"),
    InternalProduct("16", "ooewt 1",100,50,34,1000,90, updateAt = Date(), desc = "ini coba"),
    InternalProduct("17", "otewoit 1",100,50,34,1000,90, updateAt = Date(), desc = "ini coba"),
    InternalProduct("18", "wetowt 1",100,50,34,1000,90, updateAt = Date(), desc = "ini coba"),
    InternalProduct("19", "wrtwjk 1",100,50,34,1000,90, updateAt = Date(), desc = "ini coba"),
    InternalProduct("20", "wrtw 1",100,50,34,1000,90, updateAt = Date(), desc = "ini coba"),
    InternalProduct("21", "jgrgrg 1",100,50,34,1000,90, updateAt = Date(), desc = "ini coba")
)
