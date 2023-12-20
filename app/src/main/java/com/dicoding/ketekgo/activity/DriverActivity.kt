package com.dicoding.ketekgo.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.dicoding.ketekgo.R
import com.dicoding.ketekgo.databinding.ActivityDriverBinding
import com.dicoding.ketekgo.viewmodel.UserViewModel
import com.dicoding.ketekgo.viewmodel.ViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class DriverActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDriverBinding
    private lateinit var auth: FirebaseAuth
    private val viewModel: UserViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDriverBinding.inflate(layoutInflater)
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

        val navView: BottomNavigationView = binding.bottomDriverNav
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_order_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        navView.setupWithNavController(navController)
        Log.e("DriverActivity", "This is Driver Activity")

        binding.logoutText.setOnClickListener {
            signOut()
        }
    }

    private fun signOut() {
        auth.signOut()
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}