package com.example.habitude.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.habitude.databinding.ActivityIntroductionBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * A class that displays the Introduction Activity, which is displayed after the Splash screen.
 * It presents users with the options of signing in or signing up.
 */

@AndroidEntryPoint
class IntroductionActivity: AppCompatActivity() {

    private lateinit var binding: ActivityIntroductionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIntroductionBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}