package com.dicoding.ketekgo.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.ketekgo.R
import com.dicoding.ketekgo.adapter.ListDestinationAdapter
import com.dicoding.ketekgo.adapter.ListKetekAdapter
import com.dicoding.ketekgo.data.Result
import com.dicoding.ketekgo.databinding.FragmentHomeBinding
import com.dicoding.ketekgo.dataclass.Destination
import com.dicoding.ketekgo.dataclass.Ketek
import com.dicoding.ketekgo.dataclass.PreferencesRequest
import com.dicoding.ketekgo.viewmodel.UserViewModel
import com.dicoding.ketekgo.viewmodel.ViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class HomeFragment : Fragment() {
    private lateinit var fStore: FirebaseFirestore
    private lateinit var rvKetek: RecyclerView

    private lateinit var rvDestination: RecyclerView
    private val listDestination = ArrayList<Destination>()

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: UserViewModel by activityViewModels {
        ViewModelFactory.getInstance(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnMore.setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.action_homeFragment_to_destinationGridFragment)
        )

        fStore = FirebaseFirestore.getInstance()

        rvKetek = binding.rvKetek
        rvKetek.setHasFixedSize(true)

        getListKetek()

        rvDestination = binding.rvDestination
        rvDestination.setHasFixedSize(true)

        val currentUser = FirebaseAuth.getInstance().currentUser
        currentUser?.let { user ->
            val db = FirebaseFirestore.getInstance()
            val preferencesCollection =
                db.collection("Customers").document(user.uid).collection("Preferences")

            preferencesCollection.get().addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    val data = documents.documents
                    for (i in data) {
                        val sampleData = i.data?.values
                        sampleData?.forEach {sample ->
                            val listData = sample as List<String>
                            val preferencesData = PreferencesRequest(listData)
                            Log.e("HADEH", "$preferencesData")
                            viewModel.recomendDestination(preferencesData).observe(requireActivity()) { result ->
                                when (result) {
                                    is Result.Loading -> {
                                        Log.e("DEBUGAPI", "Loading")
                                    }

                                    is Result.Success -> {
                                        val destination = result.data.topLabels?.forEach { dest ->
                                            Destination(photo = null, name = dest.toString())
                                        }
                                        showRecycleDestination(destination as Destination)
                                        Log.e("SUCCESS", "$destination")
                                    }

                                    is Result.Error -> {
                                        Log.e("ERROR", result.error)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        listDestination.addAll(getListDestination())
    }

    private fun getListKetek() {
        fStore.collection("Drivers").get()
            .addOnSuccessListener { driverResult ->
                val listKetek = ArrayList<Ketek>()

                for (driverDocument in driverResult) {
                    // Mendapatkan ID dokumen driver
                    val driverDocumentId = driverDocument.id
                    Log.e("DriverID", driverDocumentId)
                    // Mengakses koleksi "Keteks" di dalam dokumen driver
                    fStore.collection("Drivers").document(driverDocumentId)
                        .collection("Keteks").get()
                        .addOnSuccessListener { keteksResult ->
                            for (ketekDocument in keteksResult) {
                                val ketekId = ketekDocument.id
                                val username = ketekDocument.getString("IDUser")
                                val photo = ketekDocument.getString("PhotoUrl")
                                val name = ketekDocument.getString("Name")
                                val placeStart = ketekDocument.getString("PlaceStart")
                                val placeEnd = ketekDocument.getString("PlaceEnd")
                                val time = ketekDocument.getString("Time")
                                val capacity = ketekDocument.getLong("Capacity")?.toInt()
                                val price = ketekDocument.getString("Price")

                                val ketek = Ketek(
                                    ketekId,
                                    driverDocumentId,
                                    username,
                                    photo,
                                    name,
                                    placeStart,
                                    placeEnd,
                                    time,
                                    capacity,
                                    price
                                )
                                listKetek.add(ketek)
                            }
                            showRecycleList(listKetek)
                        }
                        .addOnFailureListener { e ->
                            Log.e("TAG", "Error getting Keteks documents: $e")
                        }
                }
            }
            .addOnFailureListener { e ->
                Log.e("TAG", "Error getting Drivers documents: $e")
            }
    }


    private fun showRecycleList(listKetek: ArrayList<Ketek>) {
        rvKetek.layoutManager = LinearLayoutManager(requireContext())
        val listKetekAdapter = ListKetekAdapter(listKetek)

        listKetekAdapter.setOnItemClickListener(object : ListKetekAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                val selectedItem = listKetek[position]

                val bundle = Bundle().apply {
                    putParcelable(BookingDetailFragment.ITEM, selectedItem)
                }
                findNavController().navigate(
                    R.id.action_homeFragment_to_bookingDetailFragment,
                    bundle
                )
            }
        })

        rvKetek.adapter = listKetekAdapter
    }

    private fun getListDestination(): ArrayList<Destination> {
        val dataDestinationPhoto = resources.obtainTypedArray(R.array.data_destination_photo)
        val dataDestinationName = resources.getStringArray(R.array.data_destination_name)
        val listDestination = ArrayList<Destination>()
        for (i in dataDestinationName.indices) {
            val destination =
                Destination(dataDestinationPhoto.getResourceId(i, -1), dataDestinationName[i])
            listDestination.add(destination)
        }
        return listDestination
    }

    private fun showRecycleDestination(list: Destination) {
        rvDestination.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        val listDestinationAdapter = ListDestinationAdapter(listDestination)
        rvDestination.adapter = listDestinationAdapter
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}