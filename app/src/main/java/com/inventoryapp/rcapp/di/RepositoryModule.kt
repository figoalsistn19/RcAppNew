package com.inventoryapp.rcapp.di

import android.app.Application
import android.content.SharedPreferences
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import com.inventoryapp.rcapp.data.repository.AuthAgentRepository
import com.inventoryapp.rcapp.data.repository.AuthAgentRepositoryImp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

object RepositoryModule {

//    @Provides
//    @Singleton
//    fun provideNoteRepository(
//        database: FirebaseFirestore,
//        storageReference: StorageReference
//    ): NoteRepository{
//        return NoteRepositoryImp(database,storageReference)
//    }
//
//    @Provides
//    @Singleton
//    fun provideTaskRepository(
//        database: FirebaseDatabase
//    ): TaskRepository{
//        return TaskRepositoryImp(database)
//    }
}