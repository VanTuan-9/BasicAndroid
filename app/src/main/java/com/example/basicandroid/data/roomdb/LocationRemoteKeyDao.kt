package com.example.basicandroid.data.roomdb

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.basicandroid.data.roomdb.table.LocationKeyTable

@Dao
interface LocationRemoteKeyDao {
    @Query("select * from location_remote_key where id = :id")
    suspend fun getRemoteKey(id: String): LocationKeyTable?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllRemoteKeys(locationRemoteKey: List<LocationKeyTable>)

    @Query("delete from location_remote_key")
    suspend fun deleteAllRemoteKeys()
}