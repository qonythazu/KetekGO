package com.dicoding.ketekgo.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.ketekgo.R
import com.dicoding.ketekgo.dataclass.Orders

class ListOrderAdapter(private val listOrder: ArrayList<Orders>) : RecyclerView.Adapter<ListOrderAdapter.ListViewHolder>(){
    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvKetekName: TextView = itemView.findViewById(R.id.tv_item_ketek_name)
        val tvKetekDestination: TextView = itemView.findViewById(R.id.tv_item_ketek_to)
        val tvKetekTime: TextView = itemView.findViewById(R.id.tv_item_ketek_time)
        val tvKetekTotalPrice: TextView = itemView.findViewById(R.id.tv_item_ketek_cost)
        val tvKetekDate: TextView = itemView.findViewById(R.id.tv_order_date)
        val tvKetekOrderer: TextView = itemView.findViewById(R.id.tv_costumer_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_row_notification_driver, parent, false)
        return ListViewHolder(view)
    }

    override fun getItemCount(): Int  = listOrder.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (name, destination, time, totalPrice, date, orderer) = listOrder[position]
        holder.tvKetekName.text = name
        holder.tvKetekDestination.text = destination
        holder.tvKetekTime.text = time
        holder.tvKetekTotalPrice.text = totalPrice.toString()
        holder.tvKetekDate.text = date
        holder.tvKetekOrderer.text = orderer
    }
}