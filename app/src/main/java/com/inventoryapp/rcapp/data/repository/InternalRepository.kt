package com.inventoryapp.rcapp.data.repository

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.inventoryapp.rcapp.data.model.AgentStockTransaction
import com.inventoryapp.rcapp.data.model.AgentUser
import com.inventoryapp.rcapp.data.model.InternalProduct
import com.inventoryapp.rcapp.data.model.InternalUser
import com.inventoryapp.rcapp.data.model.OfferingBySales
import com.inventoryapp.rcapp.util.Resource

interface InternalRepository {

    //AUTH REPO
    val currentUser: FirebaseUser?
    suspend fun login (email: String, password: String): Resource<FirebaseUser>
    suspend fun register (name: String, email: String, password: String, user: InternalUser): Resource<FirebaseUser>
    suspend fun updateUserInfo(user: InternalUser, result: (Resource<String>) -> Unit): Resource<FirebaseFirestore>
    suspend fun storeSession(id: String, result: (InternalUser?) -> Unit): Resource<FirebaseFirestore>
    fun getSession(result: (InternalUser?) -> Unit)
    fun logout()

    //INTERNAL REPO
    suspend fun addInternalProduct(product: InternalProduct, result: (Resource<String>) -> Unit): Resource<FirebaseFirestore>

    suspend fun getInternalProducts(): Resource<List<InternalProduct>>

    suspend fun getUsers(): Resource<List<InternalUser>>

    suspend fun getAgentUsers(): Resource<List<AgentUser>>

    suspend fun addOfferingBySales(
        offering: OfferingBySales,
        result: (Resource<String>) -> Unit
    ) :Resource<FirebaseFirestore>

    suspend fun getOfferingBySales(): Resource<List<OfferingBySales>>
    suspend fun addInternalStockTransaction(
        transaction: AgentStockTransaction,
        idProduct: String,
        result: (Resource<String>) -> Unit
    ) :Resource<FirebaseFirestore>

    suspend fun getInternalStockTransaction(): Resource<List<AgentStockTransaction>>
}