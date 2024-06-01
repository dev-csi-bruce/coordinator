package com.musinsa.coordinator.repository

import com.musinsa.coordinator.model.Category
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CategoryRepository : JpaRepository<Category, Int> {
    override fun findAll(): List<Category>
    fun findByName(categoryName: String): Category?
}
