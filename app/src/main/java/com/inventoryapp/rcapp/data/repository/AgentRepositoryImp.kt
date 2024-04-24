package com.inventoryapp.rcapp.data.repository

import android.content.SharedPreferences
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import com.inventoryapp.rcapp.data.model.AgentProduct
import com.inventoryapp.rcapp.data.model.AgentStockTransaction
import com.inventoryapp.rcapp.data.model.AgentUser
import com.inventoryapp.rcapp.data.model.InternalProduct
import com.inventoryapp.rcapp.data.model.OfferingBySales
import com.inventoryapp.rcapp.util.FireStoreCollection
import com.inventoryapp.rcapp.util.FirebaseCoroutines
import com.inventoryapp.rcapp.util.Resource
import com.inventoryapp.rcapp.util.SharedPrefConstants
import com.inventoryapp.rcapp.util.await
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AgentRepositoryImp @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val database: FirebaseFirestore,
    private val appPreferences: SharedPreferences,
    private val gson: Gson
): AgentRepository {
    override val currentUser: FirebaseUser?
        get() = firebaseAuth.currentUser

    override suspend fun login(email: String, password: String): Resource<FirebaseUser> {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            val userId = result.user!!.uid // Extract the user ID

            val documentSnapshot = FirebaseFirestore.getInstance()
                .collection("AgentUser")
                .whereEqualTo("idAgent", userId) // Use userId for comparison
                .whereEqualTo("verificationStatus", "APPROVED")
                .get().await()

            if (documentSnapshot.isEmpty) {
                // Handle case where user data or approved status not found
                return Resource.Failure(Exception("User not found or verification not approved"))
            } else {
                for (doc in documentSnapshot){
                    val agentUser = doc.toObject(AgentUser::class.java)
                    appPreferences.edit().putString(SharedPrefConstants.USER_NAME, agentUser.name).apply()
                    appPreferences.edit().putString(SharedPrefConstants.USER_ID, agentUser.idAgent).apply()
                    appPreferences.edit().putString(SharedPrefConstants.USER_STATUS, agentUser.verificationStatus.toString()).apply()
                }
                Resource.Success(result.user!!)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override suspend fun storeSession(
        id: String,
        result: (AgentUser?) -> Unit
    ): Resource<FirebaseFirestore> {
        return try {
            database.collection(FireStoreCollection.AGENTUSER).document(id)
                .get()
            appPreferences.edit().putString(SharedPrefConstants.USER_SESSION, gson.toJson(result))
                .apply()
            appPreferences.edit().putString(SharedPrefConstants.USER_STATUS, gson.toJson(result))
                .apply()
            appPreferences.edit().putString(SharedPrefConstants.USER_NAME, gson.toJson(result))
                .apply()
            appPreferences.edit().putString(SharedPrefConstants.USER_ID, gson.toJson(result))
                .apply()
            appPreferences.edit().putString(SharedPrefConstants.USER_STATUS, gson.toJson(result))
                .apply()
            Resource.Success(database)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override suspend fun getProductData(): Resource<List<InternalProduct>> {
        return try {
            val querySnapshot = database
                .collection(FireStoreCollection.INTERNALPRODUCT)
                .get()
                .await()

            val productList = mutableListOf<InternalProduct>()
            for (document in querySnapshot.documents) {
                val product = document.toObject(InternalProduct::class.java)
                if (product != null) {
                    productList.add(product)
                }
            }

            Resource.Success(productList)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override fun getSession(result: (AgentUser?) -> Unit) {
        val userStr = appPreferences.getString(SharedPrefConstants.USER_SESSION, null)
        val userStatus = appPreferences.getString(SharedPrefConstants.USER_STATUS,null)
        if (userStr == null && userStatus== "PENDING") {
            result.invoke(null)
        }
        else {
            val user = gson.fromJson(userStr, AgentUser::class.java)
            result.invoke(user)
        }
    }


    override suspend fun register(
        name: String,
        email: String,
        password: String,
        user: AgentUser
    ): Resource<FirebaseUser> {
        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            result?.user?.updateProfile(
                UserProfileChangeRequest.Builder().setDisplayName(name).build()
            )?.await()
            user.idAgent = result.user?.uid ?: ""
            updateUserInfo(user) {
            }
            Resource.Success(result.user!!)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override suspend fun updateUserInfo(
        user: AgentUser,
        result: (Resource<String>) -> Unit
    ): Resource<FirebaseFirestore> {
        return try {
            val document = database.collection(FireStoreCollection.AGENTUSER).document(user.idAgent!!)
            document.set(user).await()
            Resource.Success(database)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override fun logout() {
        firebaseAuth.signOut()
        appPreferences.edit().putString(SharedPrefConstants.USER_SESSION, null).apply()
        appPreferences.edit().putString(SharedPrefConstants.USER_STATUS, null).apply()
        appPreferences.edit().putString(SharedPrefConstants.USER_NAME, null).apply()
        appPreferences.edit().putString(SharedPrefConstants.USER_ID, null).apply()
    }

    override suspend fun addAgentProduct(
        product: AgentProduct,
        result: (Resource<String>) -> Unit
    ): Resource<FirebaseFirestore> {
        return try {
            val firebaseResult = database
                .collection(FireStoreCollection.AGENTUSER)
                .document(currentUser!!.uid)
                .collection(FireStoreCollection.AGENTPRODUCT)
                .document(product.idProduct!!)
//            val idProduct = firebaseResult.document().id // Dapatkan ID yang dihasilkan secara otomatis
//            product.idProduct = idProduct
            firebaseResult.set(product).await()
            Resource.Success(database)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override suspend fun addAgentStockTransaction(
        transaction: AgentStockTransaction,
        idProduct: String,
        offering: OfferingBySales,
        result: (Resource<String>) -> Unit
    ): Resource<FirebaseFirestore> {
        return try {
            val stockRef = database.collection(FireStoreCollection.AGENTUSER)
                .document(currentUser!!.uid)
                .collection(FireStoreCollection.AGENTPRODUCT)
                .document(idProduct)
            val transactionRef = database.collection(FireStoreCollection.AGENTUSER)
                .document(currentUser!!.uid)
                .collection(FireStoreCollection.AGENTTRANSACTION)
                .document()

            val idAgentTransaction = transactionRef.id // Dapatkan ID yang dihasilkan secara otomatis
            transaction.idAgentStockTransaction = idAgentTransaction

            database.runTransaction { _transaction ->
                val stockSnapshot = _transaction.get(stockRef)

                val currentStock = stockSnapshot.getLong("qtyProduct") ?: 0
                val stockMin = stockSnapshot.getLong("qtyMin") ?: 0

                val updateReqOrderRef = database.collection(FireStoreCollection.OFFERINGBYSALES)
                    .document(offering.idOffering!!)
//                val idOffering = updateReqOrderRef.id
//                offering.idOffering = idOffering
                val updatedStock = currentStock + transaction.qtyProduct!!

                _transaction.update(stockRef, "qtyProduct", updatedStock)
                _transaction.set(transactionRef, transaction)
                if (updatedStock <= stockMin){
                    _transaction.set(updateReqOrderRef, offering)
                }

//                _transaction.set(updateReqOrderRef,)

                null
            }.await()
            Resource.Success(database)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override suspend fun getAgentProduct(): Resource<List<AgentProduct>> {
        return withContext(Dispatchers.IO) {
            val userId = if (currentUser != null)
                currentUser!!.uid
            else "ZeVxsI1nTCeZEprqlzZpti00sC42"
            val querySnapshot = database
                .collection(FireStoreCollection.AGENTUSER)
                .document(userId)
                .collection(FireStoreCollection.AGENTPRODUCT)
                .get()
            when (val taskResult = FirebaseCoroutines.awaitTask(querySnapshot)) {
                is Resource.Success -> {
                    val documents = taskResult.result
                    val users = mutableListOf<AgentProduct>()
                    for (document in documents) {
                        val user = document.toObject(AgentProduct::class.java)
                        users.add(user)
                    }
                    Resource.Success(users)
                }

                is Resource.Failure -> {
                    Resource.Failure(taskResult.throwable)
                }

                Resource.Loading -> TODO()
            }
        }
    }

    override suspend fun getAgentTransaction(): Resource<List<AgentStockTransaction>> {
        return withContext(Dispatchers.IO) {
            val userId = if (currentUser != null)
                currentUser!!.uid
            else "ZeVxsI1nTCeZEprqlzZpti00sC42"
            val querySnapshot = database
                .collection(FireStoreCollection.AGENTUSER)
                .document(userId)
                .collection(FireStoreCollection.AGENTTRANSACTION)
                .get()
            val taskResult = FirebaseCoroutines.awaitTask(querySnapshot)
            when (taskResult) {
                is Resource.Success -> {
                    val documents = taskResult.result
                    val users = mutableListOf<AgentStockTransaction>()
                    for (document in documents) {
                        val transaction = document.toObject(AgentStockTransaction::class.java)
                        users.add(transaction)
                    }
                    Resource.Success(users)
                }

                is Resource.Failure -> {
                    Resource.Failure(taskResult.throwable)
                }

                Resource.Loading -> TODO()
            }
        }
    }
//        val userStr = appPreferences.getString(SharedPrefConstants.USER_SESSION, null)
//        if (userStr!!.isEmpty()) {
//            // Handle case where user data or approved status not found
//            return Resource.Failure(Exception("No data available!!"))
//        } else
//            return withContext(Dispatchers.IO) {
//                val querySnapshot = database
//                    .collection(FireStoreCollection.AGENTUSER)
//                    .document(currentUser!!.uid)
//                    .collection(FireStoreCollection.AGENTPRODUCT)
//                    .get()
//                val taskResult = FirebaseCoroutines.awaitTask(querySnapshot)
//                when (taskResult) {
//                    is Resource.Success -> {
//                        val documents = taskResult.result
//                        val users = mutableListOf<AgentProduct>()
//                        for (document in documents) {
//                            val user = document.toObject(AgentProduct::class.java)
//                            users.add(user)
//                        }
//                        Resource.Success(users)
//                    }
//                    is Resource.Failure -> {
//                        Resource.Failure(taskResult.throwable)
//                    }
//
//                    Resource.Loading -> TODO()
//                }
//            }

//        if (userStr != null){
//            return withContext(Dispatchers.IO) {
//                val querySnapshot = database
//                    .collection(FireStoreCollection.AGENTUSER)
//                    .document(userStr)
//                    .collection(FireStoreCollection.AGENTPRODUCT)
//                    .get()
//                val taskResult = FirebaseCoroutines.awaitTask(querySnapshot)
//                when (taskResult) {
//                    is Resource.Success -> {
//                        val documents = taskResult.result
//                        val users = mutableListOf<AgentProduct>()
//                        for (document in documents) {
//                            val user = document.toObject(AgentProduct::class.java)
//                            users.add(user)
//                        }
//                        Resource.Success(users)
//                    }
//                    is Resource.Failure -> {
//                        Resource.Failure(taskResult.throwable)
//                    }
//
//                    Resource.Loading -> TODO()
//                }
//            }
//        } else
//            return withContext(Dispatchers.IO) {
//                val querySnapshot = database
//                    .collection(FireStoreCollection.AGENTUSER)
//                    .document("ZeVxsI1nTCeZEprqlzZpti00sC42")
//                    .collection(FireStoreCollection.AGENTPRODUCT)
//                    .get()
//                val taskResult = FirebaseCoroutines.awaitTask(querySnapshot)
//                when (taskResult) {
//                    is Resource.Success -> {
//                        val documents = taskResult.result
//                        val users = mutableListOf<AgentProduct>()
//                        for (document in documents) {
//                            val user = document.toObject(AgentProduct::class.java)
//                            users.add(user)
//                        }
//                        Resource.Success(users)
//                    }
//                    is Resource.Failure -> {
//                        Resource.Failure(taskResult.throwable)
//                    }
//
//                    Resource.Loading -> TODO()
//                }
//            }

}