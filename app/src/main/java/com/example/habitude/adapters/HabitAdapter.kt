package com.example.habitude.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.habitude.R
import com.example.habitude.data.Habit
import com.example.habitude.databinding.ItemHabitRowBinding
import com.example.habitude.utils.HabitDiffCallback
import java.util.*
import kotlin.collections.ArrayList

/**
 * This class is the adapter for the Habit RecyclerView.
 * It handles the binding of habit data to the item view and click events.
 */

class HabitAdapter(private val habits: MutableList<Habit>): RecyclerView.Adapter<HabitAdapter.HabitViewHolder>() {

    // dayClickListener is relevant for when the user clicks on a day in the habit
    private var dayClickListener: ((Habit) -> Unit)? = null

    // onItemClick is relevant for when the user clicks on a habit
    var onHabitItemClick: ((Habit) -> Unit)? = null

    private val calendar = Calendar.getInstance()

    // This function uses diffUtil to update the data for the adapter if the habit list size has changed.
    fun updateData(newHabitList: List<Habit>) {
        val diffCallback = HabitDiffCallback(this.habits, newHabitList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        this.habits.clear()
        this.habits.addAll(newHabitList)

        diffResult.dispatchUpdatesTo(this)
    }

    // This class handles user click events for each day.
    // If the day isn't coloured, then colour it. If it is already coloured, then revert it back to its original colour.
    inner class HabitViewHolder(val binding: ItemHabitRowBinding): RecyclerView.ViewHolder(binding.root) {
        val dayViews = listOf(
            binding.tvDayOne,
            binding.tvDayTwo,
            binding.tvDayThree,
            binding.tvDayFour,
            binding.tvDayFive,
            binding.tvDaySix,
            binding.tvDaySeven
        )

        init {
            binding.apply {
                listOf(
                    tvDayOne to 1, tvDayTwo to 2, tvDayThree to 3,
                    tvDayFour to 4, tvDayFive to 5, tvDaySix to 6,
                    tvDaySeven to 7
                ).forEach { (tv, dayIndex) ->
                    tv.setOnClickListener {
                        val habit = habits[adapterPosition]
                        habit.setDayCompletion(dayIndex, !habit.isDayCompleted(dayIndex))
                        onDayClick(habit)
                        setDayBackground(tv, habit.isDayCompleted(dayIndex))
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabitViewHolder {
        return HabitViewHolder(ItemHabitRowBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    // This function calculates the text of the days dependent on the current day, then assigns it a background colour dependent on habit data.
    override fun onBindViewHolder(holder: HabitViewHolder, position: Int) {
        val habit = habits[position]
        holder.binding.tvHabitName.text = habit.name

        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
        holder.dayViews.forEachIndexed { index, textView ->
            val dayIndex = (dayOfWeek - index + 7 - 1) % 7 + 1
            textView.text = getDayOfWeekName(dayIndex)
            setDayBackground(textView, habit.isDayCompleted(index + 1))
        }

        holder.itemView.setOnClickListener {
            onHabitItemClick?.invoke(habit)
        }
    }

    override fun getItemCount(): Int {
        return habits.size
    }

    fun getDayOfWeekName(dayOfWeek: Int): String {
        val day = if(dayOfWeek <= 0) dayOfWeek + 7 else dayOfWeek

        return when (day) {
            Calendar.SUNDAY -> "Su"
            Calendar.MONDAY -> "Mo"
            Calendar.TUESDAY -> "Tu"
            Calendar.WEDNESDAY -> "We"
            Calendar.THURSDAY -> "Th"
            Calendar.FRIDAY -> "Fr"
            Calendar.SATURDAY -> "Sa"
            else -> throw IllegalArgumentException("Invalid day of week: $day")
        }
    }

    private fun setDayBackground(circleView: TextView, isCompleted: Boolean) {
        if (isCompleted) {
            circleView.setBackgroundResource(R.drawable.ic_habit_circle_completed)
        } else {
            circleView.setBackgroundResource(R.drawable.ic_habit_circle)
        }
    }

    fun setOnDayClickListener(listener: (Habit) -> Unit) {
        dayClickListener = listener
    }

    private fun onDayClick(habit: Habit) {
        dayClickListener?.invoke(habit)
    }
}
