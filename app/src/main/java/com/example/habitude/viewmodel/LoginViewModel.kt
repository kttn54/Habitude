package com.example.habitude.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habitude.utils.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * This LoginViewModel class is responsible for managing and coordinating the data related to user logins.
 * It provides functions to login and reset password.
 */

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth
): ViewModel() {
    // Using MutableSharedFlow instead of MutableStateFlow is useful because it maintains a historical stream of
    // login state values. Also, it allows multiple components to observe the login state and
    // receive updates concurrently.
    private val _login = MutableSharedFlow<Resource<FirebaseUser>>()
    val login = _login.asSharedFlow()

    private val _validation = MutableSharedFlow<RegisterFieldsState>()
    val loginValidation = _validation.asSharedFlow()

    private val _resetPassword = MutableSharedFlow<Resource<String>>()
    val resetPassword = _resetPassword.asSharedFlow()

    data class SnackbarEvent(val message: String)
    private var _snackbarEvent = MutableLiveData<SnackbarEvent>()
    val snackbarEvent: LiveData<SnackbarEvent> = _snackbarEvent

    fun login(email: String, password: String) = viewModelScope.launch {
        if (checkValidation(email, password)) {
            _login.emit(Resource.Loading())

            if (validateForm(email, password)) {
                firebaseAuth.signInWithEmailAndPassword(
                    email, password
                ).addOnSuccessListener {
                    viewModelScope.launch {
                        it.user?.let {
                            _login.emit(Resource.Success(it))
                        }
                    }
                }.addOnFailureListener {
                    viewModelScope.launch {
                        _login.emit(Resource.Error(it.message.toString()))
                    }
                }
            }
        } else {
            val registerFieldsState = RegisterFieldsState(
                validateEmail(email), validatePassword(password)
            )
            _validation.emit(registerFieldsState)
        }
    }

    private fun validateForm(email: String, password: String): Boolean {
        return if (email.isNullOrEmpty()) {
            _snackbarEvent.value = SnackbarEvent("Please enter email.")
            false
        } else if (password.isNullOrEmpty()) {
            _snackbarEvent.value = SnackbarEvent("Please enter password.")
            false
        } else {
            true
        }
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

    fun getCurrentUserId(): String {
        val currentUser = firebaseAuth.currentUser
        var currentUserId = ""

        if (currentUser != null) {
            currentUserId = currentUser.uid
        }
        return currentUserId
    }

    private fun checkValidation(email: String, password: String): Boolean {
        val emailValidation = validateEmail(email)
        val passwordValidation = validatePassword(password)
        val shouldRegister = emailValidation is RegisterValidation.Success &&
                passwordValidation is RegisterValidation.Success

        return shouldRegister
    }
}