package com.example.habitude.adapters

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.habitude.data.Habit
import com.example.habitude.databinding.ItemHabitRowBinding
import java.time.LocalDate
import java.util.*
import kotlin.collections.ArrayList

class HabitAdapter(private val habits: ArrayList<Habit>): RecyclerView.Adapter<HabitAdapter.HabitViewHolder>() {

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

    inner class HabitViewHolder(val binding: ItemHabitRowBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabitViewHolder {
        return HabitViewHolder(ItemHabitRowBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: HabitViewHolder, position: Int) {
        val habit = habits[position]
        holder.binding.tvHabitName.text = habit.name

        val calendar = Calendar.getInstance()
        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
        holder.binding.tvDayOne.text = getDayOfWeekName(dayOfWeek)
        holder.binding.tvDayTwo.text = getDayOfWeekName(dayOfWeek + 1)
        holder.binding.tvDayThree.text = getDayOfWeekName(dayOfWeek + 2)
        holder.binding.tvDayFour.text = getDayOfWeekName(dayOfWeek + 3)
        holder.binding.tvDayFive.text = getDayOfWeekName(dayOfWeek + 4)
        holder.binding.tvDaySix.text = getDayOfWeekName(dayOfWeek + 5)
        holder.binding.tvDaySeven.text = getDayOfWeekName(dayOfWeek + 6)
    }

    override fun getItemCount(): Int {
        return habits.size
    }

    fun getDayOfWeekName(dayOfWeek: Int): String {
        var day = dayOfWeek
        when (dayOfWeek) {
            8 -> {
                day = 1
            }
            9 -> {
                day = 2
            }
            10 -> {
                day = 3
            }
            11 -> {
                day = 4
            }
            12 -> {
                day = 5
            }
            13 -> {
                day = 6
            }
            14 -> {
                day = 7
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
}
