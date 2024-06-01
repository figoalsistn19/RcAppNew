package com.inventoryapp.rcapp.data.repository

import android.content.SharedPreferences
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.Source
import com.google.gson.Gson
import com.inventoryapp.rcapp.data.model.AgentProduct
import com.inventoryapp.rcapp.data.model.AgentUser
import com.inventoryapp.rcapp.data.model.InternalProduct
import com.inventoryapp.rcapp.data.model.InternalStockTransaction
import com.inventoryapp.rcapp.data.model.InternalUser
import com.inventoryapp.rcapp.data.model.OfferingForAgent
import com.inventoryapp.rcapp.data.model.ProductsItem
import com.inventoryapp.rcapp.data.model.SalesOrder
import com.inventoryapp.rcapp.data.model.UserRole
import com.inventoryapp.rcapp.data.model.VerifAccountStatus
import com.inventoryapp.rcapp.util.FireStoreCollection
import com.inventoryapp.rcapp.util.FireStoreCollection.AGENTUSER
import com.inventoryapp.rcapp.util.FireStoreCollection.INTERNALPRODUCT
import com.inventoryapp.rcapp.util.FireStoreCollection.INTERNALUSER
import com.inventoryapp.rcapp.util.FireStoreCollection.OFFERINGFORAGENT
import com.inventoryapp.rcapp.util.FireStoreCollection.SALESORDER
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

    private val source = Source.DEFAULT
    override val currentUser: FirebaseUser?
        get() = firebaseAuth.currentUser

    override suspend fun login(
        email: String,
        password: String,
    ): Resource<FirebaseUser> {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            val userId = result.user?.uid!!
            val internalUserDoc = database.collection("InternalUser").document(userId).get().await()
            val agentUserDoc = database.collection("AgentUser").document(userId).get().await()
            if (agentUserDoc.exists()) {
                // Pengguna adalah internal user
                val agentUser = agentUserDoc.toObject(AgentUser::class.java)
                if (agentUser!!.verificationStatus==VerifAccountStatus.PENDING){
                    return Resource.Failure(Exception("Akun belum disetujui"))
                } else Resource.Success(result.user!!)
            } else if (internalUserDoc.exists()) {
                // Pengguna adalah agent user
                val internalUser = internalUserDoc.toObject(InternalUser::class.java)
                if (internalUser!!.userRole == null){
                    return Resource.Failure(Exception("Akun tidak memiliki role"))
                } else Resource.Success(result.user!!)
                if (internalUser.userRole.toString() == ""){
                    return Resource.Failure(Exception("Akun tidak memiliki role"))
                } else Resource.Success(result.user!!)
            } else Resource.Success(result.user!!)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }

    }

//    override suspend fun login(email: String, password: String): Resource<FirebaseUser> {
//        return try {
//            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
//            val documentSnapshot = database
//                .collection(INTERNALUSER)
//                .document(result.user?.uid!!)
//                .get(source).await()
////                .whereEqualTo("email", result.user?.email) // Use userId for comparison
//            //                .whereEqualTo("verificationStatus", "APPROVED")
//            val agentUser = documentSnapshot.toObject(InternalUser::class.java)
//
//            if (agentUser?.idUser?.isEmpty()!!) {
//                // Handle case where user data or approved status not found
//                return Resource.Failure(Exception("Akun tidak ditemukan"))
//            } else {
//                appPreferences.edit().putString(SharedPrefConstants.USER_NAME, agentUser.name).apply()
//                appPreferences.edit().putString(SharedPrefConstants.USER_ID, agentUser.idUser).apply()
//                appPreferences.edit().putString(SharedPrefConstants.USER_EMAIL, agentUser.email).apply()
//                appPreferences.edit().putString(SharedPrefConstants.USER_STATUS, agentUser.userRole.toString()).apply()
//                Resource.Success(result.user!!)
//            }
////            if (agentUser.verificationStatus.toString() == "PENDING"){
////                return Resource.Failure(Exception("Akun belum disetujui"))
////            }
////            if (agentUser.verificationStatus.toString() == "APPROVED") {
//////                val agentUser = documentSnapshot.toObject(AgentUser::class.java)
////                appPreferences.edit().putString(SharedPrefConstants.USER_NAME, agentUser.name).apply()
////                appPreferences.edit().putString(SharedPrefConstants.USER_ID, agentUser.idAgent).apply()
////                appPreferences.edit().putString(SharedPrefConstants.USER_EMAIL, agentUser.email).apply()
////                appPreferences.edit().putString(SharedPrefConstants.USER_STATUS, agentUser.verificationStatus.toString()).apply()
////                Resource.Success(result.user!!)
////            }
////            else Resource.Failure(Exception("Terjadi masalah dengan server"))
//
////            else {
////                for (doc in documentSnapshot){
////                    val agentUser = doc.toObject(AgentUser::class.java)
////                    appPreferences.edit().putString(SharedPrefConstants.USER_NAME, agentUser.name).apply()
////                    appPreferences.edit().putString(SharedPrefConstants.USER_ID, agentUser.idAgent).apply()
////                    appPreferences.edit().putString(SharedPrefConstants.USER_EMAIL, agentUser.email).apply()
////                    appPreferences.edit().putString(SharedPrefConstants.USER_STATUS, agentUser.verificationStatus.toString()).apply()
////                }
////                Resource.Success(result.user!!)
////            }
//        } catch (e: Exception) {
//            e.printStackTrace()
//            Resource.Failure(e)
//        }
////        return try {
////            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
////            val documentSnapshot = FirebaseFirestore.getInstance()
////                .collection(INTERNALUSER)
////                .whereEqualTo("email", email) // Use userId for comparison
////                .get(source).await()
////
////            if (documentSnapshot.isEmpty) {
////                // Handle case where user data or approved status not found
////                return Resource.Failure(Exception("User tidak memiliki role"))
////            } else {
////                for (doc in documentSnapshot){
////                    val user = doc.toObject(InternalUser::class.java)
////                    appPreferences.edit().putString(SharedPrefConstants.USER_NAME_INTERNAL, user.name).apply()
////                    appPreferences.edit().putString(SharedPrefConstants.USER_ID_INTERNAL, user.idUser).apply()
////                    appPreferences.edit().putString(SharedPrefConstants.USER_EMAIL_INTERNAL, user.email).apply()
////                    appPreferences.edit().putString(SharedPrefConstants.USER_ROLE_INTERNAL, user.userRole.toString()).apply()
////                }
////                Resource.Success(result.user!!)
////            }
////        } catch (e: Exception) {
////            e.printStackTrace()
////            Resource.Failure(e)
////        }
//    }

    override suspend fun storeSession(
        id: String,
        result: (InternalUser?) -> Unit
    ): Resource<FirebaseFirestore> {
        return try {
            database.collection(INTERNALUSER).document(id)
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

    override suspend fun updateUserInfo(
        user: InternalUser,
        result: (Resource<String>) -> Unit
    ): Resource<FirebaseFirestore> {
        return try {
            val document = database
                .collection(INTERNALUSER)
                .document(user.idUser!!)
            document.set(user).await()
            Resource.Success(database)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }
    override fun getSession(result: (InternalUser?) -> Unit) {
        val userStr = appPreferences.getString(SharedPrefConstants.USER_SESSION_INTERNAL, null)
        val userRole = appPreferences.getString(SharedPrefConstants.USER_ROLE_INTERNAL, null)
        if (userStr == null && userRole == null) {
            result.invoke(null)
        } else {
            val user = gson.fromJson(userStr, InternalUser::class.java)
            result.invoke(user)
        }
    }

    override suspend fun getAgentProduct(idAgent: String): Resource<List<AgentProduct>> {
        return withContext(Dispatchers.IO) {
            val querySnapshot = database
                .collection(AGENTUSER)
                .document(idAgent)
                .collection(FireStoreCollection.AGENTPRODUCT)
                .get(source)
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
        appPreferences.edit().putString(SharedPrefConstants.USER_SESSION_INTERNAL, null).apply()
        appPreferences.edit().putString(SharedPrefConstants.USER_NAME_INTERNAL, null).apply()
        appPreferences.edit().putString(SharedPrefConstants.USER_ID_INTERNAL, null).apply()
        appPreferences.edit().putString(SharedPrefConstants.USER_ROLE_INTERNAL, null).apply()
    }

    override suspend fun getInternalProducts(): Resource<List<InternalProduct>> {
        return withContext(Dispatchers.IO) {
            val querySnapshot = database.collection(INTERNALPRODUCT).get(source)
            when (val taskResult = FirebaseCoroutines.awaitTask(querySnapshot)) {
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

    override suspend fun getCartData(): Resource<List<ProductsItem>> {
        return try {
            val cartItems = mutableListOf<ProductsItem>()
            val documents = database.collection(FireStoreCollection.CARTDATA)
                .get(source).await()

            for (document in documents) {
                val product = document.toObject(ProductsItem::class.java)
                cartItems.add(product)
            }
            Resource.Success(cartItems)
        } catch (e:Exception){
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override suspend fun getCartDataAgent(): Resource<List<ProductsItem>> {
        return try {
            val cartItems = mutableListOf<ProductsItem>()
            val documents = database.collection(AGENTUSER)
                .document(currentUser?.uid!!)
                .collection(FireStoreCollection.CARTDATAAGENT)
                .get(source).await()

            for (document in documents) {
                val product = document.toObject(ProductsItem::class.java)
                cartItems.add(product)
            }
            Resource.Success(cartItems)
        } catch (e:Exception){
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override suspend fun deletePoAgent(idOffering: String): Resource<Boolean> {
        val offeringPoForAgentRef = database.collection(OFFERINGFORAGENT).document(idOffering)
        return try {
            offeringPoForAgentRef.delete().await()
            Resource.Success(true) // Operasi penghapusan berhasil
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e) // Operasi penghapusan gagal
        }    }

    override suspend fun getUsers(): Resource<List<InternalUser>> {
        return withContext(Dispatchers.IO) {
            val querySnapshot = database.collection("users").get(source)
            when (val taskResult = FirebaseCoroutines.awaitTask(querySnapshot)) {
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
            val source = Source.DEFAULT
            val querySnapshot = database.collection(AGENTUSER).get(source)
            when (val taskResult = FirebaseCoroutines.awaitTask(querySnapshot)){
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

    override suspend fun updateStatusAgent(
        idUser: String,
        status: VerifAccountStatus,
        result: (Resource<String>) -> Unit
    ): Resource<FirebaseFirestore> {
        return try {
            val agentUserRef = database
                .collection(AGENTUSER)
                .document(idUser)

            agentUserRef.update("verificationStatus", status.toString()).await()
            Resource.Success(database)
        } catch (e: Exception){
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override suspend fun updateStatusOrder(
        idSalesOrder: String,
        statusOrder: String,
        result: (Resource<String>) -> Unit
    ): Resource<FirebaseFirestore> {
        return try {
            val salesOrderRef = database
                .collection(SALESORDER)
                .document(idSalesOrder)

            salesOrderRef.update("statusOrder", statusOrder).await()
            Resource.Success(database)
        } catch (e: Exception){
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override suspend fun updateInternalProduct(
        internalProduct: InternalProduct,
        result: (Resource<String>) -> Unit
    ): Resource<FirebaseFirestore> {
        return try {
            val internalProductRef = database
                .collection(INTERNALPRODUCT)
                .document(internalProduct.idProduct!!)

            internalProductRef.update("qtyMin", internalProduct.qtyMin)
            internalProductRef.update("price", internalProduct.price)
            internalProductRef.update("discProduct", internalProduct.discProduct)
            internalProductRef.update("finalPrice", internalProduct.finalPrice)
            internalProductRef.update("desc", internalProduct.desc)

            Resource.Success(database)
        }catch (e:Exception){
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override suspend fun addOfferingForAgent(
        offering: OfferingForAgent,
        result: (Resource<String>) -> Unit
    ): Resource<FirebaseFirestore> {
        return try {
            val firebaseResult = database
                .collection(OFFERINGFORAGENT)
                .document()
            val idOffering = firebaseResult.id // Dapatkan ID yang dihasilkan secara otomatis
            offering.idOffering = idOffering
            firebaseResult.set(offering).await()
            Resource.Success(database)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override suspend fun deleteSalesOrder(idSalesOrder: String): Resource<Boolean> {
        val salesOrderRef = database.collection(SALESORDER).document(idSalesOrder)
        return try {
            salesOrderRef.delete().await()
            Resource.Success(true) // Operasi penghapusan berhasil
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e) // Operasi penghapusan gagal
        }
    }

    override suspend fun getOfferingForAgent(): Resource<List<OfferingForAgent>> {
        return withContext(Dispatchers.IO){
            val querySnapshot = database.collection(OFFERINGFORAGENT).get(source)
            when (val taskResult = FirebaseCoroutines.awaitTask(querySnapshot)){
                is Resource.Success -> {
                    val documents = taskResult.result
                    val users = mutableListOf<OfferingForAgent>()
                    for (document in documents){
                        val user = document.toObject(OfferingForAgent::class.java)
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

    override suspend fun getSalesOrder(): Resource<List<SalesOrder>> {
        return withContext(Dispatchers.IO) {
            val source = Source.DEFAULT
            val querySnapshot = database
                .collection(SALESORDER)
                .orderBy("orderDate", Query.Direction.DESCENDING)
                .get(source)
            when (val taskResult = FirebaseCoroutines.awaitTask(querySnapshot)) {
                is Resource.Success -> {
                    val documents = taskResult.result
                    val users = mutableListOf<SalesOrder>()
                    for (document in documents) {
                        val user = document.toObject(SalesOrder::class.java)
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

    override suspend fun getRole(): UserRole? {
        return withContext(Dispatchers.IO) {
            if (currentUser != null){
                val source = Source.DEFAULT
                val userRef = database.collection(INTERNALUSER).document(currentUser!!.uid).get(source)
                val document = userRef.await()
                val user = document.toObject(InternalUser::class.java)
                user?.userRole
            } else UserRole.HeadOfWarehouse
        }
    }

    override suspend fun addInternalStockTransaction(
        transaction: InternalStockTransaction,
        idProduct: String,
        result: (Resource<String>) -> Unit
    ): Resource<FirebaseFirestore> {
        return try {
            val stockRef = database.collection(INTERNALPRODUCT)
                .document(idProduct)
            val transactionRef = database.collection(FireStoreCollection.INTERNALSTOCKTRANSACTION)
                .document()

            val idTransaction = transactionRef.id // Dapatkan ID yang dihasilkan secara otomatis
            transaction.idTransaction = idTransaction

            database.runTransaction {
                val stockSnapshot = it.get(stockRef)

                val currentStock = stockSnapshot.getLong("qtyProduct") ?: 0

                var updatedStock = currentStock + transaction.qtyProduct!!

                if (updatedStock < 0){
                    updatedStock = 0
                }
                it.update(stockRef, "qtyProduct", updatedStock)
                it.set(transactionRef, transaction)

                null
            }.await()
            Resource.Success(database)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }    }

    override suspend fun fetchCollectionSize(collectionPath: String): Int {
        val collectionRef = database
            .collection(AGENTUSER)
            .document(collectionPath)
            .collection(FireStoreCollection.AGENTPRODUCT)
        return try {
            val querySnapshot = collectionRef.get(source).await()
            querySnapshot.count()
        } catch (e: Exception) {
            // Penanganan error jika terjadi
            e.printStackTrace()
            -1 // Nilai default jika terjadi error
        }
    }

    override suspend fun getInternalStockTransaction(): Resource<List<InternalStockTransaction>> {
        return withContext(Dispatchers.IO) {
            val querySnapshot = database
                .collection(FireStoreCollection.INTERNALSTOCKTRANSACTION)
                .get(source)
            when (val taskResult = FirebaseCoroutines.awaitTask(querySnapshot)) {
                is Resource.Success -> {
                    val documents = taskResult.result
                    val users = mutableListOf<InternalStockTransaction>()
                    for (document in documents) {
                        val transaction = document.toObject(InternalStockTransaction::class.java)
                        users.add(transaction)
                    }
                    Resource.Success(users)
                }

                is Resource.Failure -> {
                    Resource.Failure(taskResult.throwable)
                }

                Resource.Loading -> TODO()
            }
        }    }
}