package com.musinsa.coordinator.dto.admin

import com.musinsa.coordinator.model.Brand

data class BrandDTO(
    val id: Int? = null,
    val name: String,
) {
    fun toEntity(): Brand {
        return Brand(
            name = name
        )
    }
}
