package com.inventoryapp.rcapp.ui.internalnav.viewmodel

import androidx.lifecycle.ViewModel
import com.inventoryapp.rcapp.data.repository.InternalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class InternalTransactionViewModel @Inject constructor(
    private val repository: InternalRepository
): ViewModel() {

}