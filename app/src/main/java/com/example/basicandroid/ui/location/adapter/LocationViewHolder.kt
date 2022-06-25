package com.example.basicandroid.ui.location.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.basicandroid.data.models.Location
import com.example.basicandroid.databinding.LocationItemBinding

class LocationViewHolder(
    private val binding: LocationItemBinding,
    private val clickItem: (location: Location) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
    fun bind(location: Location) {
        initLayout(location)
    }

    private fun initLayout(location: Location) {
        binding.locationName.text = location.name
        binding.locationDimension.text = location.dimension
        binding.root.setOnClickListener {
            clickItem.invoke(location)
        }
    }

    companion object {
        fun create(parent: ViewGroup, clickItem: (location: Location) -> Unit): LocationViewHolder {
            val binding = LocationItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return LocationViewHolder(binding, clickItem)
        }
    }
}