package com.example.basicandroid.ui.location.adapter

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.example.basicandroid.data.roomdb.table.LocationTable

class LocationAdapter(
    private val clickItem: (location: LocationTable) -> Unit
): PagingDataAdapter<LocationTable, LocationViewHolder>(DiffUtilCallBack()) {
    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) {
        getItem(position)?.run {
            holder.bind(this)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder {
        return LocationViewHolder.create(parent, clickItem)
    }

    class DiffUtilCallBack: DiffUtil.ItemCallback<LocationTable>() {
        override fun areItemsTheSame(oldItem: LocationTable, newItem: LocationTable): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: LocationTable, newItem: LocationTable): Boolean {
            return oldItem.id == newItem.id
        }
    }
}