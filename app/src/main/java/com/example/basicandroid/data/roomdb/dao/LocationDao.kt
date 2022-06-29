package com.example.basicandroid.data.roomdb.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.basicandroid.data.roomdb.table.LocationTable

@Dao
interface LocationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllLocation(location: List<LocationTable>)

    @Query("select * from location ORDER BY `index` ASC ")
    fun selectLocation(): PagingSource<Int, LocationTable>

    @Query("select * from location ORDER BY `index` ASC LIMIT 1")
    suspend fun firstLocation(): LocationTable?

    @Query("select * from location ORDER BY `index` DESC LIMIT 1")
    suspend fun lastLocation(): LocationTable?

    @Query("delete from location")
    suspend fun deleteAllLocation()
}