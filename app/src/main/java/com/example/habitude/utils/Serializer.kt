package com.example.habitude.utils

import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import com.prolificinteractive.materialcalendarview.CalendarDay

object Serializer {
    private val gson = Gson()

    fun serializeList(calendarDays: MutableList<CalendarDay>): List<Map<String, Any>> {
        return calendarDays.map { date ->
            mapOf(
                "year" to date.year,
                "month" to date.month,
                "day" to date.day
            )
        }
    }

    fun deserializeList(serializedData: List<Map<String, Any>>): MutableList<CalendarDay> {
        return serializedData.map { map ->
            CalendarDay.from(
                (map["year"] as Long).toInt(),
                (map["month"] as Long).toInt(),
                (map["day"] as Long).toInt()
            )
        }.toMutableList()
    }
}