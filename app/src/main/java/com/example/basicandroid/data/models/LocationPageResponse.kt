package com.example.basicandroid.data.models

import com.example.basicandroid.data.roomdb.table.LocationTable

data class LocationPageResponse(
    val info: Info? = null,
    val results: List<LocationTable>? = null
)