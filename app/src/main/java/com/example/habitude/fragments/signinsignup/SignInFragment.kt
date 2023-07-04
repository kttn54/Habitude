package com.example.habitude.fragments.signinsignup

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.KeyEvent.KEYCODE_ENTER
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.habitude.R
import com.example.habitude.activities.HabitActivity
import com.example.habitude.databinding.FragmentSignInBinding
import com.example.habitude.dialog.setupBottomSheetDialog
import com.example.habitude.utils.RegisterValidation
import com.example.habitude.utils.Resource
import com.example.habitude.viewmodel.LoginViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * This fragment represents the sign-in screen of the app, where users can authenticate and access their account.
 */

@AndroidEntryPoint
class SignInFragment : Fragment(R.layout.fragment_sign_in) {
    private lateinit var binding: FragmentSignInBinding
    private val viewModel by viewModels<LoginViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignInBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.etEmailSignIn.requestFocus()

        // This will click the sign in button when the "enter" button is pressed
        binding.etPasswordSignIn.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE || (event != null && event.keyCode == KEYCODE_ENTER)) {
                binding.btnSignIn.performClick() // Programmatically perform the sign-in button click action
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }

        // A Snackbar will show if there is a change in value in the snackbar variable in the LoginViewModel.
        viewModel.snackbarEvent.observe(viewLifecycleOwner) { snackbarEvent ->
            snackbarEvent?.let {
                Snackbar.make(requireView(), snackbarEvent.message, Snackbar.LENGTH_LONG).show()
                binding.btnSignIn.revertAnimation()
            }
        }

        binding.apply {
            btnSignIn.setOnClickListener {
                val email = etEmailSignIn.text.toString().trim()
                val password = etPasswordSignIn.text.toString()
                viewModel.login(email, password)
            }
        }

        // This will show an error if the email or password fields are empty and/or incorrect.
        lifecycleScope.launchWhenStarted {
            viewModel.loginValidation.collect { validation ->
                if (validation.email is RegisterValidation.Failed) {
                    withContext(Dispatchers.Main) {
                        binding.etEmailSignIn.apply {
                            requestFocus()
                            error = validation.email.message
                        }
                    }
                }

                if (validation.password is RegisterValidation.Failed) {
                    withContext(Dispatchers.Main) {
                        binding.etPasswordSignIn.apply {
                            requestFocus()
                            error = validation.password.message
                        }
                    }
                }
            }
        }

        // When the user logins successfully, the intent clears all activities on top of the
        // Main activity stack and brings an existing instance of 'MainActivity' to the front if it exists
        lifecycleScope.launchWhenStarted {
            viewModel.login.collect {
                when (it) {
                    is Resource.Loading -> {
                        binding.btnSignIn.startAnimation()
                    }
                    is Resource.Success -> {
                        binding.btnSignIn.revertAnimation()
                        Intent(requireActivity(), HabitActivity::class.java).also { intent ->
                            startActivity(intent)
                        }
                    }
                    is Resource.Error -> {
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                        binding.btnSignIn.revertAnimation()
                    }
                    else -> Unit
                }
            }
        }

        binding.tvUpdatePassword.setOnClickListener {
            setupBottomSheetDialog { email ->
                viewModel.resetPassword(email)
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.resetPassword.collect {
                when (it) {
                    is Resource.Loading -> {
                    }
                    is Resource.Success -> {
                        Snackbar.make(requireView(), "Reset link was sent to your email", Snackbar.LENGTH_LONG).show()
                    }
                    is Resource.Error -> {
                        Snackbar.make(requireView(), "Error: ${it.message}", Snackbar.LENGTH_LONG).show()
                    }
                    else -> Unit
                }
            }
        }
    }
}