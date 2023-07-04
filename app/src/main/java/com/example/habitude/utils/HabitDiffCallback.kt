package com.example.habitude.utils

import androidx.recyclerview.widget.DiffUtil
import com.example.habitude.data.Habit

/**
 * This class compares the two habit lists given and returns a boolean result on whether
 * the two lists are the same or different.
 */

class HabitDiffCallback(
private val oldList: List<Habit>,
private val newList: List<Habit>
): DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].habitId == newList[newItemPosition].habitId
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}