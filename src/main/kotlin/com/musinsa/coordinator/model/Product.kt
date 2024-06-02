package com.musinsa.coordinator.model

import com.musinsa.coordinator.dto.BrandAndPrice
import com.musinsa.coordinator.dto.BrandCollection
import com.musinsa.coordinator.dto.ProductDTO
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne

@Entity
class Product(
    category: Category,
    name: String,
    brand: Brand,
    price: Int
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int = 0
    var name: String = name
        protected set
    @ManyToOne(fetch = FetchType.LAZY)
    var category: Category = category
        protected set
    @ManyToOne(fetch = FetchType.LAZY)
    var brand: Brand = brand
        protected set
    var price: Int = price
        protected set

    fun update(
        name: String? = null,
        price: Int? = null,
        brand: Brand? = null,
        category: Category? = null
    ): Product {
        name?.let { this.name = it }
        price?.let { this.price = it }
        brand?.let { this.brand = it }
        category?.let { this.category = it }

        return this
    }

    fun toDto(): ProductDTO {
        return ProductDTO(
            id = id,
            name = name,
            categoryName = category.name,
            brandName = brand.name,
            price = price
        )
    }

    fun toCategoryAndPrice(): BrandCollection.CategoryAndPrice {
        return BrandCollection.CategoryAndPrice(
            categoryName = category.name,
            price = price
        )
    }

    fun toBrandAndPrice(): BrandAndPrice {
        return BrandAndPrice(
            brandName = brand.name,
            price = price
        )
    }
}
