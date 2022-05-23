package com.example.myblescanner

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

data class DeviceInfo(val address: String, val name: String)

class ListDevicesAdapter(val data: MutableList<DeviceInfo>) : RecyclerView.Adapter<ListDevicesAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val address: TextView
        val name: TextView

        init {
            address = view.findViewById(R.id.ble_address)
            name = view.findViewById(R.id.ble_name)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(data.get(position)) {
            holder.address.text = address
            holder.name.text = name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.ble_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = data.size
}