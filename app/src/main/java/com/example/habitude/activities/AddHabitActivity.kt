package com.example.habitude.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.habitude.data.Habit
import com.example.habitude.databinding.ActivityAddHabitBinding
import com.example.habitude.utils.Resource
import com.example.habitude.viewmodel.HabitViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddHabitActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddHabitBinding
    private val viewModel by viewModels<HabitViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddHabitBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupActionBar()

        binding.btnAddHabit.setOnClickListener {
            val habitName = binding.etHabitName.text.toString().trim()
            val habit = Habit(habitName)
            viewModel.saveHabit(habit)
        }

        binding.etHabitName.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE || (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER)) {
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
                        startActivity(Intent(this@AddHabitActivity, HabitActivity::class.java))
                        Toast.makeText(this@AddHabitActivity, "Habit successfully added", Toast.LENGTH_SHORT).show()
                    }
                    is Resource.Error -> {
                        Toast.makeText(this@AddHabitActivity, "Error: ${it.message}", Toast.LENGTH_LONG).show()
                    }
                    else -> Unit
                }
            }
        }
    }

    private fun setupActionBar() {
        setSupportActionBar(binding.toolbarAddHabitActivity)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        supportActionBar?.title = "Add Habit"

        binding.toolbarAddHabitActivity.setNavigationOnClickListener {
            onBackPressed()
        }
    }
}