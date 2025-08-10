package com.example.esaras.Activities

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.esaras.Adapters.ImageAdapter
import com.example.esaras.Product
import com.example.esaras.R
import com.example.esaras.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductDescriptionActivity : AppCompatActivity() {

    private val apiService by lazy { RetrofitClient.apiService }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.product_description)

        // Initialize ViewPager2 for product images
        val viewPager: ViewPager2 = findViewById(R.id.productImagesViewPager)

        // Fetch product details using product ID passed via Intent
        val productId = intent.getIntExtra("PRODUCT_ID", -1)
        if (productId != -1) {
            fetchProductDetails(productId)
        } else {
            // Handle case where productId is not provided
            Toast.makeText(this, "Invalid product ID", Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchProductDetails(id: Int) {
        apiService.getProductDetails(id).enqueue(object : Callback<Product> {
            override fun onResponse(call: Call<Product>, response: Response<Product>) {
                if (response.isSuccessful) {
                    val product = response.body()
                    if (product != null) {
                        displayProductDetails(product)
                    } else {
                        // Handle case where product details are null
                        Toast.makeText(this@ProductDescriptionActivity, "Product details are not available.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    // Handle response error
                    Toast.makeText(this@ProductDescriptionActivity, "Failed to fetch product details: ${response.errorBody()?.string()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Product>, t: Throwable) {
                // Handle failure
                Toast.makeText(this@ProductDescriptionActivity, "Error fetching product details: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun displayProductDetails(product: Product) {
        // Set up ViewPager2 adapter with product images
        val imageAdapter = ImageAdapter(product.images) // Pass image URLs to the adapter
        val viewPager: ViewPager2 = findViewById(R.id.productImagesViewPager)
        viewPager.adapter = imageAdapter

        // Set other product details to your TextViews
        findViewById<TextView>(R.id.productNameTextView).text = product.title
        findViewById<TextView>(R.id.productPriceTextView).text = "Price: ${product.price}"
        findViewById<TextView>(R.id.productDiscountTextView).text = "Discount: ${product.discountPercentage}%"
        findViewById<TextView>(R.id.productRatingTextView).text = "Rating: ${product.rating}"
        findViewById<TextView>(R.id.stockStatusTextView).text = "Stock: ${product.stock}"
        findViewById<TextView>(R.id.productDescriptionTextView).text = product.description
        findViewById<TextView>(R.id.skuTextView).text = "SKU: ${product.sku}"
        findViewById<TextView>(R.id.brandNameTextView).text = "Brand: ${product.brand}"
        findViewById<TextView>(R.id.dimensionsTextView).text = "Dimensions: ${product.dimensions.width} x ${product.dimensions.height} x ${product.dimensions.depth}"
        findViewById<TextView>(R.id.weightTextView).text = "Weight: ${product.weight}g"
        findViewById<TextView>(R.id.shippingTextView).text = "Shipping Info: ${product.shippingInformation}"
    }
}
