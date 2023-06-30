package com.example.habitude.data

import android.os.Parcel
import android.os.Parcelable

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
        parcel.readByte() != 0.toByte()
    ) {
    }

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
