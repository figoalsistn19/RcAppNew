package com.inventoryapp.rcapp.data.model

import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class InternalUser(
    val idUser: String,
    val name: String,
    val email: String,
    val phoneNumber: String,
    val userRole: VerifAccountStatus,
    @ServerTimestamp
    val createAt: Date
)
