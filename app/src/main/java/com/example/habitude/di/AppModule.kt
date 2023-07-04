package com.example.habitude.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * This class is a Dagger module that provides dependencies related to Firebase services.
 * @Module indicates that this is a class that contains methods for providing dependencies.
 * @InstallIn(SingletonComponent:class) specifies that the dependencies provided should be installed in the 'SingletonComponent'.
 * @Singleton indicates that only one instance of each dependency will be created and shared throughout the app.
 *
 * The app can easily inject these Firebase dependencies into the relevant classes through this class.
 */

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth() = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseFirestoreDatabase() = FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun provideStorage() = FirebaseStorage.getInstance().reference
}