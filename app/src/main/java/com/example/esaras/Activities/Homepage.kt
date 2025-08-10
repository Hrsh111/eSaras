package com.example.esaras.Activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.esaras.APiInterface.ApiService
import com.example.esaras.Adapters.BannerAdapter
import com.example.esaras.Adapters.ProductsAdapter
import com.example.esaras.Data.ProductsData
import com.example.esaras.Product
import com.example.esaras.R
import com.example.esaras.databinding.ActivityHomepageBinding
import com.example.esaras.databinding.NavHeaderBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import com.google.android.material.navigation.NavigationView

class Homepage : AppCompatActivity() {
    private lateinit var productsRecyclerView: RecyclerView
    private lateinit var bannerViewPager: ViewPager2
    private lateinit var apiService: ApiService
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var drawerToggleButton: ImageButton
    private lateinit var searchButton: ImageButton
    private lateinit var accountContainer: LinearLayout
    private lateinit var navHeaderBinding: NavHeaderBinding
    private lateinit var binding: ActivityHomepageBinding
    private lateinit var navigationView: NavigationView


    private var currentPage = 0
    private var timer: Timer? = null
    private val DELAY_MS: Long = 500 // Delay before the task starts
    private val PERIOD_MS: Long = 3000 // Interval between tasks

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homepage)

        navigationView = findViewById(R.id.navigationView)

        // Inflate the header view
        val headerView = navigationView.getHeaderView(0)

        // Find views in the header layout
        val accountIcon = headerView.findViewById<ImageView>(R.id.accountIcon)
        val signInText = headerView.findViewById<TextView>(R.id.signInText)
        val accountText = headerView.findViewById<TextView>(R.id.accountText)
        val arrowButton = headerView.findViewById<ImageButton>(R.id.arrowButton)

        // Set click listeners
        accountIcon.setOnClickListener {
            // Handle account icon click
            // Example: Show a Toast message
            Toast.makeText(this, "Account Icon Clicked", Toast.LENGTH_SHORT).show()
        }

        signInText.setOnClickListener {
            // Handle sign in text click
            // Example: Start a new Activity
            startActivity(Intent(this, SignIn::class.java))
        }

        accountText.setOnClickListener {
            // Handle account text click
            // Example: Show a Toast message
            Toast.makeText(this, "Account Text Clicked", Toast.LENGTH_SHORT).show()
        }

        arrowButton.setOnClickListener {
            // Handle arrow button click
            // Example: Start a new Activity
            startActivity(Intent(this, SignIn::class.java))
        }

//
        // Make the status bar transparent
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.setSystemBarsBehavior(
                WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            )
            window.insetsController?.hide(WindowInsets.Type.statusBars())
            window.statusBarColor = android.graphics.Color.TRANSPARENT
        } else {
            window.statusBarColor = android.graphics.Color.TRANSPARENT
        }

        // Initialize views
        try {
            Log.d("Homepage", "Initializing views")
            drawerLayout = findViewById(R.id.drawerLayout)
            drawerToggleButton = findViewById(R.id.drawerToggleButton)
            productsRecyclerView = findViewById(R.id.productsRecyclerView)
            bannerViewPager = findViewById(R.id.bannerViewPager)
            searchButton = findViewById(R.id.searchButton)
            accountContainer = findViewById(R.id.accountContainer)
            Log.d("Homepage", "Views initialized successfully")
        } catch (e: Exception) {
            Log.e("Homepage", "Error initializing views", e)
            throw e
        }

        // Set click listener for the account container
        accountContainer.setOnClickListener {
            val intent = Intent(this, SignIn::class.java)
            startActivity(intent)
        }

        // Set the drawer toggle button listener
        drawerToggleButton.setOnClickListener {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                drawerLayout.openDrawer(GravityCompat.START)
            }
        }

        // Set click listener for the account container
        accountContainer.setOnClickListener {
            startActivity(Intent(this, SignIn::class.java))
        }

        // Initialize ApiService using Retrofit
        apiService = Retrofit.Builder()
            .baseUrl("https://dummyjson.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)

        // Fetch products from API
        fetchProducts()

        // Set click listener for the search button
        searchButton.setOnClickListener {
            // Handle the search button click
            val searchQuery = searchButton.tag as? String ?: ""
            performSearch(searchQuery)
        }

        // Set click listener for category buttons
        findViewById<ImageButton>(R.id.menCategoryButton)?.setOnClickListener {
            startCategoryActivity("mens-shirts")
        }
        findViewById<ImageButton>(R.id.womenCategoryButton)?.setOnClickListener {
            startCategoryActivity("womens-dresses")
        }
        findViewById<ImageButton>(R.id.foodsCategoryButton)?.setOnClickListener {
            startCategoryActivity("groceries")
        }
        findViewById<ImageButton>(R.id.personalCategoryButton)?.setOnClickListener {
            startCategoryActivity("personal-care")
        }

        // Set up the banner with local images
        setupLocalBanner()
    }

    private fun fetchProducts() {
        apiService.getProducts().enqueue(object : Callback<ProductsData> {
            override fun onResponse(call: Call<ProductsData>, response: Response<ProductsData>) {
                if (response.isSuccessful) {
                    val products = response.body()?.products
                    if (products.isNullOrEmpty()) {
                        // Handle empty or null data
                        println("No products available.")
                        return
                    }
                    // Log the products to see if the data is correct
                    products.forEach { product ->
                        println("Product: ${product.title}, Images: ${product.images}")
                    }
                    displayProducts(products)
                } else {
                    // Handle response error
                    println("Failed to fetch products: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<ProductsData>, t: Throwable) {
                // Handle failure
                println("Error fetching products: ${t.message}")
                t.printStackTrace()
            }
        })
    }

    private fun performSearch(query: String) {
        val intent = Intent(this, SearchResultsActivity::class.java)
        intent.putExtra("SEARCH_QUERY", query)
        startActivity(intent)
    }

    private fun displayProducts(products: List<Product>) {
        // Set up RecyclerView adapter
        val adapter = ProductsAdapter(products) { product ->
            val intent = Intent(this, ProductDescriptionActivity::class.java)
            intent.putExtra("PRODUCT_ID", product.id)
            startActivity(intent)
        }
        productsRecyclerView.adapter = adapter
        productsRecyclerView.layoutManager = GridLayoutManager(this, 2)
    }

    private fun setupLocalBanner() {
        // Use local images for the banner
        val banners = listOf(
            R.drawable.b1,
            R.drawable.b,
            R.drawable.b3,
            R.drawable.b4
        )
        val bannerAdapter = BannerAdapter(banners)
        bannerViewPager.adapter = bannerAdapter

        // Auto start banner slide
        val handler = Handler(Looper.getMainLooper())
        val update = Runnable {
            if (currentPage == banners.size) {
                currentPage = 0
            }
            bannerViewPager.setCurrentItem(currentPage++, true)
        }

        timer = Timer()
        timer?.schedule(object : TimerTask() {
            override fun run() {
                handler.post(update)
            }
        }, DELAY_MS, PERIOD_MS)
    }

    private fun startCategoryActivity(category: String) {
        val intent = Intent(this, CategoryProductsActivity::class.java)
        intent.putExtra("CATEGORY", category)
        startActivity(intent)
    }

    override fun onDestroy() {
        timer?.cancel()
        timer?.purge()
        super.onDestroy()
    }
}
