package com.inventoryapp.rcapp.data.model

import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class InternalUser(
    var idUser: String,
    val name: String,
    val email: String,
    val phoneNumber: String,
    val userRole: UserRole,
    @ServerTimestamp
    val createAt: Date
)

