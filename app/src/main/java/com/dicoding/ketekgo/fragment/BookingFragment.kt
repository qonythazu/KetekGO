package com.dicoding.ketekgo.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.ketekgo.R
import com.dicoding.ketekgo.adapter.ListKetekAdapter
import com.dicoding.ketekgo.databinding.FragmentBookingBinding
import com.dicoding.ketekgo.dummydata.Ketek

class BookingFragment : Fragment() {
    private lateinit var rvKetek: RecyclerView
    private val list = ArrayList<Ketek>()
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

        list.addAll(getListKetek())
        showRecycleList()
    }

    private fun getListKetek(): ArrayList<Ketek> {
        val dataUsername = resources.getStringArray(R.array.data_username)
        val dataPhoto = resources.obtainTypedArray(R.array.data_photo)
        val dataName = resources.getStringArray(R.array.data_name)
        val dataFrom = resources.getStringArray(R.array.data_from)
        val dataTo = resources.getStringArray(R.array.data_to)
        val dataTime = resources.getStringArray(R.array.data_time)
        val dataCapacity = resources.obtainTypedArray(R.array.data_capacity)
        val dataPrice = resources.getStringArray(R.array.data_price)
        val listKetek = ArrayList<Ketek>()
        for (i in dataUsername.indices) {
            val ketek = Ketek(dataUsername[i], dataPhoto.getResourceId(i, -1), dataName[i], dataFrom[i], dataTo[i], dataTime[i], dataCapacity.getIndex(i), dataPrice[i])
            listKetek.add(ketek)
        }
        return listKetek
    }

    private fun showRecycleList() {
        rvKetek.layoutManager = LinearLayoutManager(requireContext())
        val listKetekAdapter = ListKetekAdapter(list)

        listKetekAdapter.setOnItemClickListener(object : ListKetekAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                val selectedItem = list[position]

                Navigation.createNavigateOnClickListener(R.id.action_bookingFragment_to_bookingDetailFragment)
            }
        })

        rvKetek.adapter = listKetekAdapter
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}