package com.example.esaras.Data

import com.example.esaras.Product

data class ProductsData(
    val products: List<Product>,
    val total: Int,
    val skip: Int,
    val limit: Int

)
