package com.inventoryapp.rcapp.data.model

import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class InternalUser(
    var idUser: String? = null,
    val name: String? = null,
    val email: String? = null,
    val phoneNumber: String? = null,
    val userRole: UserRole? = null,
    @ServerTimestamp
    val createAt: Date? = null
)

