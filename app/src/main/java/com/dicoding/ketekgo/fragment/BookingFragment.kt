package com.dicoding.ketekgo.fragment

import android.os.Bundle
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

        getListKetek()
    }

    private fun getListKetek() {
        fStore.collection("Keteks").get()
            .addOnSuccessListener { result ->
                val listKetek = ArrayList<Ketek>()
                for (document in result) {
                    val username = document.getString("IDUser")
                    val photo = document.getString("PhotoURL")
                    val name = document.getString("Name")
                    val placeStart = document.getString("PlaceStart")
                    val placeEnd = document.getString("PlaceEnd")
                    val time = document.getString("Time")
                    val capacity = document.getLong("Capacity")?.toInt()
                    val price = document.getString("Price")

                    val ketek = Ketek(username, photo, name, placeStart, placeEnd, time, capacity, price)
                    listKetek.add(ketek)
                }
                showRecycleList(listKetek)
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
                findNavController().navigate(R.id.action_bookingFragment_to_bookingDetailFragment, bundle)
            }
        })

        rvKetek.adapter = listKetekAdapter
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}