package com.example.habitude.data

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.Timestamp
import com.prolificinteractive.materialcalendarview.CalendarDay
import java.util.*

/**
 * This class contains the model for the Habit object.
 * It is parcelable as habits will be sent through fragments for editing/deletion.
 */

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
    var selectedDates: MutableList<Timestamp> = mutableListOf()
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte(),
        parcel.createTypedArrayList(Timestamp.CREATOR) ?: mutableListOf()
    ) {
    }

    // Convert CalendarDay to Timestamp
    fun convertToTimestamp(calendarDay: CalendarDay): Timestamp {
        val calendar = Calendar.getInstance()
        calendar.set(calendarDay.year, calendarDay.month - 1, calendarDay.day)
        return Timestamp(calendar.time)
    }

    // Convert Timestamp to CalendarDay
    fun convertToCalendarDay(timestamp: Timestamp): CalendarDay {
        val date = timestamp.toDate()
        val calendar = Calendar.getInstance()
        calendar.time = date
        return CalendarDay.from(calendar)
    }

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

    fun setDayCompletion(day: Int, isCompleted: Boolean) {
        when (day) {
            1 -> isDayOneComplete = isCompleted
            2 -> isDayTwoComplete = isCompleted
            3 -> isDayThreeComplete = isCompleted
            4 -> isDayFourComplete = isCompleted
            5 -> isDayFiveComplete = isCompleted
            6 -> isDaySixComplete = isCompleted
            7 -> isDaySevenComplete = isCompleted
        }
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(habitId)
        parcel.writeString(userId)
        parcel.writeByte(if (isDayOneComplete) 1 else 0)
        parcel.writeByte(if (isDayTwoComplete) 1 else 0)
        parcel.writeByte(if (isDayThreeComplete) 1 else 0)
        parcel.writeByte(if (isDayFourComplete) 1 else 0)
        parcel.writeByte(if (isDayFiveComplete) 1 else 0)
        parcel.writeByte(if (isDaySixComplete) 1 else 0)
        parcel.writeByte(if (isDaySevenComplete) 1 else 0)
        parcel.writeTypedList(selectedDates)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Habit> {
        override fun createFromParcel(parcel: Parcel): Habit {
            return Habit(parcel)
        }

        override fun newArray(size: Int): Array<Habit?> {
            return arrayOfNulls(size)
        }
    }
}
