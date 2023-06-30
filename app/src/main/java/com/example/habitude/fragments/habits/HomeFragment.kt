package com.example.habitude.fragments.habits

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.habitude.R
import com.example.habitude.activities.HabitActivity
import com.example.habitude.adapters.HabitAdapter
import com.example.habitude.data.Habit
import com.example.habitude.databinding.FragmentHomeBinding
import com.example.habitude.utils.Constants.EDITED_HABIT_OBJECT
import com.example.habitude.utils.Constants.HABIT_DELETED
import com.example.habitude.utils.Constants.HABIT_OBJECT
import com.example.habitude.utils.Resource
import com.example.habitude.viewmodel.HabitViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class HomeFragment: Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var habitAdapter: HabitAdapter
    private var habitList: ArrayList<Habit> = ArrayList()
    private val viewModel by viewModels<HabitViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        habitAdapter = HabitAdapter(habitList)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)

        binding = FragmentHomeBinding.inflate(inflater)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.add_habit -> {
                findNavController().navigate(R.id.action_homeFragment_to_addHabitFragment)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()

        val deletedHabit = findNavController().currentBackStackEntry?.savedStateHandle?.remove<Habit>(HABIT_DELETED)
        if (deletedHabit != null) {
            showSnackbar("Habit deleted", deletedHabit)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupActionBar()

        viewModel.getHabits()

        lifecycleScope.launchWhenStarted {
            viewModel.habits.collect {
                when (it) {
                    is Resource.Loading -> {
                    }
                    is Resource.Success -> {
                        habitList = it.data!!
                        habitAdapter.updateData(habitList)
                        setupHabitRecyclerView()
                    }
                    is Resource.Error -> {
                        Toast.makeText(requireActivity(), "Error: ${it.message}", Toast.LENGTH_LONG).show()
                    }
                    else -> Unit
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.addHabit.collect {
                when (it) {
                    is Resource.Loading -> {
                    }
                    is Resource.Success -> {
                        viewModel.getHabits()
                    }
                    is Resource.Error -> {
                        Toast.makeText(requireActivity(), "Error: ${it.message}", Toast.LENGTH_LONG).show()
                    }
                    else -> Unit
                }
            }
        }

        habitAdapter.setOnDayClickListener { habit ->
            viewModel.updateHabitDay(habit)
        }

        habitAdapter.onItemClick = { habit ->
            val bundle = Bundle().apply {
                putParcelable(HABIT_OBJECT, habit)
            }

            findNavController().navigate(R.id.action_homeFragment_to_editHabitFragment, bundle)
        }
    }

    private fun showSnackbar(message: String, habit: Habit) {
        Snackbar.make(requireView(), message, Snackbar.LENGTH_LONG)
            .setAction("Undo") {
                viewModel.addHabit(habit)
            }.show()
    }

    private fun setupActionBar() {
        val toolbar = binding.toolbar
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.apply {
            title = "My Habits"
            toolbar.setTitleTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            setDisplayHomeAsUpEnabled(false)
        }
    }

    private fun setupHabitRecyclerView() {
        binding.rvHabits.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            adapter = habitAdapter
        }
    }
}