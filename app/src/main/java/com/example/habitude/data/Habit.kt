package com.example.habitude.data

data class Habit (
    val name: String = "",
    val userId: String = ""
) {
    constructor(): this("","")
}
