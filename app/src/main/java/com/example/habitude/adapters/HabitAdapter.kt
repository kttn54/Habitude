package com.example.habitude.adapters

import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.habitude.R
import com.example.habitude.data.Habit
import com.example.habitude.databinding.ItemHabitRowBinding
import java.time.LocalDate
import java.util.*
import kotlin.collections.ArrayList

class HabitAdapter(private val habits: ArrayList<Habit>): RecyclerView.Adapter<HabitAdapter.HabitViewHolder>() {

    private var circleClickListener: ((Habit, Int) -> Unit)? = null

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

    inner class HabitViewHolder(val binding: ItemHabitRowBinding): RecyclerView.ViewHolder(binding.root) {
        init {
            binding.apply {
                tvDayOne.setOnClickListener {
                    val habit = habits[adapterPosition]
                    habit.isDayOneComplete = !habit.isDayOneComplete
                    onCircleClick(habit, 1)
                    setCircleBackground(tvDayOne, habit.isDayCompleted(1))
                }
                tvDayTwo.setOnClickListener {
                    val habit = habits[adapterPosition]
                    habit.isDayTwoComplete = !habit.isDayTwoComplete
                    onCircleClick(habit, 2)
                    setCircleBackground(tvDayTwo, habit.isDayCompleted(2))
                }
                tvDayThree.setOnClickListener {
                    val habit = habits[adapterPosition]
                    habit.isDayThreeComplete = !habit.isDayThreeComplete
                    onCircleClick(habit, 3)
                    setCircleBackground(tvDayThree, habit.isDayCompleted(3))
                }
                tvDayFour.setOnClickListener {
                    val habit = habits[adapterPosition]
                    habit.isDayFourComplete = !habit.isDayFourComplete
                    onCircleClick(habit, 4)
                    setCircleBackground(tvDayFour, habit.isDayCompleted(4))
                }
                tvDayFive.setOnClickListener {
                    val habit = habits[adapterPosition]
                    habit.isDayFiveComplete = !habit.isDayFiveComplete
                    onCircleClick(habit, 5)
                    setCircleBackground(tvDayFive, habit.isDayCompleted(5))
                }
                tvDaySix.setOnClickListener {
                    val habit = habits[adapterPosition]
                    habit.isDaySixComplete = !habit.isDaySixComplete
                    onCircleClick(habit, 6)
                    setCircleBackground(tvDaySix, habit.isDayCompleted(6))
                }
                tvDaySeven.setOnClickListener {
                    val habit = habits[adapterPosition]
                    habit.isDaySevenComplete = !habit.isDaySevenComplete
                    onCircleClick(habit, 7)
                    setCircleBackground(tvDaySeven, habit.isDayCompleted(7))
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabitViewHolder {
        return HabitViewHolder(ItemHabitRowBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

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

        setCircleBackground(holder.binding.tvDayOne, habit.isDayCompleted(1))
        setCircleBackground(holder.binding.tvDayTwo, habit.isDayCompleted(2))
        setCircleBackground(holder.binding.tvDayThree, habit.isDayCompleted(3))
        setCircleBackground(holder.binding.tvDayFour, habit.isDayCompleted(4))
        setCircleBackground(holder.binding.tvDayFive, habit.isDayCompleted(5))
        setCircleBackground(holder.binding.tvDaySix, habit.isDayCompleted(6))
        setCircleBackground(holder.binding.tvDaySeven, habit.isDayCompleted(7))

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

    private fun setCircleBackground(circleView: TextView, isCompleted: Boolean) {
        if (isCompleted) {
            circleView.setBackgroundResource(R.drawable.ic_habit_circle_completed)
        } else {
            circleView.setBackgroundResource(R.drawable.ic_habit_circle)
        }
    }

    fun setOnCircleClickListener(listener: (Habit, Int) -> Unit) {
        circleClickListener = listener
    }

    private fun onCircleClick(habit: Habit, dayOfWeek: Int) {
        circleClickListener?.invoke(habit, dayOfWeek)
    }
}
