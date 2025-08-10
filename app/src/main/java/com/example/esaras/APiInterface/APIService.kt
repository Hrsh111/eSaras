package com.example.esaras.APiInterface

import com.example.esaras.Data.ProductsData
import com.example.esaras.Product
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface ApiService {
    @GET("products")
    fun getProducts(): Call<ProductsData>

    @GET("products/{id}")
    fun getProductDetails(@Path("id") id: Int): Call<Product>

    @GET("products/category-list")
    fun getCategoryList(): Call<List<String>>

    @GET("products/category/{category}")
    fun getProductsByCategory(@Path("category") category: String): Call<ProductsData>

    @GET("products/search")
    fun searchProducts(@Query("q") query: String): Call<ProductResponse>
}
