package com.example.habitude.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.habitude.R
import com.example.habitude.data.Habit
import com.example.habitude.databinding.ItemHabitRowBinding
import java.util.*
import kotlin.collections.ArrayList

/**
 * This class is the adapter for the Habit RecyclerView.
 * It handles the binding of habit data to the item view and click events.
 */

class HabitAdapter(private val habits: ArrayList<Habit>): RecyclerView.Adapter<HabitAdapter.HabitViewHolder>() {

    private var dayClickListener: ((Habit) -> Unit)? = null

    // onItemClick is relevant for when the user clicks on a habit
    lateinit var onItemClick: ((Habit) -> Unit)

    // This function updates the data for the adapter in case the habit list size has changed.
    fun updateData(newHabitList: ArrayList<Habit>) {
        val previousSize = this.habits.size
        this.habits.clear()
        this.habits.addAll(newHabitList)
        val newSize = this.habits.size

        notifyItemRangeChanged(0, Integer.min(previousSize, newSize))

        if (previousSize < newSize) {
            notifyItemRangeInserted(previousSize, newSize - previousSize)
        } else if (previousSize > newSize) {
            notifyItemRangeRemoved(newSize, previousSize - newSize)
        }
    }

    // This class handles user click events for each day.
    // If the day isn't coloured, then colour it. If it is already coloured, then revert it back to its original colour.
    inner class HabitViewHolder(val binding: ItemHabitRowBinding): RecyclerView.ViewHolder(binding.root) {
        init {
            binding.apply {
                tvDayOne.setOnClickListener {
                    val habit = habits[adapterPosition]
                    habit.isDayOneComplete = !habit.isDayOneComplete
                    onDayClick(habit)
                    setDayBackground(tvDayOne, habit.isDayCompleted(1))
                }
                tvDayTwo.setOnClickListener {
                    val habit = habits[adapterPosition]
                    habit.isDayTwoComplete = !habit.isDayTwoComplete
                    onDayClick(habit)
                    setDayBackground(tvDayTwo, habit.isDayCompleted(2))
                }
                tvDayThree.setOnClickListener {
                    val habit = habits[adapterPosition]
                    habit.isDayThreeComplete = !habit.isDayThreeComplete
                    onDayClick(habit)
                    setDayBackground(tvDayThree, habit.isDayCompleted(3))
                }
                tvDayFour.setOnClickListener {
                    val habit = habits[adapterPosition]
                    habit.isDayFourComplete = !habit.isDayFourComplete
                    onDayClick(habit)
                    setDayBackground(tvDayFour, habit.isDayCompleted(4))
                }
                tvDayFive.setOnClickListener {
                    val habit = habits[adapterPosition]
                    habit.isDayFiveComplete = !habit.isDayFiveComplete
                    onDayClick(habit)
                    setDayBackground(tvDayFive, habit.isDayCompleted(5))
                }
                tvDaySix.setOnClickListener {
                    val habit = habits[adapterPosition]
                    habit.isDaySixComplete = !habit.isDaySixComplete
                    onDayClick(habit)
                    setDayBackground(tvDaySix, habit.isDayCompleted(6))
                }
                tvDaySeven.setOnClickListener {
                    val habit = habits[adapterPosition]
                    habit.isDaySevenComplete = !habit.isDaySevenComplete
                    onDayClick(habit)
                    setDayBackground(tvDaySeven, habit.isDayCompleted(7))
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

        val calendar = Calendar.getInstance()
        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
        holder.binding.tvDayOne.text = getDayOfWeekName(dayOfWeek)
        holder.binding.tvDayTwo.text = getDayOfWeekName(dayOfWeek - 1)
        holder.binding.tvDayThree.text = getDayOfWeekName(dayOfWeek - 2)
        holder.binding.tvDayFour.text = getDayOfWeekName(dayOfWeek - 3)
        holder.binding.tvDayFive.text = getDayOfWeekName(dayOfWeek - 4)
        holder.binding.tvDaySix.text = getDayOfWeekName(dayOfWeek - 5)
        holder.binding.tvDaySeven.text = getDayOfWeekName(dayOfWeek - 6)

        setDayBackground(holder.binding.tvDayOne, habit.isDayCompleted(1))
        setDayBackground(holder.binding.tvDayTwo, habit.isDayCompleted(2))
        setDayBackground(holder.binding.tvDayThree, habit.isDayCompleted(3))
        setDayBackground(holder.binding.tvDayFour, habit.isDayCompleted(4))
        setDayBackground(holder.binding.tvDayFive, habit.isDayCompleted(5))
        setDayBackground(holder.binding.tvDaySix, habit.isDayCompleted(6))
        setDayBackground(holder.binding.tvDaySeven, habit.isDayCompleted(7))

        holder.itemView.setOnClickListener {
            onItemClick.invoke(habit)
        }
    }

    override fun getItemCount(): Int {
        return habits.size
    }

    fun getDayOfWeekName(dayOfWeek: Int): String {
        var day = dayOfWeek
        when (dayOfWeek) {
            0 -> {
                day = 7
            }
            -1 -> {
                day = 6
            }
            -2 -> {
                day = 5
            }
            -3 -> {
                day = 4
            }
            -4 -> {
                day = 3
            }
            -5 -> {
                day = 2
            }
            -6 -> {
                day = 1
            }
        }

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
