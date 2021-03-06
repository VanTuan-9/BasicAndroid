package com.example.basicandroid.data.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.basicandroid.data.api.ApiService
import com.example.basicandroid.data.roomdb.table.LocationTable
import kotlinx.coroutines.delay

class LocationPagingSource(private val apiService: ApiService): PagingSource<Int, LocationTable>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LocationTable> {
        return kotlin.runCatching {
            val nextKey = params.key ?: FIRST_PAGE
            val response = apiService.getPageLocation(nextKey)
            val dataList = response.results ?: mutableListOf()
            val isEnd = response.info?.next.isNullOrEmpty()
            delay(500)
            LoadResult.Page(
                data = dataList,
                prevKey = null,
                nextKey = if(isEnd) null else nextKey + 1
            )
        }.getOrElse {
            it.message?.takeIf { it.equals("HTTP 404 Not Found") }?.run {
                LoadResult.Page(
                    data = mutableListOf(),
                    prevKey = null,
                    nextKey = null
                )
            } ?: kotlin.run {
                LoadResult.Error(it)
            }
        }
    }
    companion object {
        private const val FIRST_PAGE = 1
    }

    override fun getRefreshKey(state: PagingState<Int, LocationTable>): Int? {
        TODO("Not yet implemented")
    }
}