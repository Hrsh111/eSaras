package com.example.esaras.Activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.esaras.Adapters.ProductsAdapter
import com.example.esaras.APiInterface.CategoryRetrofitClient
import com.example.esaras.APiInterface.ProductResponse
import com.example.esaras.Product
import com.example.esaras.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoryProductsActivity : AppCompatActivity() {

    private lateinit var categoryRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_products)

        categoryRecyclerView = findViewById(R.id.categoryRecyclerView)

        val category = intent.getStringExtra("CATEGORY")
        if (category != null) {
            fetchProductsByCategory(category)
        }
    }

    private fun fetchProductsByCategory(category: String) {
        CategoryRetrofitClient.categoryApiService.getProductsByCategory(category).enqueue(object : Callback<ProductResponse> {
            override fun onResponse(call: Call<ProductResponse>, response: Response<ProductResponse>) {
                if (response.isSuccessful) {
                    val products = response.body()?.products
                    if (products != null) {
                        displayProducts(products)
                    }
                }
            }

            override fun onFailure(call: Call<ProductResponse>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    private fun displayProducts(products: List<Product>) {
        val adapter = ProductsAdapter(products) { product ->
            val intent = Intent(this, ProductDescriptionActivity::class.java)
            intent.putExtra("PRODUCT_ID", product.id)
            startActivity(intent)
        }
        categoryRecyclerView.adapter = adapter
        categoryRecyclerView.layoutManager = GridLayoutManager(this, 2)
    }
}
