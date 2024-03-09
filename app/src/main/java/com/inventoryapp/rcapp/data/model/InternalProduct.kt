package com.inventoryapp.rcapp.data.model

import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class InternalProduct(
    val idProduct: String,
    val productName: String,
    val qtyProduct: Int,
    val qtyMin: Int,
    val discProduct: Int,
    val price: Long,
    val finalPrice: Long,
    @ServerTimestamp
    val updateAt: Date,
    val desc: String
)
