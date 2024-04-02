package com.inventoryapp.rcapp.data.model

import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class SalesOrder(
    val idOrder: String,
    val idAgent: String,
    val name: String,
    val email: String,
    val productsItem: List<ProductsItem>,
    @ServerTimestamp
    val orderDate: Date,
    val statusOrder: StatusOrder,
    val totalPrice: Long,
    val tax: Int
)

data class ProductsItem(
    val idProduct: String,
    val productName: String,
    val price: Long?,
    val quantity: Int,
    val totalPrice: Long
)