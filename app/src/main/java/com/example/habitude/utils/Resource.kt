package com.example.habitude.utils

/**
 * This class outputs the result of an operation/request with several states.
 * Resource<T> is used to capture the different states of an operation where it can receive any data type.
 */

sealed class Resource<T> (
    val data: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T): Resource<T>(data)
    class Error<T>(message: String): Resource<T>(message = message)
    class Loading<T>: Resource<T>()
    class Unspecified<T>: Resource<T>()
}
