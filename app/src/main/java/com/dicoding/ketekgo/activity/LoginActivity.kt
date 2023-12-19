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

        val email: EditText = binding.edEmailLogin
        val password: EditText = binding.edPasswordLogin
        val btnLogin = binding.btnLogin

        btnLogin.setOnClickListener {
            if (checkField(email) && checkField(password)) {
                fLogin.signInWithEmailAndPassword(email.text.toString(), password.text.toString())
                    .addOnSuccessListener{ ar ->
                        Toast.makeText(this, "Logged In Successfully", Toast.LENGTH_SHORT).show()
                        checkUser(ar.user!!.uid)
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Logged In Failure", Toast.LENGTH_SHORT).show()
                    }
            }
        }

        // Go To Register Page
        binding.btnRegisterHere.setOnClickListener{
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
        }
    }

    private fun checkUser(uid: String) {
        val df: DocumentReference = fStore.collection("Users").document(uid)
        df.get().addOnSuccessListener { dc ->
            Log.e("TAG", "onSuccess : ${dc.data}")
            val role = dc.getString("Role")?.toLong()

            if (role == 1L) {
                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                finish()
            } else if (role == 2L) {
                startActivity(Intent(this@LoginActivity, DriverActivity::class.java))
                finish()
            }
        }.addOnFailureListener { e ->
            Log.e("TAG", "onFailure: $e")
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