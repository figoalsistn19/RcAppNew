package com.inventoryapp.rcapp.data.model

import android.os.Parcelable
import com.google.firebase.firestore.ServerTimestamp
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class SalesOrder(
    var idOrder: String? ="",
    var idAgent: String? ="",
    var nameAgent: String? ="",
    var email: String? ="",
    var productsItem: List<ProductsItem>?= null,
    @ServerTimestamp
    var orderDate: Date? = null,
    var statusOrder: String? = "",
    var totalPrice: Long? = null,
    var tax: Int? = null
) :Parcelable

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