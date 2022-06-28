package com.example.basicandroid.data.roomdb

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.basicandroid.data.models.LocationRemoteKey

@Dao
interface LocationRemoteKeyDao {
    @Query("select * from location where id = :id")
    suspend fun getRemoteKey(id: String): LocationRemoteKey

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllRemoteKeys(locationRemoteKey: List<LocationRemoteKey>)

    @Query("delete from location_remote_key")
    suspend fun deleteAllRemoteKeys()
}