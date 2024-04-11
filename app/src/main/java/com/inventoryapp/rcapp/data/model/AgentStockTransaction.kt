package com.inventoryapp.rcapp.data.model

import android.os.Parcelable
import com.google.firebase.firestore.ServerTimestamp
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class AgentStockTransaction(
    val idAgentStockTransaction: String? ="",
    val idProduct: String? ="",
    val productName: String? ="",
    val qtyProduct: Int? = null,
    val transactionType: String? ="",
    @ServerTimestamp
    val createAt: Date? = null,
    val desc: String? =""
): Parcelable
