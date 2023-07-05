package com.example.habitude.fragments.habits

import android.os.Bundle
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
import com.example.habitude.adapters.HabitAdapter
import com.example.habitude.data.Habit
import com.example.habitude.databinding.FragmentHomeBinding
import com.example.habitude.utils.Constants.HABIT_DELETED
import com.example.habitude.utils.Constants.HABIT_OBJECT
import com.example.habitude.utils.Resource
import com.example.habitude.viewmodel.HabitViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

/**
 * This fragment displays a list of habits. It retrieves the habits from the ViewModel and
 * populates them in a RecyclerView using the HabitAdapter. It handles navigation to the
 * AddHabitFragment for adding new habits and to the EditHabitFragment for editing existing habits.
 * Users can also edit a habit by clicking on it.
 */

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

    // Function to display the "add" icon in the toolbar.
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.add_habit -> {
                findNavController().navigate(R.id.action_homeFragment_to_addHabitFragment)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

        // Function to retrieve the deleted habit (if any) when the fragment is resumed.
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
        setupHabitRecyclerView()

        viewModel.getHabits()

        observeGetHabits()

        observeAddHabits()

        habitAdapter.setOnDayClickListener { habit ->
            viewModel.updateHabit(habit)
        }

        habitAdapter.onHabitItemClick = { habit ->
            val bundle = Bundle().apply {
                putParcelable(HABIT_OBJECT, habit)
            }

            findNavController().navigate(R.id.action_homeFragment_to_editHabitFragment, bundle)
        }
    }

    private fun observeGetHabits() {
        lifecycleScope.launchWhenStarted {
            viewModel.habits.collect {
                when (it) {
                    is Resource.Loading -> {
                    }
                    is Resource.Success -> {
                        habitList = it.data ?: ArrayList()
                        habitAdapter.updateData(habitList)
                    }
                    is Resource.Error -> {
                        handleError(it.message ?: "Unknown Error")
                    }
                    else -> Unit
                }
            }
        }
    }

    private fun observeAddHabits() {
        lifecycleScope.launchWhenStarted {
            viewModel.addHabit.collect {
                when (it) {
                    is Resource.Loading -> {
                    }
                    is Resource.Success -> {
                        val addedHabit = it.data
                        addedHabit?.let {
                            habitList.add(it)
                            habitAdapter.updateData(habitList)
                        }
                        viewModel.getHabits()
                    }
                    is Resource.Error -> {
                        handleError(it.message ?: "Unknown Error")
                    }
                    else -> Unit
                }
            }
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

    private fun handleError(message: String) {
        Toast.makeText(requireActivity(), "Error: $message", Toast.LENGTH_LONG).show()
    }
}