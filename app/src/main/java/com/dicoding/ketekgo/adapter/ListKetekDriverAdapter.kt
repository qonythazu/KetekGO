package com.dicoding.ketekgo.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.ketekgo.R
import com.dicoding.ketekgo.dataclass.KetekDriver

class ListKetekDriverAdapter(private val listKetekDriver: ArrayList<KetekDriver>) : RecyclerView.Adapter<ListKetekDriverAdapter.ListViewHolder>(){
    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgPhoto: ImageView = itemView.findViewById((R.id.img_item_ketek_photo))
        val tvItemKetekName: TextView = itemView.findViewById(R.id.tv_item_ketek_name)
        val tvItemKetekFrom: TextView = itemView.findViewById(R.id.tv_item_ketek_from)
        val tvItemKetekTo: TextView = itemView.findViewById(R.id.tv_item_ketek_to)
        val tvItemKetekTime: TextView = itemView.findViewById(R.id.tv_item_ketek_time)
        val tvItemKetekCapacity: TextView = itemView.findViewById(R.id.tv_item_ketek_capacity)
        val tvItemKetekPrice: TextView = itemView.findViewById(R.id.tv_item_ketek_cost)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_row_ketek_driver, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (photo, name, from, to, time, capacity, price) = listKetekDriver[position]
        Glide.with(holder.itemView.context)
            .load(photo)
            .into(holder.imgPhoto)
        holder.tvItemKetekName.text = name
        holder.tvItemKetekFrom.text = from
        holder.tvItemKetekTo.text = to
        holder.tvItemKetekTime.text = time
        holder.tvItemKetekCapacity.text = capacity.toString()
        holder.tvItemKetekPrice.text = price
    }

    override fun getItemCount(): Int = listKetekDriver.size
}