package com.dicoding.ketekgo.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.dicoding.ketekgo.databinding.FragmentProfileCostumerBinding
import com.dicoding.ketekgo.viewmodel.UserViewModel
import com.dicoding.ketekgo.viewmodel.ViewModelFactory
import com.google.firebase.firestore.FirebaseFirestore

class ProfileCostumerFragment : Fragment() {

    private val viewModel: UserViewModel by activityViewModels {
        ViewModelFactory.getInstance(requireActivity())
    }
    private var _binding : FragmentProfileCostumerBinding? = null
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileCostumerBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupProfile()
    }

    private fun setupProfile(){
        viewModel.getUserId().observe(viewLifecycleOwner) {userId ->
            val db = FirebaseFirestore.getInstance()
            val userRef = db.collection("Customers").document(userId)

            userRef.get().addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val fullName = documentSnapshot.getString("FullName")
                    val email = documentSnapshot.getString("Email")

                    Log.e("Profile Name", "$fullName")
                    Log.e("Profile Email", "$email")
                    binding?.tvUsername?.text = fullName
                    binding?.tvEmail?.text = email

                }
            }.addOnFailureListener { exception ->
                Log.e("Profile Error", "$exception")
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}