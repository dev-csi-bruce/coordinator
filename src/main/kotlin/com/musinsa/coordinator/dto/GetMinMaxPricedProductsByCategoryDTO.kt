package com.musinsa.coordinator.dto

class GetMinMaxPricedProductsByCategoryDTO {
    data class Response(
        val categoryName: String,
        val min: BrandAndPrice,
        val max: BrandAndPrice
    )
}

data class BrandAndPrice(
    val brandName: String,
    val price: Int
)
