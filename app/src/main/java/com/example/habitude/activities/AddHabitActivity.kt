package com.example.habitude.activities

import android.os.Bundle
import com.example.habitude.databinding.ActivityAddHabitBinding

class AddHabitActivity : BaseActivity() {

    private lateinit var binding: ActivityAddHabitBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddHabitBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupActionBar()
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