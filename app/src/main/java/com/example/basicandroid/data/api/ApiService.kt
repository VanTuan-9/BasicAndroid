package com.example.basicandroid.data.api

import com.example.basicandroid.data.models.LocationPageResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("location")
    suspend fun getPageLocation(@Query("page") page: Int): LocationPageResponse
}