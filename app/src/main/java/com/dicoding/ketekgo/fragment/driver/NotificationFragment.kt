package com.dicoding.ketekgo.fragment.driver

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.ketekgo.adapter.ListOrderAdapter
import com.dicoding.ketekgo.databinding.FragmentNotificationBinding
import com.dicoding.ketekgo.dataclass.Orders
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class NotificationFragment : Fragment() {
    private lateinit var fStore: FirebaseFirestore
    private lateinit var rvOrders: RecyclerView
    private var list = ArrayList<Orders>()
    private var _binding: FragmentNotificationBinding? = null
    private val binding get() = _binding!!
    private val orderLiveData = MutableLiveData<ArrayList<Orders>>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fStore = FirebaseFirestore.getInstance()
        rvOrders = binding.rvOrders
        rvOrders.setHasFixedSize(true)

        observeOrderData()
        fetchOrdersData()
    }

    private fun fetchOrdersData() {
        val listOrders = ArrayList<Orders>()
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            fStore.collection("Drivers").document(userId)
                .collection("History").get()
                .addOnSuccessListener { historyOrder ->
                    for (ketekDocument in historyOrder) {
                        val ketekName = ketekDocument.getString("ketekName")
                        val ketekDestination = ketekDocument.getString("ketekDestination").toString()
                        val ketekTime = ketekDocument.getString("ketekTime").toString()
                        val ketekTotalPrice = ketekDocument.getLong("totalPrice")?.toInt()
                        val ketekDate = ketekDocument.getString("date").toString()
                        val ketekOrderer = ketekDocument.getString("customerName").toString()

                        val orders = Orders(ketekName, ketekDestination, ketekTime, ketekTotalPrice, ketekDate, ketekOrderer)
                        listOrders.add(orders)
                    }
                    orderLiveData.value = listOrders
                }
        }

    }
    private fun observeOrderData() {
        orderLiveData.observe(viewLifecycleOwner) { orderList ->
            list.clear()
            list.addAll(orderList)
            showRecycleList()
        }
    }

    private fun showRecycleList() {
        rvOrders.layoutManager = LinearLayoutManager(requireContext())
        val listKetekDriverAdapter = ListOrderAdapter(list)
        rvOrders.adapter = listKetekDriverAdapter
    }

}