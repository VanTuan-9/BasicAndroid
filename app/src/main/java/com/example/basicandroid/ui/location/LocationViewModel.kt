package com.example.basicandroid.ui.location

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import com.example.basicandroid.data.roomdb.table.LocationTable
import com.example.basicandroid.data.repository.LocationRepository
import kotlinx.coroutines.flow.Flow

class LocationViewModel(context: Context): ViewModel() {
    private val apiRepository: LocationRepository = LocationRepository(context)

    @ExperimentalPagingApi
    fun loadData(): Flow<PagingData<LocationTable>> {
        return apiRepository.loadDataDB()
    }
}