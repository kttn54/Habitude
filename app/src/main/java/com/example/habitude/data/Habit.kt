package com.example.habitude.data

import android.os.Parcel
import android.os.Parcelable
import com.prolificinteractive.materialcalendarview.CalendarDay
import kotlinx.android.parcel.Parcelize

/**
 * This class contains the model for the Habit object.
 * It is parcelable as habits will be sent through fragments for editing/deletion.
 */

@Parcelize
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
    var isDaySevenComplete: Boolean = false,
) : Parcelable {
    fun isDayCompleted(dayOfWeek: Int): Boolean {
        return when (dayOfWeek) {
            1 -> isDayOneComplete
            2 -> isDayTwoComplete
            3 -> isDayThreeComplete
            4 -> isDayFourComplete
            5 -> isDayFiveComplete
            6 -> isDaySixComplete
            7 -> isDaySevenComplete
            else -> throw IllegalArgumentException("Invalid day index: $dayOfWeek")
        }
    }

    fun setDayCompletion(dayIndex: Int, isCompleted: Boolean) {
        when (dayIndex) {
            1 -> isDayOneComplete = isCompleted
            2 -> isDayTwoComplete = isCompleted
            3 -> isDayThreeComplete = isCompleted
            4 -> isDayFourComplete = isCompleted
            5 -> isDayFiveComplete = isCompleted
            6 -> isDaySixComplete = isCompleted
            7 -> isDaySevenComplete = isCompleted
            else -> throw IllegalArgumentException("Invalid day index: $dayIndex")
        }
    }
}
