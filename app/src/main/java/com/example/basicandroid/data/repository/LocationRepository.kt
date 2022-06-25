package com.example.basicandroid.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.basicandroid.data.api.ApiService
import com.example.basicandroid.data.api.LocationRetrofitInstance
import com.example.basicandroid.data.models.Location
import kotlinx.coroutines.flow.Flow

class LocationRepository {
    private val apiService:ApiService by lazy {
        LocationRetrofitInstance.getInstance()
    }
    suspend fun loadData(): Flow<PagingData<Location>> {
        return Pager(
            config = PagingConfig(pageSize = 20, maxSize = 200),
            pagingSourceFactory = {
                LocationPagingSource(apiService)
            }
        ).flow
    }
    companion object {
        val instance by lazy {
            LocationRepository()
        }
    }
}