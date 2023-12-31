package com.dicoding.ketekgo.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.dicoding.ketekgo.PreferenceManager
import com.dicoding.ketekgo.R
import com.dicoding.ketekgo.databinding.ActivityMainBinding
import com.dicoding.ketekgo.viewmodel.UserViewModel
import com.dicoding.ketekgo.viewmodel.ViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private val viewModel: UserViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        val firebaseUser = auth.currentUser

        if (firebaseUser == null) {
            // Not signed in, launch the Auth activity
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        viewModel.setUserId(firebaseUser.uid)

        val preferenceManager = PreferenceManager(this)

        if (!preferenceManager.isSurveyShown()) {
            val intent = Intent(this, SurveyActivity::class.java)
            startActivity(intent)

            preferenceManager.setSurveyShown()
        }

        val navView: BottomNavigationView = binding.bottomNav
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_home_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        navView.setupWithNavController(navController)
        navView.setupWithNavController(navController)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.bookingDetailFragment -> {
                    hideBottomNavigationBar()
                }
                R.id.destinationGridFragment -> {
                    hideBottomNavigationBar()
                }
                R.id.destinationInputPreferenceFragment -> {
                    hideBottomNavigationBar()
                }
                else -> {
                    showBottomNavigationBar()
                }
            }
        }

        binding.logoutText.setOnClickListener {
            signOut()
        }
    }

    private fun hideBottomNavigationBar() {
        binding.bottomNav.visibility = View.GONE
        binding.toolbar.visibility = View.GONE
    }

    private fun showBottomNavigationBar() {
        binding.bottomNav.visibility = View.VISIBLE
        binding.toolbar.visibility = View.VISIBLE
    }

    private fun signOut() {
        showLogoutConfirmationDialog()
    }

    private fun showLogoutConfirmationDialog() {
        val alertDialogBuilder = AlertDialog.Builder(this)

        alertDialogBuilder.setTitle("Logout Confirmation")
        alertDialogBuilder.setMessage("Are you sure you want to log out?")

        alertDialogBuilder.setPositiveButton("Yes") { _, _ ->
            auth.signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        alertDialogBuilder.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

}