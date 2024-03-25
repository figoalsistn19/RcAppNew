package com.inventoryapp.rcapp.di

import android.content.Context
import android.content.SharedPreferences
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.gson.Gson
import com.inventoryapp.rcapp.data.repository.AuthAgentRepository
import com.inventoryapp.rcapp.data.repository.AuthAgentRepositoryImp
import com.inventoryapp.rcapp.data.repository.AuthInternalRepository
import com.inventoryapp.rcapp.data.repository.AuthInternalRepositoryImp
import com.inventoryapp.rcapp.util.FirebaseStorageConstants
import com.inventoryapp.rcapp.util.SharedPrefConstants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object FirebaseModule {

    @Provides
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    fun provideFirebaseFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    @Provides
    fun provideAuthAgentRepository(impl:AuthAgentRepositoryImp):AuthAgentRepository = impl

    @Provides
    fun provideAuthInternalRepository(impl:AuthInternalRepositoryImp):AuthInternalRepository = impl

//    @Provides
//    fun provideAuthRepository(impl:AuthRepositoryImp):AuthRepository = impl

}