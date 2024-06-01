package com.musinsa.coordinator.dto

data class ProductDTO(
    val id: Int,
    val name: String,
    val price: Int,
    val category: String,
    val brand: String
)

data class ProductCollection(
    val products: List<ProductDTO>
)