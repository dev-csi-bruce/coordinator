package com.musinsa.coordinator.dto.admin

class UpdateBrandDTO {
    data class Request(
        val brand: BrandDTO
    )

    data class Response(
        val brand: BrandDTO
    )
}
