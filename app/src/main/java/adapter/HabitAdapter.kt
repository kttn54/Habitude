package adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.habitude.databinding.ItemHabitRowBinding
import model.Habit

class HabitAdapter(private val habits: ArrayList<Habit>): RecyclerView.Adapter<HabitAdapter.ViewHolder>() {

    class ViewHolder(binding: ItemHabitRowBinding): RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }
}