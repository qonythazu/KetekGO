package com.dicoding.ketekgo.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Toast
import com.dicoding.ketekgo.R
import com.dicoding.ketekgo.checkField
import com.dicoding.ketekgo.databinding.ActivityRegisterBinding
import com.dicoding.ketekgo.isLoading
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : AppCompatActivity() {
    private lateinit var fReg: FirebaseAuth
    private lateinit var fStore: FirebaseFirestore
    private lateinit var binding: ActivityRegisterBinding
    private var selectedRole: String = "1"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initiate Firebase Auth and Store
        fReg = FirebaseAuth.getInstance()
        fStore = FirebaseFirestore.getInstance()

        val fullName: EditText = binding.edFullNameRegister
        val email: EditText = binding.edEmailRegister
        val password: EditText = binding.edPasswordRegister
        val radioGroup: RadioGroup = binding.radioGroup
        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.register_as_customer -> {
                    selectedRole = "1"
                }
                R.id.register_as_driver -> {
                    selectedRole = "2"
                }
            }
        }

        val btnRegister = binding.btnRegister
        val loadingProgressBar = binding.progressRegister
        btnRegister.setOnClickListener{
            isLoading(true, loadingProgressBar)
            // Registration Process
            if (checkField(fullName) && checkField(email) && checkField(password)) {
                fReg.createUserWithEmailAndPassword(email.text.toString(), password.text.toString())
                    .addOnSuccessListener {
                        val user: FirebaseUser? = fReg.currentUser
                        Toast.makeText(this, "Account Created", Toast.LENGTH_SHORT).show()
                        val df: DocumentReference
                        when (selectedRole) {
                            "1" -> {
                                df = fStore.collection("Customers").document(user?.uid!!)
                                val userInfo: MutableMap<String, Any> = HashMap()
                                userInfo["FullName"] = fullName.text.toString()
                                userInfo["Email"] = email.text.toString()
                                userInfo["Password"] = password.text.toString()
                                userInfo["Role"] = selectedRole
                                df.set(userInfo)
                                isLoading(false, loadingProgressBar)
                                startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                                finish()
                            }
                            "2" -> {
                                df = fStore.collection("Drivers").document(user?.uid!!)
                                val userInfo: MutableMap<String, Any> = HashMap()
                                userInfo["FullName"] = fullName.text.toString()
                                userInfo["Email"] = email.text.toString()
                                userInfo["Password"] = password.text.toString()
                                userInfo["Role"] = selectedRole
                                df.set(userInfo)
                                isLoading(false, loadingProgressBar)
                                startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                                finish()
                            }
                            else -> {
                                Toast.makeText(this, "Pilih Register Sebagai Apa Dulu", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                    .addOnFailureListener { e ->
                        isLoading(false, loadingProgressBar)
                        Toast.makeText(this, "Error creating account: $e", Toast.LENGTH_SHORT).show()
                    }
            }
        }

        // Go To Login Page
        binding.btnLoginHere.setOnClickListener {
            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
        }
    }
}