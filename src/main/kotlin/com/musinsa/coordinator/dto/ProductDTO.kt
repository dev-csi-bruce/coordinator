package com.musinsa.coordinator.dto

import com.musinsa.coordinator.model.Brand
import com.musinsa.coordinator.model.Category
import com.musinsa.coordinator.model.Product

data class ProductDTO(
    val id: Int? = null,
    val name: String,
    val price: Int,
    val categoryName: String,
    val brandName: String
) {
    fun toEntity(brand: Brand, category: Category): Product {
        return Product(
            name = name,
            price = price,
            category = category,
            brand = brand
        )
    }
}

data class ProductCollection(
    val products: List<ProductDTO>
)
