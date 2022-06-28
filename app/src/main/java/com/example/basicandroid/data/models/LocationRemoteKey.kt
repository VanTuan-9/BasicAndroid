package com.example.basicandroid.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "location_remote_key")
data class LocationRemoteKey (
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val prevPage: Int?,
    val nextPage: Int?
)