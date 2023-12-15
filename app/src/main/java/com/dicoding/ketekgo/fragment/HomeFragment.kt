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
import com.dicoding.ketekgo.adapter.ListDestinationAdapter
import com.dicoding.ketekgo.adapter.ListKetekAdapter
import com.dicoding.ketekgo.databinding.FragmentHomeBinding
import com.dicoding.ketekgo.dummydata.Destination
import com.dicoding.ketekgo.dummydata.Ketek

class HomeFragment : Fragment() {
    private lateinit var rvKetek: RecyclerView
    private val listKetek = ArrayList<Ketek>()

    private lateinit var rvDestination: RecyclerView
    private val listDestination = ArrayList<Destination>()

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
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

        rvKetek = binding.rvKetek
        rvKetek.setHasFixedSize(true)

        listKetek.addAll(getListKetek())
        showRecycleListKetek()

        rvDestination = binding.rvDestination
        rvDestination.setHasFixedSize(true)

        listDestination.addAll(getListDestination())
        showRecycleDestination()
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

    private fun getListDestination(): ArrayList<Destination> {
        val dataDestinationPhoto = resources.obtainTypedArray(R.array.data_destination_photo)
        val dataDestinationName = resources.getStringArray(R.array.data_destination_name)
        val listDestination = ArrayList<Destination>()
        for (i in dataDestinationName.indices) {
            val destination = Destination(dataDestinationPhoto.getResourceId(i, -1), dataDestinationName[i])
            listDestination.add(destination)
        }
        return listDestination
    }

    private fun showRecycleListKetek() {
        rvKetek.layoutManager = LinearLayoutManager(requireContext())
        val listKetekAdapter = ListKetekAdapter(listKetek)
        rvKetek.adapter = listKetekAdapter
    }

    private fun showRecycleDestination() {
        rvDestination.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        val listDestinationAdapter = ListDestinationAdapter(listDestination)
        rvDestination.adapter = listDestinationAdapter
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}