package com.example.habitude.utils

/**
 * These classes allow the email and password fields to contain a 'Success' or 'Failed' subclass.
 * 'Failed' is a data class as it allows creation of instances with a specific error message,
 * where 'Success' does not need any additional information.
 */

sealed class RegisterValidation {
    object Success: RegisterValidation()
    data class Failed(val message: String): RegisterValidation()
}

data class RegisterFieldsState(
    val email: RegisterValidation,
    val password: RegisterValidation
)
