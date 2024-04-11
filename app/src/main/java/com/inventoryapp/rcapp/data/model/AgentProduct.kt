package com.inventoryapp.rcapp.data.model

import android.os.Parcelable
import com.google.firebase.firestore.ServerTimestamp
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class AgentProduct(
    var idProduct: String? ="",
    var productName: String? ="",
    var qtyProduct: Int? = null,
    var qtyMin: Int? = null,
    @ServerTimestamp
    var updateAt: Date? = null,
    var desc: String? =""
): Parcelable
