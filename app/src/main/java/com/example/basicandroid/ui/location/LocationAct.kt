package com.example.basicandroid.ui.location

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.map
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.basicandroid.R
import com.example.basicandroid.data.models.Location
import com.example.basicandroid.data.roomdb.LocationDB
import com.example.basicandroid.databinding.LocationActBinding
import com.example.basicandroid.ui.location.adapter.LocationAdapter
import com.example.basicandroid.ui.location.adapter.LocationStateAdapter
import kotlinx.coroutines.flow.collectLatest

@ExperimentalPagingApi
class LocationAct : AppCompatActivity() {
    private lateinit var binding: LocationActBinding
    private lateinit var locationViewModel: LocationViewModel
    private val database by lazy {
        LocationDB.get(context = applicationContext)
    }
    private val locationAdapter by lazy {
        LocationAdapter(clickItem = {
            Toast.makeText(this, "Name: " + it.name, Toast.LENGTH_SHORT).show()
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.location_act)
        locationViewModel = LocationViewModel(applicationContext)
        binding = DataBindingUtil.setContentView(this, R.layout.location_act)

        initLocationRecycle()
        initViewModel()
    }
    private fun initLocationRecycle() {
        binding.locationList?.apply {
            layoutManager = LinearLayoutManager(applicationContext)
            val decoration = DividerItemDecoration(applicationContext, DividerItemDecoration.VERTICAL)
            addItemDecoration(decoration)
            adapter = locationAdapter.withLoadStateFooter(
                LocationStateAdapter(retry = {
                    locationAdapter.retry()
                })
            )
        }
    }

    private fun initViewModel() {
        lifecycleScope.launchWhenCreated {
            locationViewModel.loadData().collectLatest {
                locationAdapter.submitData(it)
            }
        }
    }
}