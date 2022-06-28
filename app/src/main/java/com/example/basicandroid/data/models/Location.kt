package com.example.basicandroid.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "location")
data class Location(
    @PrimaryKey(autoGenerate = false) var id: String,
    @ColumnInfo var name: String? = null,
    @ColumnInfo var dimension: String? = null
)
