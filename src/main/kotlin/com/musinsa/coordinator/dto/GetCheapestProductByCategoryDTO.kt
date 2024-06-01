package com.musinsa.coordinator.dto

class GetCheapestProductByCategoryDTO {
    data class Response(
        val cheapestProductCollection: List<ProductDTO>
    )
}
