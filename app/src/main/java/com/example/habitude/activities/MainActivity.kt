package com.example.habitude.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.GravityCompat
import com.example.habitude.R
import com.example.habitude.databinding.ActivityMainBinding
import com.example.habitude.databinding.AppBarMainBinding
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth

class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityMainBinding
    private var bindingAppBar: AppBarMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bindingAppBar = AppBarMainBinding.inflate(layoutInflater)
        //bindingAppBar = AppBarMainBinding.bind(binding.root)

        setupActionBar()

        binding.navView.setNavigationItemSelectedListener(this)
    }

    private fun setupActionBar() {
        setSupportActionBar(bindingAppBar?.toolbarMainActivity)

        bindingAppBar?.toolbarMainActivity?.setNavigationIcon(R.drawable.ic_action_navigation_menu)

        bindingAppBar?.toolbarMainActivity?.setNavigationOnClickListener {
            toggleDrawer()
        }
    }

    private fun toggleDrawer() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            binding.drawerLayout.isDrawerOpen(GravityCompat.START)
        }
    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            doubleBackToExit()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.navMyProfile -> {
                Toast.makeText(
                    this@MainActivity,
                    "Profile is clicked",
                    Toast.LENGTH_SHORT
                ).show()
            }
            R.id.navSignOut -> {
                FirebaseAuth.getInstance().signOut()

                val intent = Intent(this, IntroActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
            }
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)

        return true
    }
}