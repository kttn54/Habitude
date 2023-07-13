package com.example.habitude.fragments.signinsignup

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.habitude.R
import com.example.habitude.activities.HabitActivity
import com.example.habitude.databinding.FragmentSplashBinding
import com.example.habitude.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * This fragment is shown when the app is launched and provides a brief visual indication to
 * the user that the app is loading.
 */

@AndroidEntryPoint
class SplashFragment: Fragment(R.layout.fragment_splash) {

    private lateinit var binding: FragmentSplashBinding
    private val viewModel by viewModels<LoginViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSplashBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        moveToNextActivity()
    }

    /**
     * This function shows the Splash activity for 2.5 second, then moves to the Main/Intro activity,
     * depending if the user is already logged in or not.
     **/
    private fun moveToNextActivity() {
        val delayMillis = 1500L

        /*
        Handler(Looper.getMainLooper()) ensures the postDelayed method will run on the main thread,
        which is where UI related code should be executed
        */
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            val currentUserId = viewModel.getCurrentUserId()
            if (currentUserId.isNotEmpty()) {
                startActivity(Intent(requireActivity(), HabitActivity::class.java))
            } else {
                findNavController().navigate(R.id.action_splashFragment_to_introFragment2)
            }
        }, delayMillis)
    }
}