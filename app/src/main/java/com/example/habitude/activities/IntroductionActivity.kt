package com.example.habitude.activities

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import com.example.habitude.R
import com.example.habitude.databinding.ActivityIntroductionBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * A class that shows the Introduction Activity, which is displayed after the Splash screen.
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

    /**
     * This function ensures the app's content is properly displayed and system UI is hidden
     * so that the app is full screen. It can be shown through swiping if needed.
     **/
    fun makeSplashFullScreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // Use WindowInsetsController on Android R and above
            // setDecorFitsSystemWindows(false) sets a flag that ensures the space normally reserved for
            // status/navigation bars are not used.
            window.setDecorFitsSystemWindows(false)
            val controller = window.insetsController
            if (controller != null) {
                controller.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
                controller.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        } else {
            // Use the deprecated method for older versions of Android
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
    }
}