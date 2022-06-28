package com.example.basicandroid.data.roomdb

import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.basicandroid.data.models.Location

@Dao
interface LocationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllLocation(location: List<Location>)

    @Query("select * from location")
    fun selectLocation(): PagingSource<Int, Location>

    @Query("delete from location")
    suspend fun deleteAllLocation()
}