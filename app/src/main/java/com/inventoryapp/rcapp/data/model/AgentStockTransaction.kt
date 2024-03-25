package com.inventoryapp.rcapp.data.model

import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class AgentStockTransaction(
    val idAgentStockTransaction: String,
    val idAgent: String,
    val productName: String,
    val qtyProduct: Int,
    val transactionType: TransactionType,
    @ServerTimestamp
    val createAt: Date,
    val desc: String
)
