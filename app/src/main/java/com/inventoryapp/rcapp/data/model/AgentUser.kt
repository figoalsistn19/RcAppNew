package com.inventoryapp.rcapp.data.model

import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class AgentUser(
    var idAgent: String? = null,
    val name: String? = null,
    val email: String? = null,
    val phoneNumber: String? = null,
    val address: String? = null,
    val verificationStatus: VerifAccountStatus? = null,
    @ServerTimestamp
    val createAt: Date? = null
)
