package com.inventoryapp.rcapp.data.repository

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.inventoryapp.rcapp.data.model.AgentProduct
import com.inventoryapp.rcapp.data.model.AgentStockTransaction
import com.inventoryapp.rcapp.data.model.AgentUser
import com.inventoryapp.rcapp.data.model.InternalProduct
import com.inventoryapp.rcapp.data.model.OfferingBySales
import com.inventoryapp.rcapp.util.Resource

interface AgentRepository {

    //AUTH REPO
    val currentUser: FirebaseUser?
    suspend fun login (email: String, password: String): Resource<FirebaseUser>
    suspend fun register (name: String, email: String, password: String, user: AgentUser): Resource<FirebaseUser>
    suspend fun updateUserInfo(user: AgentUser, result: (Resource<String>) -> Unit): Resource<FirebaseFirestore>
    suspend fun storeSession(id: String, result: (AgentUser?) -> Unit): Resource<FirebaseFirestore>
    fun getSession(result: (AgentUser?) -> Unit)
    fun logout()

    //AGENT REPO
    suspend fun addAgentProduct(product: AgentProduct, result: (Resource<String>) -> Unit): Resource<FirebaseFirestore>

    suspend fun addAgentStockTransaction(
        transaction: AgentStockTransaction,
        idProduct: String,
        offering: OfferingBySales,
        result: (Resource<String>) -> Unit
    ) :Resource<FirebaseFirestore>

    suspend fun getAgentProduct(): Resource<List<AgentProduct>>

    suspend fun getAgentTransaction(): Resource<List<AgentStockTransaction>>
    suspend fun getProductData(): Resource<List<InternalProduct>>
}
