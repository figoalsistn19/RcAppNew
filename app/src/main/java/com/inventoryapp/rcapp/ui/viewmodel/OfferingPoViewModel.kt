package com.inventoryapp.rcapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.inventoryapp.rcapp.data.model.InternalProduct
import com.inventoryapp.rcapp.data.model.OfferingForAgent
import com.inventoryapp.rcapp.data.repository.AgentRepository
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

@Suppress("UNUSED_EXPRESSION")
@HiltViewModel
class OfferingPoViewModel @Inject constructor(
    private val repository: InternalRepository,
    private val agentRepository: AgentRepository
): ViewModel() {

    val currentUser: FirebaseUser?
        get() = repository.currentUser

    //TO ADD OFFERING
    private val _internalProducts = MutableLiveData<Resource<List<InternalProduct>>>()
    val internalProducts: LiveData<Resource<List<InternalProduct>>> get() = _internalProducts

    private val _addOfferingFlow = MutableStateFlow<Resource<FirebaseFirestore>?>(null)
    val addOfferingFlow: StateFlow<Resource<FirebaseFirestore>?> = _addOfferingFlow

    fun addOffering(offering: OfferingForAgent) = viewModelScope.launch {
        val result = repository.addOfferingForAgent(offering){
        }
        _addOfferingFlow.value = result
    }

    // TO GET OFFERING
    private val _offeringAgentsSearch = MutableStateFlow<Resource<List<OfferingForAgent>>>(Resource.Loading)

    private val _offeringAgents = MutableLiveData<Resource<List<OfferingForAgent>>>()
    val offeringAgents: LiveData<Resource<List<OfferingForAgent>>> get() = _offeringAgents

    fun fetchOfferingForAgent() {
        viewModelScope.launch {
            _offeringAgents.value = Resource.Loading
            val result = repository.getOfferingForAgent()
            _offeringAgents.value = result
            _offeringAgentsSearch.value = result
            // Update filtered list based on initial data
            _offeringAgentsList.value = mapToOfferingList(_offeringAgentsSearch.value) ?: emptyList()
        }
    }

    private val _offeringAgentsById = MutableLiveData<Resource<List<OfferingForAgent>>>()
    val offeringAgentsById: LiveData<Resource<List<OfferingForAgent>>> get() = _offeringAgentsById

    fun fetchOfferingForAgentById(){
        viewModelScope.launch {
            _offeringAgentsById.value = Resource.Loading
            val result = agentRepository.getOfferingForAgentById()
            _offeringAgentsById.value = result
        }
    }

    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    //second state the text typed by the user
    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    //filter for list order by sales
    private val _offeringAgentsList = MutableStateFlow(emptyList<OfferingForAgent>())
    val offeringAgentList = searchText
        .combine(_offeringAgentsList) { text, orders ->//combine searchText with _contriesList
            if (text.isBlank()) { //return the entery list of countries if not is typed
                orders
            }
            orders.filter { offering ->// filter and return a list of countries based on the text the user typed
                offering.nameAgent!!.uppercase().contains(text.trim().uppercase())
            }
        }.stateIn(//basically convert the Flow returned from combine operator to StateFlow
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),//it will allow the StateFlow survive 5 seconds before it been canceled
            initialValue = _offeringAgentsList.value
        )

    val offeringAgentListSize = searchText
        .combine(_offeringAgentsList) { text, orders ->//combine searchText with _contriesList
            if (text.isBlank()) { //return the entery list of countries if not is typed
                orders
            }
            orders.filter { offering ->// filter and return a list of countries based on the text the user typed
                offering.nameAgent == currentUser?.displayName
            }
        }.stateIn(//basically convert the Flow returned from combine operator to StateFlow
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),//it will allow the StateFlow survive 5 seconds before it been canceled
            initialValue = _offeringAgentsList.value
        )

    init {
        viewModelScope.launch {
            _offeringAgentsSearch.value = repository.getOfferingForAgent()
            // Update filtered list based on initial data
            _offeringAgentsList.value = mapToOfferingList(_offeringAgentsSearch.value) ?: emptyList()
        }
    }

    private fun mapToOfferingList(resource: Resource<List<OfferingForAgent>>): List<OfferingForAgent>? {
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

    // DELETE OFFERING
    private val _deleteOfferingFlow = MutableStateFlow<Resource<Boolean>?>(null)
    val deleteOfferingFlow: StateFlow<Resource<Boolean>?> = _deleteOfferingFlow

    fun deleteOffering(idOffering: String) = viewModelScope.launch {
        _deleteOfferingFlow.value = Resource.Loading
        val result = repository.deletePoAgent(idOffering)
        _deleteOfferingFlow.value = result
    }
}