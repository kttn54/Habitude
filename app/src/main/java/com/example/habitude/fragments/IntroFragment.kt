package com.example.habitude.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.habitude.R
import com.example.habitude.databinding.FragmentIntroBinding

class IntroFragment: Fragment(R.layout.fragment_intro) {
    private lateinit var binding: FragmentIntroBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentIntroBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSignInIntro.setOnClickListener {
            findNavController().navigate(R.id.action_introFragment_to_signInFragment2)
        }

        binding.btnSignUpIntro.setOnClickListener {
            findNavController().navigate(R.id.action_introFragment_to_signUpFragment)
        }
    }
}