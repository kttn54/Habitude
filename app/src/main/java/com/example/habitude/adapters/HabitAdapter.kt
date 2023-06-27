package com.example.habitude.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.habitude.data.Habit
import com.example.habitude.databinding.ItemHabitRowBinding

class HabitAdapter(private val habits: List<Habit>): RecyclerView.Adapter<HabitAdapter.HabitViewHolder>() {

    inner class HabitViewHolder(val binding: ItemHabitRowBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabitViewHolder {
        return HabitViewHolder(ItemHabitRowBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: HabitViewHolder, position: Int) {
        val habit = habits[position]
        holder.binding.tvHabitName.text = habit.name
    }

    override fun getItemCount(): Int {
        return habits.size
    }

}
