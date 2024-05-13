package com.inventoryapp.rcapp.data.repository

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.inventoryapp.rcapp.data.model.AgentProduct
import com.inventoryapp.rcapp.data.model.AgentStockTransaction
import com.inventoryapp.rcapp.data.model.AgentUser
import com.inventoryapp.rcapp.data.model.InternalProduct
import com.inventoryapp.rcapp.data.model.InternalStockTransaction
import com.inventoryapp.rcapp.data.model.InternalUser
import com.inventoryapp.rcapp.data.model.OfferingForAgent
import com.inventoryapp.rcapp.data.model.ProductsItem
import com.inventoryapp.rcapp.data.model.SalesOrder
import com.inventoryapp.rcapp.data.model.StatusOrder
import com.inventoryapp.rcapp.data.model.VerifAccountStatus
import com.inventoryapp.rcapp.util.Resource

interface InternalRepository {

    //AUTH REPO
    val currentUser: FirebaseUser?

    suspend fun login (
        email: String,
        password: String,
    ): Resource<FirebaseUser>
    suspend fun register (name: String, email: String, password: String, user: InternalUser): Resource<FirebaseUser>
    suspend fun updateUserInfo(user: InternalUser, result: (Resource<String>) -> Unit): Resource<FirebaseFirestore>
    suspend fun storeSession(id: String, result: (InternalUser?) -> Unit): Resource<FirebaseFirestore>
    fun getSession(result: (InternalUser?) -> Unit)

    suspend fun getAgentProduct(idAgent: String): Resource<List<AgentProduct>>
    fun logout()

    //INTERNAL REPO
    suspend fun addInternalProduct(product: InternalProduct, result: (Resource<String>) -> Unit): Resource<FirebaseFirestore>

    suspend fun deleteInternalProduct(
        idProduct: String
    ): Resource<Boolean>
    suspend fun getInternalProducts(): Resource<List<InternalProduct>>

    suspend fun getCardData(): Resource<List<ProductsItem>>

    suspend fun getUsers(): Resource<List<InternalUser>>

    suspend fun getAgentUsers(): Resource<List<AgentUser>>

    suspend fun getInternalUser(idUser: String): Resource<InternalUser>

    suspend fun updateStatusAgent(
        idUser: String,
        status: VerifAccountStatus,
        result: (Resource<String>) -> Unit
        ): Resource<FirebaseFirestore>

    suspend fun updateStatusOrder(
        idSalesOrder: String,
        statusOrder: String,
        result: (Resource<String>) -> Unit
    ): Resource<FirebaseFirestore>

    suspend fun updateInternalProduct(
        internalProduct: InternalProduct,
        result: (Resource<String>) -> Unit
    ): Resource<FirebaseFirestore>
    suspend fun addOfferingForAgent(
        offering: OfferingForAgent,
        result: (Resource<String>) -> Unit
    ) :Resource<FirebaseFirestore>

    suspend fun deleteSalesOrder(
        idSalesOrder: String
    ) :Resource<Boolean>

    suspend fun getOfferingForAgent(): Resource<List<OfferingForAgent>>

    suspend fun deleteOfferingForAgent(
        idOffering: String
    ): Resource<FirebaseFirestore>
    suspend fun getSalesOrder(): Resource<List<SalesOrder>>

    suspend fun addInternalStockTransaction(
        transaction: InternalStockTransaction,
        idProduct: String,
        result: (Resource<String>) -> Unit
    ) :Resource<FirebaseFirestore>

    suspend fun fetchCollectionSize(collectionPath: String): Int
    suspend fun getInternalStockTransaction(): Resource<List<InternalStockTransaction>>
}