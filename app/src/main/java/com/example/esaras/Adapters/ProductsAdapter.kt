package com.example.esaras.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.esaras.Product
import com.example.esaras.R

class ProductsAdapter(
    private val products: List<Product>,
    private val onItemClick: (Product) -> Unit
) : RecyclerView.Adapter<ProductsAdapter.ProductViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]

        // Load image
        Glide.with(holder.itemView.context)
            .load(product.thumbnail)  // Use thumbnail for the product image
            .into(holder.productImageView)

        // Set product details
        holder.productNameTextView.text = product.title
        holder.productPriceTextView.text = "$${product.price}"
        holder.productRatingTextView.text = "Rating: ${product.rating}"

        // Handle item click
        holder.itemView.setOnClickListener {
            onItemClick(product)
        }
    }

    override fun getItemCount(): Int {
        return products.size
    }

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productImageView: ImageView = itemView.findViewById(R.id.productImage)
        val productNameTextView: TextView = itemView.findViewById(R.id.productName)
        val productPriceTextView: TextView = itemView.findViewById(R.id.productPrice)
        val productRatingTextView: TextView = itemView.findViewById(R.id.productRating)
    }
}
