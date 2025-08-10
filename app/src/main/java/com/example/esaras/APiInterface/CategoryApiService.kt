// CategoryApiService.kt
package com.example.esaras.APiInterface

import com.example.esaras.Product
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface CategoryApiService {
    @GET("products/category-list")
    fun getCategoryList(): Call<List<String>>

    @GET("products/category/{category}")
    fun getProductsByCategory(@Path("category") category: String): Call<ProductResponse>
}

data class ProductResponse(
    val products: List<Product>,
    val total: Int,
    val skip: Int,
    val limit: Int
)
