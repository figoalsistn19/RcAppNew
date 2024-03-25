package com.inventoryapp.rcapp.ui.nav

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlin.reflect.KProperty


class BottomNavAgentViewModel : ViewModel() {
    private val _selectedItemIndexed = MutableStateFlow(0)
    val selectedItemIndexed : Int get()= _selectedItemIndexed.value

    fun updateSelectedItemIndexed(index: Int) {
        _selectedItemIndexed.value = index
    }

    private val _count = MutableStateFlow(0)
    val count: StateFlow<Int> = _count.asStateFlow()

    fun updateCount(value: Int) {
        _count.value = value
    }
}