package com.musinsa.coordinator.dto.admin

import com.musinsa.coordinator.dto.ProductDTO

class AddProductDTO {
    data class Request(
        val product: ProductDTO
    )

    data class Response(
        val product: ProductDTO
    )
}
