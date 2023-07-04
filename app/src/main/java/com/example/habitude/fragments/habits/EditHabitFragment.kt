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

        habit = arguments?.getParcelable(HABIT_OBJECT)

        binding.etEditHabitName.setText(habit!!.name)

        binding.btnSaveHabit.setOnClickListener {
            val editedName = binding.etEditHabitName.text.toString()
            updatedHabit = habit!!.copy(name = editedName)
            viewModel.updateHabit(updatedHabit!!)
        }

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
                        Toast.makeText(requireActivity(), "Error: ${it.message}", Toast.LENGTH_LONG).show()
                    }
                    else -> Unit
                }
            }
        }

        binding.btnDeleteHabit.setOnClickListener {
            viewModel.deleteHabit(habit!!)
        }

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
                        Toast.makeText(requireActivity(), "Error: ${it.message}", Toast.LENGTH_LONG).show()
                    }
                    else -> Unit
                }
            }
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

}