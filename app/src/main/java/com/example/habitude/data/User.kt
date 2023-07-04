package com.example.habitude.data

/**
 * This class contains the model for the User object
 */

data class User (
    val name: String = "",
    val email: String = "",
    val image: String = "",
) {
    constructor(): this("","","")
}
