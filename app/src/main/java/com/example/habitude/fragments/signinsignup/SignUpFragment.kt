package com.example.habitude.fragments.signinsignup

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.habitude.R
import com.example.habitude.data.User
import com.example.habitude.databinding.FragmentSignUpBinding
import com.example.habitude.utils.RegisterValidation
import com.example.habitude.utils.Resource
import com.example.habitude.viewmodel.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SignUpFragment: Fragment(R.layout.fragment_sign_up) {
    private lateinit var binding: FragmentSignUpBinding
    private val viewModel by viewModels<RegisterViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSignUpBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupActionBar()

        binding.btnSignUp.setOnClickListener { registerUser() }

        observeUserRegister()
        observeRegisterValidation()

    }

    private fun observeRegisterValidation() {
        lifecycleScope.launchWhenStarted {
            viewModel.registerValidation.collect { validation ->
                if (validation.email is RegisterValidation.Failed) {
                    binding.etEmail.apply {
                        requestFocus()
                        error = validation.email.message
                    }
                }

                if (validation.password is RegisterValidation.Failed) {
                    binding.etPassword.apply {
                        requestFocus()
                        error = validation.password.message
                    }
                }
            }
        }
    }

    private fun observeUserRegister() {
        lifecycleScope.launchWhenStarted {
            viewModel.register.collect {
                when (it) {
                    is Resource.Loading -> {
                        binding.btnSignUp.startAnimation()
                    }
                    is Resource.Success -> {
                        binding.btnSignUp.revertAnimation()
                        userRegisteredSuccess()
                    }
                    is Resource.Error -> {
                        handleError(it.message ?: "Unknown Error")
                        binding.btnSignUp.revertAnimation()
                    }
                    else -> Unit
                }
            }
        }
    }

    private fun setupActionBar() {
        (activity as AppCompatActivity).apply {
            setSupportActionBar(binding.toolbarSignUpFragment)
            supportActionBar?.apply {
                setDisplayHomeAsUpEnabled(true)
                setHomeAsUpIndicator(R.drawable.ic_black_color_back_24dp)
            }
        }
        binding.toolbarSignUpFragment.setNavigationOnClickListener { findNavController().popBackStack() }
    }

    private fun registerUser() {
        // Here we get the text from editText and trim the space
        binding.apply {
            val user = User(
                etName.text.toString().trim(),
                etEmail.text.toString().trim()
            )
            val password: String = binding.etPassword.text.toString()
            viewModel.createAccountWithEmailAndPassword(user, password)
        }
    }

    /**
     * A function to be called the user is registered successfully and an entry is made in the Firestore database.
     */
    private fun userRegisteredSuccess() {
        Toast.makeText(
            requireContext(),
            "You have successfully registered.",
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun handleError(message: String) {
        Toast.makeText(requireActivity(), "Error: $message", Toast.LENGTH_LONG).show()
    }
}