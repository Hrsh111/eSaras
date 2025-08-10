package com.example.esaras.Activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.esaras.APiInterface.ApiService
import com.example.esaras.APiInterface.ProductResponse
import com.example.esaras.Adapters.ProductsAdapter
import com.example.esaras.Product
import com.example.esaras.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchResultsActivity : AppCompatActivity() {

    private lateinit var productsRecyclerView: RecyclerView
    private lateinit var searchView: SearchView
    private lateinit var apiService: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_results)

        productsRecyclerView = findViewById(R.id.productsRecyclerView)
        searchView = findViewById(R.id.searchView)
        productsRecyclerView.layoutManager = LinearLayoutManager(this)

        // Initialize ApiService
        apiService = Retrofit.Builder()
            .baseUrl("https://dummyjson.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)

        // Set up SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { performSearch(it) }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Optionally implement real-time search here
                return false
            }
        })

        // Get initial search query from intent
        val initialSearchQuery = intent.getStringExtra("SEARCH_QUERY") ?: ""
        searchView.setQuery(initialSearchQuery, true)
    }

    private fun performSearch(query: String) {
        apiService.searchProducts(query).enqueue(object : Callback<ProductResponse> {
            override fun onResponse(call: Call<ProductResponse>, response: Response<ProductResponse>) {
                if (response.isSuccessful) {
                    val productResponse = response.body()
                    productResponse?.let {
                        displaySearchResults(it.products)
                    }
                } else {
                    // Handle error
                    println("Search failed: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<ProductResponse>, t: Throwable) {
                // Handle failure
                println("Search error: ${t.message}")
            }
        })
    }

    private fun displaySearchResults(products: List<Product>) {
        val adapter = ProductsAdapter(products) { product ->
            val intent = Intent(this, ProductDescriptionActivity::class.java)
            intent.putExtra("PRODUCT_ID", product.id)
            startActivity(intent)
        }
        productsRecyclerView.adapter = adapter
    }
}