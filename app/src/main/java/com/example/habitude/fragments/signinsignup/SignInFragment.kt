package com.example.habitude.fragments.signinsignup

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.habitude.R
import com.example.habitude.activities.HabitActivity
import com.example.habitude.databinding.FragmentSignInBinding
import com.example.habitude.dialog.setupBottomSheetDialog
import com.example.habitude.utils.Resource
import com.example.habitude.viewmodel.LoginViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignInFragment : Fragment(R.layout.fragment_sign_in) {
    private lateinit var binding: FragmentSignInBinding
    private val viewModel by viewModels<LoginViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignInBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

        lifecycleScope.launchWhenStarted {
            viewModel.login.collect {
                when (it) {
                    is Resource.Loading -> {
                        binding.btnSignIn.startAnimation()
                    }
                    is Resource.Success -> {
                        binding.btnSignIn.revertAnimation()
                        Intent(requireActivity(), HabitActivity::class.java).also { intent ->
                            // ACTIVITY_CLEAR_TASK clears all the activities on top of the Main activity stack and
                            // ACTIVITY_CLEAR_TOP ensures that if an instance of MainActivity already exists in the current stack,
                            // it will be brought to the front instead of creating a new instance.
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
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

        //TODO: do validation error fields for username and password
    }
}