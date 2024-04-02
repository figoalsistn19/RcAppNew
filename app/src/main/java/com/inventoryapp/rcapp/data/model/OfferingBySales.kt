package com.inventoryapp.rcapp.data.model

data class OfferingBySales(
    val idOffering: String,
    val idProduct: String,
    val productName: String,
    val price: Long,
    val quantity: Int,
    val finalPrice: Long,
    val desc: String
)
