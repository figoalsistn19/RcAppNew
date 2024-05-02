package com.inventoryapp.rcapp.data.model

import android.os.Parcelable
import com.google.firebase.firestore.ServerTimestamp
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class InternalStockTransaction(
    var idTransaction: String? ="",
    var idProduct: String? ="",
    var productName: String? ="",
    var qtyProduct: Int? = null,
    var transactionType: String? ="",
    var userEditor: String? = "",
    @ServerTimestamp
    var createAt: Date? = null,
    var desc: String? =""
): Parcelable

