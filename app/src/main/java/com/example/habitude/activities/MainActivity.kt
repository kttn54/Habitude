package com.example.habitude.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import com.bumptech.glide.Glide
import com.example.habitude.R
import com.example.habitude.databinding.ActivityMainBinding
import com.example.habitude.databinding.NavHeaderMainBinding
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.example.habitude.firebase.FirestoreClass
import com.example.habitude.data.User

/**
 * A class that shows the Introduction Activity, which is displayed after the Splash screen.
 * It presents users with the options of signing in or signing up.
 */

class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var mBinding: ActivityMainBinding
    private lateinit var bindingNav: NavHeaderMainBinding
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        bindingNav = NavHeaderMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        setupActionBar()

        FirestoreClass().signInUser(this)

        mBinding.navView.setNavigationItemSelectedListener(this)
    }

    /**
     * A function adds the navigation icon to the toolbar and sets functionality when it is clicked.
     */
    private fun setupActionBar() {
        setSupportActionBar(mBinding.toolbarMainActivity)

        mBinding.toolbarMainActivity.setNavigationIcon(R.drawable.ic_action_navigation_menu)
        mBinding.toolbarMainActivity.setNavigationOnClickListener {
            toggleDrawer()
        }
    }

    /**
     * A function that opens or closers the drawer, depending on which state it is in.
     */
    private fun toggleDrawer() {
        if (mBinding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            mBinding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            mBinding.drawerLayout.openDrawer(GravityCompat.START)
        }
    }

    /**
     * A function that closers the drawer, or exits the app if the back button is pressed twice.
     */
    override fun onBackPressed() {
        if (mBinding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            mBinding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            doubleBackToExit()
        }
    }

    /**
     * A function that outlines what each navigation item should do.
     */
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
        mBinding.drawerLayout.closeDrawer(GravityCompat.START)

        return true
    }

    /**
     * A function that displays the user information in the navigation pane
     */
    fun displayNavigationUserDetails(user: User) {
        // The below allows access to the first element of navView, which is the Linear Layout
        // which gives access to all other child elements.
        val viewHeader = mBinding.navView.getHeaderView(0)
        val headerBinding = viewHeader.let { NavHeaderMainBinding.bind(it) }

        headerBinding.ivUserImage.let {
            Glide
                .with(this)
                .load(user.image)
                .centerCrop()
                .placeholder(R.drawable.ic_user_place_holder)
                .into(it)
        }

        headerBinding.tvUsername.text = user.name
    }

    /**
     * A function to display icons to the right of the toolbar
     */
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.activity_main_toolbar_menu, menu)
        return true
    }

    /**
     * A function to describe what the toolbar icons should do when they are selected.
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.mainActivityToolbarAdd -> {
                startActivity(Intent(this@MainActivity, AddHabitActivity::class.java))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}