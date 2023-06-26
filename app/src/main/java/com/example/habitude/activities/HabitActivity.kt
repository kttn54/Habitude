package com.example.habitude.activities

import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.habitude.R
import com.example.habitude.databinding.NavHeaderMainBinding
import com.example.habitude.databinding.ActivityHabitBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * A class that displays the user's habits as the Home screen.
 */

@AndroidEntryPoint
class HabitActivity : BaseActivity() {

    private lateinit var binding: ActivityHabitBinding
    private lateinit var bindingNav: NavHeaderMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHabitBinding.inflate(layoutInflater)
        bindingNav = NavHeaderMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navController = findNavController(R.id.habitHostFragment)
        binding.bottomNavigation.setupWithNavController(navController)

        // FirestoreClass().signInUser(this)
    }

}