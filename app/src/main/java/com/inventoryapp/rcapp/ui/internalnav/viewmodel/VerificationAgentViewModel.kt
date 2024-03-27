package com.inventoryapp.rcapp.ui.internalnav.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inventoryapp.rcapp.data.model.AgentUser
import com.inventoryapp.rcapp.data.model.VerifAccountStatus
import com.inventoryapp.rcapp.ui.agentnav.viewmodel.reqOrders
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import java.util.Date

class VerificationAgentViewModel :ViewModel(){
    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    //second state the text typed by the user
    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    //filter for list order by sales
    private val _agentUsersList = MutableStateFlow(agentUserList)
    val agentUsersList = searchText
        .combine(_agentUsersList) { text, orders ->//combine searchText with _contriesList
            if (text.isBlank()) { //return the entery list of countries if not is typed
                orders
            }
            orders.filter { orders ->// filter and return a list of countries based on the text the user typed
                orders.name.uppercase().contains(text.trim().uppercase())
            }
        }.stateIn(//basically convert the Flow returned from combine operator to StateFlow
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),//it will allow the StateFlow survive 5 seconds before it been canceled
            initialValue = _agentUsersList.value
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

val agentUserList = listOf(
    AgentUser("miwf","Figo als", "figo@mail.com", "21313132","wede2d2d2", VerifAccountStatus.PENDING, Date()),
    AgentUser("ewfwef","Figo als", "figo@mail.com", "21313132","wede2d2d2", VerifAccountStatus.PENDING, Date()),
    AgentUser("fewfewef","Figo als", "figo@mail.com", "21313132","wede2d2d2", VerifAccountStatus.PENDING, Date()),
    AgentUser("fewfwf","Figo als", "figo@mail.com", "21313132","wede2d2d2", VerifAccountStatus.PENDING, Date()),
    AgentUser("wdcewc","Figo als", "figo@mail.com", "21313132","wede2d2d2", VerifAccountStatus.PENDING, Date()),
    AgentUser("emfoeff","Figo als", "figo@mail.com", "21313132","wede2d2d2", VerifAccountStatus.PENDING, Date()),
    AgentUser("fweomfweo","Figo als", "figo@mail.com", "21313132","wede2d2d2", VerifAccountStatus.PENDING, Date()),
    AgentUser("wefowfo","Figo als", "figo@mail.com", "21313132","wede2d2d2", VerifAccountStatus.PENDING, Date()),
    AgentUser("fewkfmwe","Figo als", "figo@mail.com", "21313132","wede2d2d2", VerifAccountStatus.PENDING, Date()),
    AgentUser("ewfnwefwe","Figo ni", "figo@mail.com", "21313132","wede2d2d2", VerifAccountStatus.APPROVED, Date())

)