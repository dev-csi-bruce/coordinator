package com.musinsa.coordinator.dto.admin

import com.musinsa.coordinator.dto.ProductDTO

class UpdateProductDTO {
    data class Reqeust(
        val product: ProductDTO
    )

    data class Response(
        val product: ProductDTO
    )
}
