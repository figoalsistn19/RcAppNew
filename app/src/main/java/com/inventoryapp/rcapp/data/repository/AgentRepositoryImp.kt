package com.inventoryapp.rcapp.data.repository

import android.content.SharedPreferences
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import com.inventoryapp.rcapp.data.model.AgentUser
import com.inventoryapp.rcapp.data.model.InternalProduct
import com.inventoryapp.rcapp.util.FireStoreCollection
import com.inventoryapp.rcapp.util.Resource
import com.inventoryapp.rcapp.util.SharedPrefConstants
import com.inventoryapp.rcapp.util.await
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
    }
}