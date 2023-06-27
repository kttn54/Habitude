package com.example.habitude.fragments.habits

import android.content.Intent
import android.graphics.Color.BLACK
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.graphics.BlendMode.Companion.Color
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.habitude.R
import com.example.habitude.activities.IntroductionActivity
import com.example.habitude.data.User
import com.example.habitude.databinding.FragmentProfileBinding
import com.example.habitude.dialog.setupBottomSheetDialog
import com.example.habitude.utils.Resource
import com.example.habitude.viewmodel.ProfileViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.collection.LLRBNode
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class ProfileFragment: Fragment() {
    private lateinit var binding: FragmentProfileBinding
    val viewModel by viewModels<ProfileViewModel>()

    private lateinit var imageActivityResultLauncher: ActivityResultLauncher<Intent>

    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        imageActivityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                imageUri = it.data?.data
                Glide.with(this)
                    .load(imageUri)
                    .into(binding.civUser)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.llLogOut.setOnClickListener {
            viewModel.logout()
            val intent = Intent(requireActivity(), IntroductionActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }

        binding.btnEditProfile.setOnClickListener {
            binding.btnSaveProfile.visibility = View.VISIBLE
            binding.btnEditProfile.visibility = View.GONE
            binding.tvName.visibility = View.GONE
            binding.etName.visibility = View.VISIBLE
            binding.civEdit.visibility = View.VISIBLE
            binding.etName.setText("${binding.tvName.text}")
        }

        // TODO: Update name
        binding.btnSaveProfile.setOnClickListener {
            binding.tvName.visibility = View.VISIBLE
            binding.etName.visibility = View.INVISIBLE
            binding.civEdit.visibility = View.GONE

            val name = binding.etName.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val user = User(name, email)
            viewModel.updateUser(user, imageUri)

            showUserInformation(user)
        }

        binding.civEdit.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            imageActivityResultLauncher.launch(intent)
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

        lifecycleScope.launchWhenStarted {
            viewModel.user.collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        binding.progressbarProfile.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
                        showUserInformation(it.data!!)
                        binding.progressbarProfile.visibility = View.GONE
                    }
                    is Resource.Error -> {
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                        binding.progressbarProfile.visibility = View.GONE
                    }
                    else -> Unit
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.updateInfo.collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        binding.btnSaveProfile.startAnimation()
                    }
                    is Resource.Success -> {
                        binding.btnSaveProfile.revertAnimation()
                        binding.btnSaveProfile.visibility = View.GONE
                        binding.btnEditProfile.visibility = View.VISIBLE
                    }
                    is Resource.Error -> {
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }
                    else -> Unit
                }
            }
        }
    }

    private fun showUserInformation(data: User) {
        binding.apply {
            Glide.with(this@ProfileFragment)
                .load(data.image)
                .error(R.drawable.test_image)
                .into(binding.civUser)

            tvName.text = data.name
            etEmail.setText(data.email)
        }
    }
}