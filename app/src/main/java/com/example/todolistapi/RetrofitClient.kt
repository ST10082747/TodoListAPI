package com.example.todolistapi

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    // Running server from VS Code
   // private const val BASE_URL = "http://10.0.2.2:3000/"

    // Running server from EC2 instance
    private const val BASE_URL = "http://13.60.169.37:3000/"

    val apiService: TodoApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TodoApiService::class.java)
    }
}