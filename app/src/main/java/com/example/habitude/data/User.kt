package com.example.habitude.data

import android.os.Parcel
import android.os.Parcelable

/**
 * Parcelable is a serialization mechanism to pass complex data from one activity to another.
 * Parcelable is used to send a whole object of a model class to another page.
 **/

data class User (
    val name: String = "",
    val email: String = "",
    val image: String = "",
) {
    constructor(): this("","","")
}
