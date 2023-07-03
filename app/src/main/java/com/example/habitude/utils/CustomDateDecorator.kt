package com.example.habitude.utils

import android.app.Activity
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.habitude.R
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade

class CustomDateDecorator(context: Activity?, private val selectedDates: List<CalendarDay>) : DayViewDecorator {
    private val drawable: Drawable?

    override fun shouldDecorate(day: CalendarDay): Boolean {
        return selectedDates.contains(day)
    }

    override fun decorate(view: DayViewFacade) {
        view.setSelectionDrawable(drawable!!)
    }

    init {
        drawable = ContextCompat.getDrawable(context!!, R.drawable.ic_habit_circle_completed)
    }
}