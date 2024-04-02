package com.inventoryapp.rcapp.data.repository

import android.content.SharedPreferences
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import com.inventoryapp.rcapp.data.model.AgentUser
import com.inventoryapp.rcapp.data.model.InternalProduct
import com.inventoryapp.rcapp.data.model.InternalUser
import com.inventoryapp.rcapp.util.FireStoreCollection
import com.inventoryapp.rcapp.util.FireStoreCollection.INTERNALPRODUCT
import com.inventoryapp.rcapp.util.FirebaseCoroutines
import com.inventoryapp.rcapp.util.Resource
import com.inventoryapp.rcapp.util.SharedPrefConstants
import com.inventoryapp.rcapp.util.await
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class InternalRepositoryImp @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val database: FirebaseFirestore,
    private val appPreferences: SharedPreferences,
    private val gson: Gson
): InternalRepository {
    override val currentUser: FirebaseUser?
        get() = firebaseAuth.currentUser

    override suspend fun login(email: String, password: String): Resource<FirebaseUser> {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            Resource.Success(result.user!!)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override suspend fun storeSession(
        id: String,
        result: (InternalUser?) -> Unit
    ): Resource<FirebaseFirestore> {
        return try {
            database.collection(FireStoreCollection.INTERNALUSER).document(id)
                .get()
            appPreferences.edit().putString(SharedPrefConstants.USER_SESSION, gson.toJson(result))
                .apply()
            appPreferences.edit().putString(SharedPrefConstants.USER_NAME, gson.toJson(result))
                .apply()
            Resource.Success(database)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override suspend fun addInternalProduct(
        product: InternalProduct,
        result: (Resource<String>) -> Unit
    ): Resource<FirebaseFirestore> {
        return try {
            val firebaseResult = database
                .collection(INTERNALPRODUCT)
                .document()
            val idProduct = firebaseResult.id // Dapatkan ID yang dihasilkan secara otomatis
            product.idProduct = idProduct
            firebaseResult.set(product).await()
            Resource.Success(database)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

//    override suspend fun getInternalProductData(): Resource<MutableList<InternalProduct>> {
//        return try {
//            val querySnapshot = database
//                .collection(INTERNALPRODUCT)
//                .get()
//                .await()
//
//            val productList = mutableListOf<InternalProduct>()
//            for (document in querySnapshot.documents) {
//                val product = document.toObject<InternalProduct>()
////                    InternalProduct(
////                    idProduct = document.getString("idProduct"),
////                    productName = document.getString("productName"),
////                    qtyProduct = document.get("qtyProduct") as Int,
////                    qtyMin = document.get("qtyMin") as Int,
////                    discProduct = document.get("discProduct") as Int,
////                    price = document.getLong("price"),
////                    finalPrice = document.getLong("finalPrice"),
////                    updateAt = document.getDate("updateAt"),
////                    desc = document.getString("desc")
////                )
//                product.let {
//                    if (it != null) {
//                        productList.add(it)
//                    }
//                }
//            }
//
//            Resource.Success(productList)
//        } catch (e: Exception) {
//            e.printStackTrace()
//            Resource.Failure(e)
//        }
//    }



    override suspend fun updateUserInfo(
        user: InternalUser,
        result: (Resource<String>) -> Unit
    ): Resource<FirebaseFirestore> {
        return try {
            val document = database
                .collection(FireStoreCollection.INTERNALUSER)
                .document(user.idUser)
            document.set(user).await()
            Resource.Success(database)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }
    override fun getSession(result: (InternalUser?) -> Unit) {
        val userStr = appPreferences.getString(SharedPrefConstants.USER_SESSION, null)
        if (userStr == null) {
            result.invoke(null)
        } else {
            val user = gson.fromJson(userStr, InternalUser::class.java)
            result.invoke(user)
        }
    }

    override suspend fun register(
        name: String,
        email: String,
        password: String,
        user: InternalUser
    ): Resource<FirebaseUser> {
        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            result?.user?.updateProfile(
                UserProfileChangeRequest.Builder().setDisplayName(name).build()
            )?.await()
            user.idUser = result.user?.uid ?: ""
            updateUserInfo(user) {
            }
            Resource.Success(result.user!!)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override fun logout() {
        firebaseAuth.signOut()
        appPreferences.edit().putString(SharedPrefConstants.USER_SESSION, null).apply()
    }

    override suspend fun getInternalProducts(): Resource<List<InternalProduct>> {
        return withContext(Dispatchers.IO) {
            val querySnapshot = database.collection(INTERNALPRODUCT).get()
            val taskResult = FirebaseCoroutines.awaitTask(querySnapshot)
            when (taskResult) {
                is Resource.Success -> {
                    val documents = taskResult.result
                    val users = mutableListOf<InternalProduct>()
                    for (document in documents) {
                        val user = document.toObject(InternalProduct::class.java)
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

    override suspend fun getUsers(): Resource<List<InternalUser>> {
        return withContext(Dispatchers.IO) {
            val querySnapshot = database.collection("users").get()
            val taskResult = FirebaseCoroutines.awaitTask(querySnapshot)
            when (taskResult) {
                is Resource.Success -> {
                    val documents = taskResult.result
                    val users = mutableListOf<InternalUser>()
                    for (document in documents) {
                        val user = document.toObject(InternalUser::class.java)
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

    override suspend fun getAgentUsers(): Resource<List<AgentUser>> {
        return withContext(Dispatchers.IO){
            val querySnapshot = database.collection(FireStoreCollection.AGENTUSER).get()
            val taskResult = FirebaseCoroutines.awaitTask(querySnapshot)
            when (taskResult){
                is Resource.Success -> {
                    val documents = taskResult.result
                    val users = mutableListOf<AgentUser>()
                    for (document in documents){
                        val user = document.toObject(AgentUser::class.java)
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
}