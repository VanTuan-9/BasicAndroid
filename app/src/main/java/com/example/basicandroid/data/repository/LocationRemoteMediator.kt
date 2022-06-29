package com.example.basicandroid.data.repository

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.basicandroid.data.api.ApiService
import com.example.basicandroid.data.roomdb.LocationDB
import com.example.basicandroid.data.roomdb.table.LocationKeyTable
import com.example.basicandroid.data.roomdb.table.LocationTable
import com.google.gson.Gson

@ExperimentalPagingApi
class LocationRemoteMediator(
    private val database: LocationDB,
    private val apiService: ApiService
) : RemoteMediator<Int, LocationTable>() {
    private val locationDao = database.locationDao()
    private val locationRemoteKeyDao = database.locationRemoteKeyDao()

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, LocationTable>
    ): MediatorResult {
        return try {
            // Xác định trạng thái load
            // xác định key load
            val currentPage = when (loadType) {
                LoadType.REFRESH -> {
                    state.anchorPosition?.run {
                        state.closestItemToPosition(this)?.id?.run {
                            Log.d("LocationRemoteMediator", "REFRESH "+ this)
                            locationRemoteKeyDao.getRemoteKey(this)?.nextPage?.minus(1)
                        }
                    }?:6
                }
                LoadType.PREPEND -> {
                    Log.d("PAGE", "PREPEND")
                    Log.d("LocationRemoteMediator", "PREPEND ")
                    // load item ở vị trí đầu tiên
//                    val firstItem = state.pages.firstOrNull()?.data?.firstOrNull() // trả về cache bị xóa
//                    Log.d("LocationRemoteMediator", "firstItem " + firstItem)
//                    firstItem?.run {
//                        locationRemoteKeyDao.getRemoteKey(this.id)?.prevPage
//                    } ?: kotlin.run {
//                        // Pre Key = null-> end load
//                        return MediatorResult.Success(
//                            endOfPaginationReached = true
//                        )
//                    }

                    locationDao.firstLocation()?.run {
                        Log.d("LocationRemoteMediator", "firstItem " + Gson().toJson(this))
                        locationRemoteKeyDao.getRemoteKey(this.id)?.prevPage
                    }?: kotlin.run {
                        return MediatorResult.Success(
                            endOfPaginationReached = true
                        )
                    }
                }
                LoadType.APPEND -> {
                    Log.d("LocationRemoteMediator", "APPEND ")
                    Log.d("PAGE", "a")
                    // Load next key
                    // load last Item
                    val lastItem = state.pages.lastOrNull()?.data?.lastOrNull()
                    lastItem?.id?.run {
                        // next key
                        Log.d("LocationRemoteMediator", "lastItem " + Gson().toJson(this))
                        locationRemoteKeyDao.getRemoteKey(this)?.nextPage
                    } ?: kotlin.run {
                        // load to end
                        return MediatorResult.Success(
                            endOfPaginationReached = true
                        )
                    }
                }
            }
            Log.d("PAGE", currentPage.toString())
            Log.d("LocationRemoteMediator", "currentPage " + currentPage)
            val response = apiService.getPageLocation(currentPage)
            val dataList = response.results?.toMutableList() ?: mutableListOf()
            val endPaging = response.results.isNullOrEmpty() // end load when list = null || empty

            Log.d("LocationRemoteMediator", "dataList " + dataList.size)
            Log.d("LocationRemoteMediator", "dataListName " + dataList.map {
                it.name
            })
            // set index for item
            dataList.forEachIndexed { index, locationTable ->
                //index = vị trí trong list of Page
                locationTable.index = currentPage * 1000 + index
            }

            // sau khi tải đc dữ liệu về;. đổ dữ liệu vào DB
            database.withTransaction { // Phiên làm việc của DB
                if (loadType == LoadType.REFRESH) {
                    // Nếu mà lần đầu tiên tải dữ liệu về . sẽ clear cache cũ đi
                    locationDao.deleteAllLocation()
                    locationRemoteKeyDao.deleteAllRemoteKeys()
                }
                // insert key => RoomDB
                val keyList = dataList.map {
                    LocationKeyTable(
                        id = it.id,
                        prevPage = (currentPage - 1).takeIf { it > 0 }, // chỉ nhận giá trị dương >1
                        nextPage = currentPage + 1
                    )
                }
                locationRemoteKeyDao.insertAllRemoteKeys(keyList)

                // insert dataLIst => RoomDB
                locationDao.insertAllLocation(dataList)
            }
            Log.d("LocationRemoteMediator", "endOfPaginationReached " + endPaging)

            // Thông báo paging biết, là đã kết thúc rồi hay chưa .
            // còn dữ liệu sẽ đc đổ vào DB
            MediatorResult.Success(endOfPaginationReached = endPaging)
        } catch (e: Exception) {
            Log.d("LocationRemoteMediator", "e " + e)
            return MediatorResult.Error(e)
        }
    }
}