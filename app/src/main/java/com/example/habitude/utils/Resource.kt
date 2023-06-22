package com.example.habitude.utils

/**
 *  The 'sealed' modifier restricts the inheritance of a class - it can only be subclassed within the same file where it is declared.
 *  Purpose of using a 'sealed' class is to provide a limited set of possibilities for the subclasses of 'Resource'.
 *  Since the sealed class guarantees that all possible subclasses are defined within the same file, the compiler can perform exhaustive checks in a possible 'when' clause.
 */

// Generic class: <T> can receive any data type
sealed class Resource<T> (
    val data: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T): Resource<T>(data)
    class Error<T>(message: String): Resource<T>(message = message)
    class Loading<T>: Resource<T>()
    class Unspecified<T>: Resource<T>()
}
