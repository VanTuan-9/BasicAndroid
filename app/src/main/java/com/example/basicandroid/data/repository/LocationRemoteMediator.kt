package com.example.basicandroid.data.repository

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.example.basicandroid.data.api.ApiService
import com.example.basicandroid.data.models.Location
import com.example.basicandroid.data.models.LocationRemoteKey
import com.example.basicandroid.data.roomdb.LocationDB
import java.lang.Exception
import androidx.room.withTransaction

@ExperimentalPagingApi
class LocationRemoteMediator(
    private val database: LocationDB,
    private val apiService: ApiService
): RemoteMediator<Int, Location>() {
    private val locationDao = database.locationDao()
    private val locationRemoteKeyDao = database.locationRemoteKeyDao()

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Location>
    ): MediatorResult {
        Log.d("PAGE", "b")
        return try {
            val currentPage =when(loadType) {
                LoadType.REFRESH -> {
                    Log.d("PAGE", "a")
                    val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                    remoteKeys?.nextPage?.minus(1) ?: 1
                }
                LoadType.PREPEND -> {
                    val remoteKeys = getRemoteKeyForFirstItem(state)
                    val prevPage = remoteKeys?.prevPage
                        ?: return MediatorResult.Success(
                            endOfPaginationReached = remoteKeys != null
                        )
                    Log.d("PAGE", prevPage.toString())
                    prevPage
                }
                LoadType.APPEND -> {
                    val remoteKeys = getRemoteKeyForLastItem(state)
                    val nextPage = remoteKeys?.nextPage
                        ?: return MediatorResult.Success(
                            endOfPaginationReached = remoteKeys != null
                        )
                    Log.d("PAGE", nextPage.toString())
                    nextPage
                }
            }
            Log.d("PAGE", currentPage.toString())
            val response = apiService.getPageLocation(currentPage)
            val endOfPaginationReached = response.results?.run {
                isEmpty()
            } ?: true

            val prevPage = if (currentPage == 1) null else currentPage - 1
            val nextPage = if (endOfPaginationReached) null else currentPage + 1

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    locationDao.deleteAllLocation()
                    locationRemoteKeyDao.deleteAllRemoteKeys()
                }
                val keys = response.results?.map { location ->
                    LocationRemoteKey(
                        id = location.id,
                        prevPage = prevPage,
                        nextPage = nextPage
                    )
                }?: mutableListOf()
                locationRemoteKeyDao.insertAllRemoteKeys(keys)
                locationDao.insertAllLocation(response.results?: mutableListOf())
            }
            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: Exception) {
            return MediatorResult.Error(e)
        }
    }
    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, Location>
    ): LocationRemoteKey? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                locationRemoteKeyDao.getRemoteKey(id = id)
            }
        }
    }
//
    private suspend fun getRemoteKeyForFirstItem(
        state: PagingState<Int, Location>
    ): LocationRemoteKey? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { location ->
                locationRemoteKeyDao.getRemoteKey(id = location.id)
            }
    }
//
    private suspend fun getRemoteKeyForLastItem(
        state: PagingState<Int, Location>
    ): LocationRemoteKey? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { location ->
                locationRemoteKeyDao.getRemoteKey(id = location.id)
            }
    }
}