package com.inventoryapp.rcapp.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class OfferingBySales(
    var idOffering: String? ="",
    var idAgent: String? ="",
    var nameAgent: String? = "",
    var totalPrice: Long? = null,
    var desc: String? ="",
    var productsItem: List<ProductsItem>? = null,
    var statusOffering: String? =""
) :Parcelable