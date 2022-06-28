package com.example.basicandroid.ui.location.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.example.basicandroid.databinding.LocationStateItemBinding

class LocationStateViewHolder(
    private val binding: LocationStateItemBinding,
    private val retry: () ->Unit
): RecyclerView.ViewHolder(binding.root) {

    fun bind(loadState: LoadState) {
        binding.loading.isVisible = loadState is LoadState.Loading
        binding.retryBtn.isVisible = loadState is LoadState.Error
        binding.retryBtn.setOnClickListener {
            retry.invoke()
        }
    }

    companion object {
        fun create(
            parent: ViewGroup,
            retry: () -> Unit
        ): LocationStateViewHolder {
            val binding = LocationStateItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return LocationStateViewHolder(binding, retry)
        }
    }
}