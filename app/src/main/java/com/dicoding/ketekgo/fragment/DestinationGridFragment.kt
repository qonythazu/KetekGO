package com.dicoding.ketekgo.fragment

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.ketekgo.R
import com.dicoding.ketekgo.activity.MainActivity
import com.dicoding.ketekgo.adapter.ListDestinationAdapter
import com.dicoding.ketekgo.databinding.FragmentDestinationGridBinding
import com.dicoding.ketekgo.dataclass.Destination
import com.google.android.material.floatingactionbutton.FloatingActionButton

class DestinationGridFragment : Fragment() {
    private lateinit var rvDestination: RecyclerView
    private val listDestination = ArrayList<Destination>()
    private var _binding: FragmentDestinationGridBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDestinationGridBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.floatingButton.setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.action_destinationGridFragment_to_destinationInputPreferenceFragment)
        )

        binding.btnBack.setOnClickListener{
            findNavController().navigateUp()
        }

        val floatingActionButton: FloatingActionButton = binding.floatingButton
        floatingActionButton.setImageResource(R.drawable.ic_add)
        floatingActionButton.imageTintList = ColorStateList.valueOf(Color.WHITE)

        rvDestination = binding.rvDestination
        rvDestination.setHasFixedSize(true)

        listDestination.addAll(getListDestination())
        showRecycleDestination()
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

    private fun showRecycleDestination() {
        rvDestination.layoutManager = GridLayoutManager(requireContext(), 2)
        val listDestinationAdapter = ListDestinationAdapter(listDestination)
        rvDestination.adapter = listDestinationAdapter
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}