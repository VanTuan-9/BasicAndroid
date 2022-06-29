package com.example.basicandroid.data.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LocationRetrofitInstance {
    companion object {
        private const val api = "https://rickandmortyapi.com/api/"

        private val retrofit: Retrofit by lazy {

            val interceptor = HttpLoggingInterceptor()
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            val client: OkHttpClient = OkHttpClient.Builder()
                .addInterceptor(interceptor).build()

            Retrofit.Builder()
                .baseUrl(api)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        fun getInstance(): ApiService {
            return retrofit.create(ApiService::class.java)
        }
    }
}