package com.example.basicandroid.ui.location.adapter

import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter

class LocationStateAdapter(private val retry: () -> Unit): LoadStateAdapter<LocationStateViewHolder>() {
    override fun onBindViewHolder(holder: LocationStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): LocationStateViewHolder {
        return LocationStateViewHolder.create(parent, retry)
    }
}