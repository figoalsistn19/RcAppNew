package com.inventoryapp.rcapp.data.repository

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.inventoryapp.rcapp.data.model.AgentUser
import com.inventoryapp.rcapp.data.model.InternalUser
import com.inventoryapp.rcapp.util.Resource

interface AuthInternalRepository {
    val currentUser: FirebaseUser?
    suspend fun login (email: String, password: String): Resource<FirebaseUser>
    suspend fun register (name: String, email: String, password: String, user: InternalUser): Resource<FirebaseUser>
    suspend fun updateUserInfo(user: InternalUser, result: (Resource<String>) -> Unit): Resource<FirebaseFirestore>
    suspend fun storeSession(id: String, result: (InternalUser?) -> Unit): Resource<FirebaseFirestore>
    fun getSession(result: (InternalUser?) -> Unit)
    fun logout()
}