package com.inventoryapp.rcapp.data.model

import android.os.Parcelable
import com.google.firebase.firestore.ServerTimestamp
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class SalesOrder(
    val idOrder: String? ="",
    val idAgent: String? ="",
    val nameAgent: String? ="",
    val name: String? ="",
    val email: String? ="",
    val productsItem: List<ProductsItem>,
    @ServerTimestamp
    val orderDate: Date? = null,
    val statusOrder: String? = "",
    var totalPrice: Long? = null,
    val tax: Int? = null
) :Parcelable {
    init {
        var totalPriceCalculation = 0L
        for (product in productsItem) {
            totalPriceCalculation += product.totalPrice ?: 0
        }
        totalPrice = totalPriceCalculation
    }
}

@Parcelize
data class ProductsItem(
    var idProduct: String? ="",
    var productName: String? ="",
    var price: Long? = null,
    var quantity: Int? = null,
    var discProduct: Int? = null,
    var finalPrice: Long? = null,
    var totalPrice: Long? = null
) :Parcelable