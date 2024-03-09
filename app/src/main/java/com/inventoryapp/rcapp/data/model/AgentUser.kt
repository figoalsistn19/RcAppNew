package com.inventoryapp.rcapp.data.model

import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class AgentUser(
    var idAgent: String,
    val name: String,
    val email: String,
    val phoneNumber: String,
    val address: String,
    val verificationStatus: VerifAccountStatus,
    @ServerTimestamp
    val createAt: Date
)
