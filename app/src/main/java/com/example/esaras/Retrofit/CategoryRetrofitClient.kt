// CategoryRetrofitClient.kt
package com.example.esaras.APiInterface

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object CategoryRetrofitClient {
    private const val BASE_URL = "https://dummyjson.com/" // Replace with your actual base URL

    val categoryApiService: CategoryApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CategoryApiService::class.java)
    }
}
