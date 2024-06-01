package com.musinsa.coordinator.repository

import com.musinsa.coordinator.model.Brand
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface BrandRepository : JpaRepository<Brand, Int> {
    override fun findAll(): List<Brand>
}
