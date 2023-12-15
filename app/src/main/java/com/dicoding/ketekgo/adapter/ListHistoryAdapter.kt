package com.dicoding.ketekgo.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.ketekgo.R
import com.dicoding.ketekgo.dummydata.History

class ListHistoryAdapter(private val listHistory: ArrayList<History>) : RecyclerView.Adapter<ListHistoryAdapter.ListViewHolder>() {
    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvKetekName: TextView = itemView.findViewById(R.id.tv_item_ketek_name)
        val tvKetekFrom: TextView = itemView.findViewById(R.id.tv_item_ketek_from)
        val tvKetekTo: TextView = itemView.findViewById(R.id.tv_item_ketek_to)
        val tvKetekTime: TextView = itemView.findViewById(R.id.tv_item_ketek_time)
        val tvKetekPrice: TextView = itemView.findViewById(R.id.tv_item_ketek_cost)
        val tvStatus: TextView = itemView.findViewById(R.id.tv_status)
        val tvDate: TextView = itemView.findViewById(R.id.tv_date)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_row_history, parent, false)
        return ListViewHolder(view)
    }

    override fun getItemCount(): Int = listHistory.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (ketekName, ketekFrom, ketekTo, ketekTime, ketekPrice, status, date) = listHistory[position]
        holder.tvKetekName.text = ketekName
        holder.tvKetekFrom.text = ketekFrom
        holder.tvKetekTo.text = ketekTo
        holder.tvKetekTime.text = ketekTime
        holder.tvKetekPrice.text = ketekPrice
        holder.tvStatus.text = status
        holder.tvDate.text = date
    }
}