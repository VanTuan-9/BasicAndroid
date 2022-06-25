package com.example.basicandroid.ui.location.adapter

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.example.basicandroid.data.models.Location

class LocationAdapter(
    private val clickItem: (location: Location) -> Unit
): PagingDataAdapter<Location, LocationViewHolder>(DiffUtilCallBack()) {
    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) {
        getItem(position)?.run {
            holder.bind(this)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder {
        return LocationViewHolder.create(parent, clickItem)
    }

    class DiffUtilCallBack: DiffUtil.ItemCallback<Location>() {
        override fun areItemsTheSame(oldItem: Location, newItem: Location): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: Location, newItem: Location): Boolean {
            return oldItem.dimension == newItem.dimension
        }
    }
}