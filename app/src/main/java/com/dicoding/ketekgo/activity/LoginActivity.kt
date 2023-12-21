package com.dicoding.ketekgo.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import com.dicoding.ketekgo.R
import com.dicoding.ketekgo.checkField
import com.dicoding.ketekgo.databinding.ActivityLoginBinding
import com.dicoding.ketekgo.isLoading
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var fLogin: FirebaseAuth
    private lateinit var fStore: FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase Auth
        fLogin = FirebaseAuth.getInstance()
        fStore = FirebaseFirestore.getInstance()

        val btnLogin = binding.btnLogin
        val loadingProgressBar = binding.progressLogin

        btnLogin.setOnClickListener {
            isLoading(true, loadingProgressBar)
            val email: EditText = binding.edEmailLogin
            val password: EditText = binding.edPasswordLogin
            if (checkField(email) && checkField(password)) {
                fLogin.signInWithEmailAndPassword(email.text.toString(), password.text.toString())
                    .addOnSuccessListener { ar ->
                        isLoading(false, loadingProgressBar)
                        Toast.makeText(this, "Logged In Successfully", Toast.LENGTH_SHORT).show()
                        checkUser(ar.user!!.uid)
                    }
                    .addOnFailureListener {
                        isLoading(false, loadingProgressBar)
                        Toast.makeText(this, "Logged In Failure", Toast.LENGTH_SHORT).show()
                    }
            }
        }

        // Go To Register Page
        binding.btnRegisterHere.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
        }
    }

    private fun checkUser(uid: String) {
        Log.e("UID", uid)
        val driverCollection = fStore.collection("Drivers").document(uid)
        val customerCollection = fStore.collection("Customers").document(uid)

        driverCollection.get().addOnSuccessListener { driverDoc ->
            if (driverDoc.exists()) {
                Log.e("Driver", "Login")
                startActivity(Intent(this@LoginActivity, DriverActivity::class.java))
                finish()
            } else {
                customerCollection.get().addOnSuccessListener { customerDoc ->
                    if (customerDoc.exists()) {
                        Log.e("Customer", "Login")
                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                        finish()
                    } else {
                        Log.e("TAG", "User not found in Driver or Customer collection")
                    }
                }.addOnFailureListener { e ->
                    Log.e("TAG", "Error checking in Customer collection: $e")
                }
            }
        }.addOnFailureListener { e ->
            Log.e("TAG", "Error checking in Driver collection: $e")
        }
    }

    private fun updateUI(currentUser: FirebaseUser?) {
        if (currentUser != null){
            checkUser(currentUser.uid)
        }
    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = fLogin.currentUser
        updateUI(currentUser)
    }
}