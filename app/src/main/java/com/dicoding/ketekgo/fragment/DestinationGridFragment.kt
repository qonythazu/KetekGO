package com.dicoding.ketekgo.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.dicoding.ketekgo.R
import com.dicoding.ketekgo.databinding.FragmentDestinationGridBinding

class DestinationGridFragment : Fragment() {
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
    }
}