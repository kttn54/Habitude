package com.example.habitude.activities

import android.content.Intent
import android.graphics.Typeface
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import com.example.habitude.databinding.ActivitySplashBinding

class SplashActivity : BaseActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        makeSplashFullScreen()
        setSplashTextFont()
        moveToNextActivity()
    }

    /**
     * This function shows the Splash activity for 1 second, then moves to the Intro activity
     * Handler(Looper.getMainLooper()) ensures the postDelayed method will run on the main thread,
     * which is where UI related code should be executed
    **/
    private fun moveToNextActivity() {
        val delayMillis = 2500L
        val intent = Intent(this@SplashActivity, IntroActivity::class.java)
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            startActivity(intent)
            finish()
        }, delayMillis)
    }

    /**
     * The below is ensuring the app's content is properly displayed and system UI is hidden
     * so that the app is full screen. It can be shown through swiping if needed.
    **/
    private fun makeSplashFullScreen() {
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

    private fun setSplashTextFont() {
        val typeface: Typeface =
            Typeface.createFromAsset(assets, "carbon bl.ttf")
        binding.tvAppName.typeface = typeface
    }
}