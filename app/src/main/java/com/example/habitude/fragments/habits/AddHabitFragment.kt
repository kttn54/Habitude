package com.example.habitude.fragments.habits

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.habitude.R
import com.example.habitude.activities.HabitActivity
import com.example.habitude.data.Habit
import com.example.habitude.databinding.FragmentAddHabitBinding
import com.example.habitude.utils.Resource
import com.example.habitude.viewmodel.HabitViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddHabitFragment : Fragment() {

    private lateinit var binding: FragmentAddHabitBinding
    private val viewModel by viewModels<HabitViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddHabitBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupActionBar()
        binding.btnAddHabit.setOnClickListener {
            val habitName = binding.etHabitName.text.toString().trim()
            val habit = Habit(habitName)
            viewModel.addHabit(habit)
        }

        binding.etHabitName.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE || (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP)) {
                binding.btnAddHabit.performClick() // Programmatically perform the sign-in button click action
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }

        lifecycleScope.launchWhenStarted {
            viewModel.addHabit.collect {
                when (it) {
                    is Resource.Loading -> {
                        binding.btnAddHabit.startAnimation()
                    }
                    is Resource.Success -> {
                        binding.btnAddHabit.revertAnimation()
                        startActivity(Intent(requireActivity(), HabitActivity::class.java))
                        Toast.makeText(requireActivity(), "Habit successfully added", Toast.LENGTH_SHORT).show()
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
        val toolbar = binding.toolbarAddHabitActivity
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.apply {
            title = "Add Habit"
            toolbar.setTitleTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            setDisplayHomeAsUpEnabled(true)
        }

        binding.toolbarAddHabitActivity.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }
    }
}