package com.example.habitude.viewmodel

import android.app.Application
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.provider.MediaStore.Images.Media
import android.provider.MediaStore.Images.Media.getBitmap
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habitude.data.User
import com.example.habitude.utils.Constants.USER_COLLECTION
import com.example.habitude.utils.RegisterValidation
import com.example.habitude.utils.Resource
import com.example.habitude.utils.validateEmail
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.StorageReference
import dagger.hilt.android.internal.Contexts.getApplication
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream
import java.util.*
import javax.inject.Inject
import com.example.habitude.HabitudeApplication as HabitudeApplication

/**
 * This ProfileViewModel class is responsible for managing and coordinating the data related to the user profile.
 * It provides functions to retrieve, update and logout the user.
 */

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth,
    private val firebaseStorage: StorageReference,
    app: Application
): AndroidViewModel(app) {

    private var _user = MutableStateFlow<Resource<User>>(Resource.Unspecified())
    var user = _user.asStateFlow()

    private val _resetPassword = MutableSharedFlow<Resource<String>>()
    val resetPassword = _resetPassword.asSharedFlow()

    private var _updateInfo = MutableStateFlow<Resource<User>>(Resource.Unspecified())
    var updateInfo = _updateInfo.asStateFlow()

    fun getUser() {
        viewModelScope.launch { _user.emit((Resource.Loading())) }

        firestore.collection(USER_COLLECTION)
            .document(firebaseAuth.uid!!).get()
            .addOnSuccessListener {
                val user = it.toObject(User::class.java)
                user?.let {
                    viewModelScope.launch {
                        _user.emit(Resource.Success(it))
                    }
                }
            }.addOnFailureListener {
                viewModelScope.launch {
                    _user.emit(Resource.Error(it.message.toString()))
                }
            }
    }

    fun updateUser(user: User, imageUri: Uri?) {
        val areInputsValid = validateEmail(user.email) is RegisterValidation.Success
                && user.name.trim().isNotEmpty()

        if (!areInputsValid) {
            viewModelScope.launch {
                _user.emit((Resource.Error("Check your inputs")))
            }
        }

        viewModelScope.launch { _updateInfo.emit(Resource.Loading()) }

        if (imageUri == null) {
            saveUserInformation(user, true)
        } else {
            saveUserInformationWithNewImage(user, imageUri)
        }

    }

    // To upload any image on the Firebase storage, we want to get the byte array of the image.
    // Need to get the bitmap of the image
    private fun saveUserInformationWithNewImage(user: User, imageUri: Uri) {
        viewModelScope.launch {
            try {
                val imageBitmap = Media.getBitmap(getApplication<HabitudeApplication>().contentResolver, imageUri)
                // Compress the image
                val byteArrayOutputStream = ByteArrayOutputStream()
                imageBitmap.compress(Bitmap.CompressFormat.JPEG, 96, byteArrayOutputStream)
                val imageByteArray = byteArrayOutputStream.toByteArray()
                val fileName = "${System.currentTimeMillis()}_${imageUri.lastPathSegment}"
                val imageDirectory = firebaseStorage.child("profileImages/${firebaseAuth.uid}/$fileName")
                val result = imageDirectory.putBytes(imageByteArray).await()
                val imageUrl = result.storage.downloadUrl.await().toString()
                saveUserInformation(user.copy(image = imageUrl), false)
                Log.e("test", "imageurl is $imageUrl")
            } catch (e: Exception) {
                viewModelScope.launch {
                    _updateInfo.emit(Resource.Error(e.message.toString()))
                }
            }
        }
    }

    private fun saveUserInformation(user: User, shouldRetrieveOldImage: Boolean) {
        viewModelScope.launch { _updateInfo.emit(Resource.Loading()) }

        firestore.runTransaction { transaction ->
            val documentRef = firestore.collection("user").document(firebaseAuth.uid!!)
            if (shouldRetrieveOldImage) {
                val currentUser = transaction.get(documentRef).toObject(User::class.java)
                val newUser = user.copy(image = currentUser?.image ?: "")
                transaction.set(documentRef, newUser)
            } else {
                transaction.set(documentRef, user)
            }
        }.addOnSuccessListener {
            viewModelScope.launch {
                _updateInfo.emit(Resource.Success(user))
            }
        }.addOnFailureListener {
            viewModelScope.launch {
                _updateInfo.emit(Resource.Error(it.message.toString()))
            }
        }
    }

    fun logout() {
        firebaseAuth.signOut()
    }

    fun resetPassword(email: String) {
        viewModelScope.launch { _resetPassword.emit(Resource.Loading()) }

        firebaseAuth.sendPasswordResetEmail(email)
                .addOnSuccessListener {
                    viewModelScope.launch {
                        _resetPassword.emit(Resource.Success(email))
                    }
                }.addOnFailureListener {
                    viewModelScope.launch {
                        _resetPassword.emit(Resource.Error(it.message.toString()))
                    }
                }
    }
}