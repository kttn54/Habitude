package com.example.habitude.utils

import android.os.Parcel
import androidx.core.content.ContextCompat
import com.example.habitude.R
import com.google.android.material.datepicker.DayViewDecorator

/*
class CustomCellDecorator(private val selectedDates: Set<Long>): DayViewDecorator() {

    override fun shouldDecorate(day: CalendarDay?): Boolean {
        // Check if the day is in the selected dates set
        return day != null && selectedDates.contains(day.date.time)
    }

    override fun decorate(view: DayViewFacade?) {
        // Set the background color of the selected dates
        view?.setBackgroundDrawable(ContextCompat.getDrawable(view.context, R.drawable.ic_habit_circle_completed))
    }

}*/
