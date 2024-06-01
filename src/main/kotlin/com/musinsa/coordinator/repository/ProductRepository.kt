package com.musinsa.coordinator.repository

import com.musinsa.coordinator.model.Brand
import com.musinsa.coordinator.model.Category
import com.musinsa.coordinator.model.Product
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface ProductRepository : JpaRepository<Product, Int> {
    fun findAllByCategory(category: Category): List<Product>

    @Query("SELECT p FROM Product p WHERE p.category = :category AND p.brand = :brand ORDER BY p.price ASC LIMIT 1")
    fun findCheapestProductByCategoryAndBrand(category: Category, brand: Brand): Product

    @Query("SELECT p FROM Product p WHERE p.category = :category ORDER BY p.price ASC LIMIT 1")
    fun cheapestProductByCategory(category: Category): Product
    @Query("SELECT p FROM Product p WHERE p.category = :category ORDER BY p.price DESC LIMIT 1")
    fun mostExpensiveProductByCategory(category: Category): Product
}