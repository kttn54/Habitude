package com.example.habitude.data

data class Habit (
    val name: String = "",
    var habitId: String = "",
    val userId: String = "",
    var isDayOneComplete: Boolean = false,
    var isDayTwoComplete: Boolean = false,
    var isDayThreeComplete: Boolean = false,
    var isDayFourComplete: Boolean = false,
    var isDayFiveComplete: Boolean = false,
    var isDaySixComplete: Boolean = false,
    var isDaySevenComplete: Boolean = false
) {
    constructor(): this("","", "",false, false, false,
        false, false, false, false,)

    fun isDayCompleted(dayOfWeek: Int): Boolean {
        return when (dayOfWeek) {
            1 -> isDayOneComplete
            2 -> isDayTwoComplete
            3 -> isDayThreeComplete
            4 -> isDayFourComplete
            5 -> isDayFiveComplete
            6 -> isDaySixComplete
            7 -> isDaySevenComplete
            else -> false
        }
    }
}
