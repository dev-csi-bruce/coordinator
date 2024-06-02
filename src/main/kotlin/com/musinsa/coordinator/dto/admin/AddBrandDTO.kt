package com.musinsa.coordinator.dto.admin

class AddBrandDTO {
    data class Request(
        val brand: BrandDTO
    )
    data class Response(
        val brand: BrandDTO
    )
}
