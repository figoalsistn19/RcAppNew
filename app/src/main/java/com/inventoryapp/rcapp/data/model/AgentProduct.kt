package com.inventoryapp.rcapp.data.model

import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class AgentProduct(
    val idAgentProduct: String,
    val idAgent: AgentUser,
    val productName: String,
    val qtyProduct: Int,
    val qtyMin: Int,
    @ServerTimestamp
    val updateAt: Date,
    val desc: String
)
