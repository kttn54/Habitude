package com.example.habitude.viewmodel

import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habitude.utils.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth
): ViewModel() {
    // Using MutableSharedFlow instead of MutableStateFlow is useful because it maintains a historical stream of
    // login state values. Also, it allows multiple components to observe the login state and
    // receive updates concurrently.
    private val _login = MutableSharedFlow<Resource<FirebaseUser>>()
    val login = _login.asSharedFlow()

    data class SnackbarEvent(val message: String)
    private var _snackbarEvent = MutableLiveData<SnackbarEvent>()
    val snackbarEvent: LiveData<SnackbarEvent> = _snackbarEvent

    fun login(email: String, password: String) {

        viewModelScope.launch { _login.emit(Resource.Loading())}

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
    }

    private fun validateForm(email: String, password: String): Boolean {
        return if (TextUtils.isEmpty(email)) {
            _snackbarEvent.value = SnackbarEvent("Please enter email.")
            false
        } else if (TextUtils.isEmpty(password)) {
            _snackbarEvent.value = SnackbarEvent("Please enter password.")
            false
        } else {
            true
        }
    }
}