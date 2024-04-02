package com.inventoryapp.rcapp.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.inventoryapp.rcapp.data.repository.AgentRepository
import com.inventoryapp.rcapp.data.repository.AgentRepositoryImp
import com.inventoryapp.rcapp.data.repository.InternalRepository
import com.inventoryapp.rcapp.data.repository.InternalRepositoryImp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object FirebaseModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun provideAuthAgentRepository(impl:AgentRepositoryImp):AgentRepository = impl


//    @Provides
//    fun provideInternalRepository(impl: InternalRepositoryImp):InternalRepositoryImp = impl

    @Provides
    @Singleton
    fun provideAuthInternalRepository(impl:InternalRepositoryImp):InternalRepository = impl

//    @Provides
//    fun provideAuthRepository(impl:AuthRepositoryImp):AuthRepository = impl

}