package com.musinsa.coordinator.dto

class GetCheapestBrandCollectionDTO {
    data class Response(
        val cheapestBrandCollection: BrandCollection
    )
}

data class BrandCollection(
    val brandName: String,
    val categories: List<CategoryAndPrice>,
    val totalPrice: Int
) {
    data class CategoryAndPrice(
        val categoryName: String,
        val price: Int
    )
}