package com.example.habitude.fragments

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
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private val TAG = "SignUpFragment"

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
    }

    /**
     * A function for actionBar Setup.
     */
    private fun setupActionBar() {

        val appCompatActivity = requireActivity() as AppCompatActivity

        appCompatActivity.setSupportActionBar(binding.toolbarSignUpFragment)

        val actionBar = (requireActivity() as AppCompatActivity).supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24dp)
        }

        binding.toolbarSignUpFragment.setNavigationOnClickListener { findNavController().popBackStack() }

        lifecycleScope.launchWhenStarted {
            viewModel.register.collect {
                when (it) {
                    is Resource.Loading -> {
                        binding.btnSignUp.startAnimation()
                    }
                    is Resource.Success -> {
                        Log.d("test", it.data.toString())
                        binding.btnSignUp.revertAnimation()
                    }
                    is Resource.Error -> {
                        Log.e(TAG, it.message.toString())
                        binding.btnSignUp.revertAnimation()
                    }
                    else -> Unit
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.validation.collect { validation ->
                if (validation.email is RegisterValidation.Failed) {
                    withContext(Dispatchers.Main) {
                        binding.etEmail.apply {
                            requestFocus()
                            error = validation.email.message
                        }
                    }
                }

                if (validation.password is RegisterValidation.Failed) {
                    withContext(Dispatchers.Main) {
                        binding.etPassword.apply {
                            requestFocus()
                            error = validation.password.message
                        }
                    }
                }
            }
        }
    }

    /**
     * A function to register the user into the Firestore database.
     */
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
     * A function to be called the user is registered successfully and
     * an entry is made in the Firestore database.
     */
    fun userRegisteredSuccess() {
        Toast.makeText(
            requireContext(),
            "You have successfully registered.",
            Toast.LENGTH_SHORT
        ).show()

        // Hide the progress dialog
        // hideProgressDialog()

        /*
        Here the new user registered is automatically signed-in so we just sign-out the user from firebase
        and send him to Intro Screen for Sign-In
        */
        FirebaseAuth.getInstance().signOut()
        // Finish the Sign-Up Screen
        // finish()
    }
}