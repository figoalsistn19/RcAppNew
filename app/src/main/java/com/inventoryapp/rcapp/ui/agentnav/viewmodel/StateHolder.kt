package com.inventoryapp.rcapp.ui.agentnav.viewmodel

class StateHolder<T>(initialValue: T) {
    private var _value: T = initialValue

    val value: T
        get() = _value

    fun updateValue(newValue: T) {
        _value = newValue
    }

    private var _navBarValue: T = initialValue

    val valueNavBar: T
        get() = _navBarValue

    fun updateValueNavBar (newValue: T){
        _navBarValue = newValue
    }
}