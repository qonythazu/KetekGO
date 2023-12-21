package com.dicoding.ketekgo.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.ketekgo.adapter.ListHistoryAdapter
import com.dicoding.ketekgo.databinding.FragmentHistoryBinding
import com.dicoding.ketekgo.dataclass.History
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class HistoryFragment : Fragment() {
    private lateinit var fStore: FirebaseFirestore
    private lateinit var rvHistory: RecyclerView
    private val list = ArrayList<History>()

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!

    private val historyLiveData = MutableLiveData<ArrayList<History>>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fStore = FirebaseFirestore.getInstance()

        rvHistory = binding.rvHistory
        rvHistory.setHasFixedSize(true)

        observeHistoryData()
        fetchHistoryData()
    }

    private fun fetchHistoryData() {
        val listHistory = ArrayList<History>()
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            fStore.collection("Customers").document(userId)
                .collection("Bookings").get()
                .addOnSuccessListener { historyBookings ->
                    for (bookingDocument in historyBookings) {
                        val ketekName = bookingDocument.getString("name").toString()
                        val ketekDestination = bookingDocument.getString("destination").toString()
                        val ketekTime = bookingDocument.getString("time").toString()
                        val ketekTotalPrice = bookingDocument.getLong("totalPrice").toString()
                        val ketekPassengers = bookingDocument.getLong("passengers").toString().toInt()
                        val status = bookingDocument.getString("status").toString()
                        val date = bookingDocument.getString("date").toString()

                        val history = History(ketekName, ketekDestination, ketekTime, ketekTotalPrice, ketekPassengers, status, date)
                        listHistory.add(history)
                    }
                    historyLiveData.value = listHistory
                }
        }
    }

    private fun observeHistoryData() {
        historyLiveData.observe(viewLifecycleOwner) { historyList ->
            list.clear()
            list.addAll(historyList)
            showRecycleList()
        }
    }

    private fun showRecycleList() {
        rvHistory.layoutManager = LinearLayoutManager(requireContext())
        val listHistoryAdapter = ListHistoryAdapter(list)
        rvHistory.adapter = listHistoryAdapter
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
