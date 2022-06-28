package com.example.basicandroid.ui.location

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import com.example.basicandroid.data.models.Location
import com.example.basicandroid.data.repository.LocationRepository
import com.example.basicandroid.data.roomdb.LocationDB
import kotlinx.coroutines.flow.Flow

class LocationViewModel(context: Context): ViewModel() {
    private val apiRepository: LocationRepository = LocationRepository(context)

    @ExperimentalPagingApi
    fun loadData(): Flow<PagingData<Location>> {
        return apiRepository.loadDataDB()
    }
}