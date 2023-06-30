package com.example.habitude.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.habitude.R
import com.example.habitude.databinding.ActivityHabitBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * A class that displays the user's habits as the Home screen.
 */

@AndroidEntryPoint
class HabitActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHabitBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHabitBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navController = findNavController(R.id.habitHostFragment)
        binding.bottomNavigation.setupWithNavController(navController)
    }

}
