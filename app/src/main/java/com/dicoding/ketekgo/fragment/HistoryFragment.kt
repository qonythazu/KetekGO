package com.dicoding.ketekgo.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.ketekgo.R
import com.dicoding.ketekgo.adapter.ListHistoryAdapter
import com.dicoding.ketekgo.databinding.FragmentHistoryBinding
import com.dicoding.ketekgo.dataclass.History

class HistoryFragment : Fragment() {
    private lateinit var rvHistory: RecyclerView
    private val list = ArrayList<History>()

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvHistory = binding.rvHistory
        rvHistory.setHasFixedSize(true)

        list.addAll(getListHistory())
        showRecycleList()
    }

    private fun getListHistory(): ArrayList<History> {
        val ketekName = resources.getStringArray(R.array.data_name)
        val ketekFrom = resources.getStringArray(R.array.data_from)
        val ketekTo = resources.getStringArray(R.array.data_to)
        val ketekTime = resources.getStringArray(R.array.data_time)
        val ketekPrice = resources.getStringArray(R.array.data_price)
        val status = resources.getStringArray(R.array.data_history_status)
        val date = resources.getStringArray(R.array.data_history_date)
        val listHistory = ArrayList<History>()
        for (i in ketekName.indices) {
            val history = History(ketekName[i], ketekFrom[i], ketekTo[i], ketekTime[i], ketekPrice[i], status[i], date[i])
            listHistory.add(history)
        }
        return listHistory
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