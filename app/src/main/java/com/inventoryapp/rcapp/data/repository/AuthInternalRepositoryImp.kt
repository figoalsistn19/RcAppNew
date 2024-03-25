package com.inventoryapp.rcapp.data.repository

import android.content.SharedPreferences
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import com.inventoryapp.rcapp.data.model.InternalUser
import com.inventoryapp.rcapp.util.FireStoreCollection
import com.inventoryapp.rcapp.util.Resource
import com.inventoryapp.rcapp.util.SharedPrefConstants
import com.inventoryapp.rcapp.util.await
import javax.inject.Inject

class AuthInternalRepositoryImp @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val database: FirebaseFirestore,
    private val appPreferences: SharedPreferences,
    private val gson: Gson
): AuthInternalRepository {
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

    override suspend fun updateUserInfo(
        user: InternalUser,
        result: (Resource<String>) -> Unit
    ): Resource<FirebaseFirestore> {
        return try {
            val document = database.collection(FireStoreCollection.INTERNALUSER).document(user.idUser)
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