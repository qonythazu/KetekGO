package com.dicoding.ketekgo.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.cardview.widget.CardView
import com.dicoding.ketekgo.databinding.ActivitySurveyBinding
import com.dicoding.ketekgo.dataclass.PreferencesRequest
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class SurveyActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySurveyBinding
    private var culinarySelected = false
    private var recreationSelected = false
    private var shoppingSelected = false
    private var cultureSelected = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySurveyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.culinary.setOnClickListener {
            culinarySelected = !culinarySelected
            updateSelectionState(binding.culinary, culinarySelected)
        }

        binding.recreation.setOnClickListener {
            recreationSelected = !recreationSelected
            updateSelectionState(binding.recreation, recreationSelected)
        }

        binding.shopping.setOnClickListener {
            shoppingSelected = !shoppingSelected
            updateSelectionState(binding.shopping, shoppingSelected)
        }

        binding.culture.setOnClickListener {
            cultureSelected = !cultureSelected
            updateSelectionState(binding.culture, cultureSelected)
        }

        val submit = binding.btnSubmit
        submit.setOnClickListener {
            val currentUser = FirebaseAuth.getInstance().currentUser
            currentUser?.let { user ->
                val preferences = mutableListOf<String>()

                if (culinarySelected) preferences.add("kuliner")
                if (recreationSelected) preferences.add("rekreasi")
                if (shoppingSelected) preferences.add("belanja")
                if (cultureSelected) preferences.add("budaya")

                val preferencesRequest = PreferencesRequest(preferences)

                val db = FirebaseFirestore.getInstance()
                val preferencesCollection = db.collection("Customers").document(user.uid).collection("Preferences")

                preferencesCollection.get().addOnSuccessListener { documents ->
                    if (documents.isEmpty) {
                        preferencesCollection.add(mapOf("preference" to preferences))
                    } else {
                        preferencesCollection.document(documents.documents[0].id)
                            .update("preference", FieldValue.arrayUnion(*preferences.toTypedArray()))
                    }

                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("preferencesRequest", preferencesRequest)
                    startActivity(intent)
                    finish()
                }
            }
        }


        val skip = binding.btnSkip
        skip.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun updateSelectionState(view: CardView, isSelected: Boolean) {
        if (isSelected) {
            view.cardElevation = 36f
        } else {
            view.cardElevation = 1f
        }
    }

}