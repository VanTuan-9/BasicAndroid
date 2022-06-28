package com.example.basicandroid.data.repository

import android.content.Context
import androidx.paging.*
import com.example.basicandroid.data.api.ApiService
import com.example.basicandroid.data.api.LocationRetrofitInstance
import com.example.basicandroid.data.models.Location
import com.example.basicandroid.data.roomdb.LocationDB
import kotlinx.coroutines.flow.Flow

class LocationRepository(
    private val context: Context
) {
    private val database: LocationDB by lazy {
        LocationDB.get(context)
    }
    private val apiService:ApiService by lazy {
        LocationRetrofitInstance.getInstance()
    }
    fun loadData(): Flow<PagingData<Location>> {
        return Pager(
            config = PagingConfig(pageSize = 1, maxSize = 200),
            pagingSourceFactory = {
                LocationPagingSource(apiService)
            }
        ).flow
    }

    @ExperimentalPagingApi
    fun loadDataDB(): Flow<PagingData<Location>> {
        val pagingSourceFatory ={
            database.locationDao().selectLocation()
        }
        return Pager(
            config =PagingConfig(pageSize = 10),
            remoteMediator = LocationRemoteMediator(
                database = database, apiService = apiService
            ),
            pagingSourceFactory = pagingSourceFatory
        ).flow
    }
}