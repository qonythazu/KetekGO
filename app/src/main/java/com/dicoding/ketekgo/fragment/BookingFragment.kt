package com.dicoding.ketekgo.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.ketekgo.R
import com.dicoding.ketekgo.adapter.ListKetekAdapter
import com.dicoding.ketekgo.databinding.FragmentBookingBinding
import com.dicoding.ketekgo.dataclass.Ketek
import com.google.firebase.firestore.FirebaseFirestore

class BookingFragment : Fragment() {
    private lateinit var fStore: FirebaseFirestore
    private lateinit var rvKetek: RecyclerView
    private var _binding: FragmentBookingBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBookingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvKetek = binding.rvKetek
        rvKetek.setHasFixedSize(true)

        fStore = FirebaseFirestore.getInstance()

        getListKetek(requireContext())
    }

    private fun getListKetek(context: Context) {
        fStore.collection("Drivers").get()
            .addOnSuccessListener { driverResult ->
                val listKetek = ArrayList<Ketek>()

                for (driverDocument in driverResult) {
                    val driverDocumentId = driverDocument.id
                    Log.e("DriverID", driverDocumentId)

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
                            showRecycleList(listKetek, context)
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


    private fun showRecycleList(listKetek: ArrayList<Ketek>, context: Context) {
        rvKetek.layoutManager = LinearLayoutManager(context)
        val listKetekAdapter = ListKetekAdapter(listKetek)

        listKetekAdapter.setOnItemClickListener(object : ListKetekAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                val selectedItem = listKetek[position]

                val bundle = Bundle().apply {
                    putParcelable(BookingDetailFragment.ITEM, selectedItem)
                }
                Log.e("KETEKID", "${selectedItem.ketekId}")
                findNavController().navigate(
                    R.id.action_bookingFragment_to_bookingDetailFragment,
                    bundle
                )
            }
        })

        rvKetek.adapter = listKetekAdapter
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
