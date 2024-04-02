package com.inventoryapp.rcapp.ui.agentnav.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inventoryapp.rcapp.data.model.ProductsItem
import com.inventoryapp.rcapp.data.model.SalesOrder
import com.inventoryapp.rcapp.data.model.StatusOrder
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import java.util.Date

class AgentOrderViewModel: ViewModel() {
    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    //second state the text typed by the user
    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    //third state the list to be filtered
    private val _historyOrderList = MutableStateFlow(reqOrders)
    val historyOrderList = searchText
        .combine(_historyOrderList) { text, orders ->//combine searchText with _contriesList
            if (text.isBlank()) { //return the entery list of countries if not is typed
                orders
            }
            orders.filter { orders ->// filter and return a list of countries based on the text the user typed
                orders.statusOrder.toString().uppercase().contains(text.trim().uppercase())
            }
        }.stateIn(//basically convert the Flow returned from combine operator to StateFlow
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),//it will allow the StateFlow survive 5 seconds before it been canceled
            initialValue = _historyOrderList.value
        )

    //filter for list order by sales
    private val _reqOrderBySales = MutableStateFlow(reqOrders)
    val reqOrderbySalesList = searchText
        .combine(_reqOrderBySales) { text, orders ->//combine searchText with _contriesList
            if (text.isBlank()) { //return the entery list of countries if not is typed
                orders
            }
            orders.filter { orders ->// filter and return a list of countries based on the text the user typed
                orders.idAgent.uppercase().contains(text.trim().uppercase())
            }
        }.stateIn(//basically convert the Flow returned from combine operator to StateFlow
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),//it will allow the StateFlow survive 5 seconds before it been canceled
            initialValue = _reqOrderBySales.value
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

val products = listOf(
    ProductsItem(internalProducts[1].idProduct!!,
        internalProducts[1].productName!!, internalProducts[1].price,10, 100000),
    )

val reqOrders = listOf( // Replace with your actual data
    SalesOrder("IDX-344-432","SDWKE431DE", "Figo", "figo@gmail.com", products, Date(), StatusOrder.DalamPerjalanan, 100000,10),
    SalesOrder("IDX-344-432","SDWKE431DE", "Dani", "figo@gmail.com", products, Date(), StatusOrder.DalamPerjalanan, 100000,10),
    SalesOrder("IDX-344-432","SDWKE431DE", "Indo", "figo@gmail.com", products, Date(), StatusOrder.DalamPerjalanan, 100000,10),
    SalesOrder("IDX-344-432","SDWKE431DE", "Oid", "figo@gmail.com", products, Date(), StatusOrder.DalamPerjalanan, 100000,10),
    SalesOrder("IDX-344-432","SDWKE431DE", "Onad", "figo@gmail.com", products, Date(), StatusOrder.DalamPerjalanan, 100000,10)
)