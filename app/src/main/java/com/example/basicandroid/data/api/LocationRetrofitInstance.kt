package com.example.basicandroid.data.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LocationRetrofitInstance {
    companion object {
        private const val api = "https://rickandmortyapi.com/api/"

        private val retrofit: Retrofit by lazy {
            Retrofit.Builder()
                .baseUrl(api)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        fun getInstance(): ApiService {
            return retrofit.create(ApiService::class.java)
        }
    }
}