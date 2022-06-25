package com.example.basicandroid.ui.location

import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.example.basicandroid.data.models.Location
import com.example.basicandroid.data.repository.LocationRepository
import kotlinx.coroutines.flow.Flow

class LocationViewModel: ViewModel() {
    private val apiRepository: LocationRepository = LocationRepository.instance

    suspend fun loadData(): Flow<PagingData<Location>> {
        return apiRepository.loadData()
    }
}