package com.example.basicandroid.data.roomdb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.basicandroid.data.models.Location
import com.example.basicandroid.data.models.LocationRemoteKey

@Database(entities = [Location::class, LocationRemoteKey::class], version = 1)
abstract class LocationDB: RoomDatabase() {
    abstract fun locationDao(): LocationDao
    abstract fun locationRemoteKeyDao(): LocationRemoteKeyDao

    companion object {
        @Volatile
        private var instance: LocationDB? = null
        fun get(context: Context): LocationDB {
            if(instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    LocationDB::class.java,
                    "locationdb"
                ).allowMainThreadQueries().build()
            }
            return instance!!
        }
    }
}