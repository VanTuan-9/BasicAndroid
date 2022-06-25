package com.example.basicandroid.ui.location

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.basicandroid.R
import com.example.basicandroid.databinding.LocationActBinding
import com.example.basicandroid.ui.location.adapter.LocationAdapter
import kotlinx.coroutines.flow.collectLatest

class LocationAct : AppCompatActivity() {
    private lateinit var binding: LocationActBinding
    private lateinit var locationViewModel: LocationViewModel

    private val locationAdapter by lazy {
        LocationAdapter(clickItem = {
            Toast.makeText(this, "Name: " + it.name, Toast.LENGTH_SHORT)
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.location_act)
        locationViewModel = ViewModelProvider(this).get(LocationViewModel::class.java)
        binding = DataBindingUtil.setContentView(this, R.layout.location_act)
        initLocationRecycle()
        initViewModel()
    }
    private fun initLocationRecycle() {
        binding.locationList?.apply {
            layoutManager = LinearLayoutManager(applicationContext)
            val decoration = DividerItemDecoration(applicationContext, DividerItemDecoration.VERTICAL)
            addItemDecoration(decoration)
            adapter = locationAdapter
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