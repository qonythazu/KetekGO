package com.dicoding.ketekgo.fragment.driver

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.ketekgo.adapter.ListKetekDriverAdapter
import com.dicoding.ketekgo.databinding.FragmentProfileDriverBinding
import com.dicoding.ketekgo.dataclass.KetekDriver
import com.dicoding.ketekgo.viewmodel.UserViewModel
import com.dicoding.ketekgo.viewmodel.ViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProfileDriverFragment : Fragment() {
    private lateinit var fStore: FirebaseFirestore
    private lateinit var rvKetekDriver: RecyclerView
    private var list = ArrayList<KetekDriver>()
    private val viewModel: UserViewModel by activityViewModels {
        ViewModelFactory.getInstance(requireActivity())
    }
    private var _binding : FragmentProfileDriverBinding? = null
    private val binding get() = _binding
    private val ketekDriverLiveData = MutableLiveData<ArrayList<KetekDriver>>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileDriverBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupProfile()
        fStore = FirebaseFirestore.getInstance()
        rvKetekDriver = binding!!.rvKetekDriver
        rvKetekDriver.setHasFixedSize(true)

        observeKetekdriverData()
        fetchKetekDriverData()
    }

    private fun setupProfile(){
        viewModel.getUserId().observe(viewLifecycleOwner) {userId ->
            val db = FirebaseFirestore.getInstance()
            val userRef = db.collection("Drivers").document(userId)

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

    private fun fetchKetekDriverData() {
        val listKetekDriver = ArrayList<KetekDriver>()
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            fStore.collection("Drivers").document(userId)
                .collection("Keteks").get()
                .addOnSuccessListener { driverKetek ->
                    for (ketekDocument in driverKetek) {
                        val ketekPhoto = ketekDocument.getString("PhotoUrl")
                        val ketekName = ketekDocument.getString("Name").toString()
                        val ketekPlaceStart = ketekDocument.getString("PlaceStart").toString()
                        val ketekPlaceEnd = ketekDocument.getString("PlaceEnd").toString()
                        val ketekTime = ketekDocument.getString("Time").toString()
                        val ketekCapacity = ketekDocument.getLong("Capacity").toString().toInt()
                        val ketekPrice = ketekDocument.getString("Price").toString()

                        val ketekDriver = KetekDriver(ketekPhoto, ketekName, ketekPlaceStart, ketekPlaceEnd, ketekTime, ketekCapacity, ketekPrice)
                        listKetekDriver.add(ketekDriver)
                    }
                    ketekDriverLiveData.value = listKetekDriver
                }
        }
    }

    private fun observeKetekdriverData() {
        ketekDriverLiveData.observe(viewLifecycleOwner) { historyList ->
            list.clear()
            list.addAll(historyList)
            showRecycleList()
        }
    }

    private fun showRecycleList() {
        rvKetekDriver.layoutManager = LinearLayoutManager(requireContext())
        val listKetekDriverAdapter = ListKetekDriverAdapter(list)
        rvKetekDriver.adapter = listKetekDriverAdapter
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}