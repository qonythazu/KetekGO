package com.dicoding.ketekgo.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.ketekgo.R
import com.dicoding.ketekgo.dataclass.Destination

class ListDestinationAdapter(private val listDestination: MutableList<Destination>): RecyclerView.Adapter<ListDestinationAdapter.ListViewHolder>() {
    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgPhoto: ImageView = itemView.findViewById(R.id.img_item_destination_photo)
        val tvName: TextView = itemView.findViewById(R.id.tv_item_destination_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_row_destination, parent, false)
        return ListViewHolder(view)
    }

    override fun getItemCount(): Int = listDestination.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val currentDestination = listDestination[position]
        holder.imgPhoto.setImageResource(R.drawable.ic_launcher_foreground)
        holder.tvName.text = currentDestination.name
    }

    fun updateData(newDestinationList: List<Destination>) {
        listDestination.clear()
        listDestination.addAll(newDestinationList)
        notifyDataSetChanged()
    }
}