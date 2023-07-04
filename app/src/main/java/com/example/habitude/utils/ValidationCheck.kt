package com.example.habitude.utils

import android.util.Patterns

/**
 *  These two functions check the validity of the email and password fields of the Login and Register fragments
 *  and return the appropriate string if not valid.
 */

fun validateEmail(email: String): RegisterValidation {
    if (email.isEmpty())
        return RegisterValidation.Failed("Email cannot be empty")

    if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        return RegisterValidation.Failed("Wrong email format")

    return RegisterValidation.Success
}

fun validatePassword(password: String): RegisterValidation {
    if (password.isEmpty())
        return RegisterValidation.Failed("Password cannot be empty")

    if (password.length < 6)
        return RegisterValidation.Failed("Password should contain at least 6 characters")

    return RegisterValidation.Success
}