package com.example.habitude.fragments.habits

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.habitude.R
import com.example.habitude.data.Habit
import com.example.habitude.databinding.FragmentEditHabitBinding
import com.example.habitude.utils.Constants.HABIT_DELETED
import com.example.habitude.utils.Constants.HABIT_OBJECT
import com.example.habitude.utils.Resource
import com.example.habitude.viewmodel.HabitViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * This fragment allows the user to edit or delete an existing habit.
 * The updated habit is then saved and reflected in the user's habit list.
 */

@AndroidEntryPoint
class EditHabitFragment : Fragment() {

    private lateinit var binding: FragmentEditHabitBinding
    private val viewModel by viewModels<HabitViewModel>()
    private var habit: Habit? = null
    private var updatedHabit: Habit? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditHabitBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupActionBar()

        // Get the Habit passed into this fragment from the Home fragment.
        habit = arguments?.getParcelable(HABIT_OBJECT)

        habit?.let {
            binding.etEditHabitName.setText(it.name)
        }

        binding.btnSaveHabit.setOnClickListener {
            handleSaveHabit()
        }

        observeUpdateHabit()

        binding.btnDeleteHabit.setOnClickListener {
            habit?.let { habit -> viewModel.deleteHabit(habit) }
        }

        observeDeleteHabit()
    }

    // When the habit is successfully deleted, navigate back to the Home fragment and set the deleted habit
    // to the "HABIT_DELETED" key, allowing it to be retrieved if needed.
    private fun observeUpdateHabit() {
        lifecycleScope.launchWhenStarted {
            viewModel.updateHabit.collect {
                when (it) {
                    is Resource.Loading -> {
                        binding.btnSaveHabit.startAnimation()
                    }
                    is Resource.Success -> {
                        binding.btnSaveHabit.revertAnimation()
                        findNavController().navigate(R.id.action_editHabitFragment_to_homeFragment)
                    }
                    is Resource.Error -> {
                        handleError(it.message ?: "Unknown Error")
                    }
                    else -> Unit
                }
            }
        }
    }

    private fun observeDeleteHabit() {
        lifecycleScope.launchWhenStarted {
            viewModel.deleteHabit.collect {
                when (it) {
                    is Resource.Loading -> {
                        binding.btnDeleteHabit.startAnimation()
                    }
                    is Resource.Success -> {
                        binding.btnDeleteHabit.revertAnimation()
                        findNavController().previousBackStackEntry?.savedStateHandle?.set(HABIT_DELETED, habit)
                        findNavController().popBackStack()
                    }
                    is Resource.Error -> {
                        handleError(it.message ?: "Unknown Error")
                    }
                    else -> Unit
                }
            }
        }
    }

    private fun handleSaveHabit() {
        val editedName = binding.etEditHabitName.text.toString().trim()
        if (editedName.isNotBlank()) {
            updatedHabit = habit?.copy(name = editedName)
            updatedHabit?.let { viewModel.updateHabit(it) }
        } else {
            handleError("Habit name cannot be empty")
        }
    }

    private fun setupActionBar() {
        val toolbar = binding.toolbarEditHabitActivity
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.apply {
            title = "Edit Habit"
            toolbar.setTitleTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            setDisplayHomeAsUpEnabled(true)
        }

        binding.toolbarEditHabitActivity.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    private fun handleError(message: String) {
        Toast.makeText(requireActivity(), "Error: $message", Toast.LENGTH_LONG).show()
    }
}