package com.musinsa.coordinator.controller

import com.musinsa.coordinator.dto.GetCheapestBrandCollectionDTO
import com.musinsa.coordinator.dto.GetCheapestProductByCategoryDTO
import com.musinsa.coordinator.dto.GetMinMaxPricedProductsByCategoryDTO
import com.musinsa.coordinator.service.CoordinatorService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class CoordinatorController(
    private val coordinatorService: CoordinatorService
) {
    /*
        [구현1] 카테고리 별 최저가격 브랜드와 상품 가격, 총액을 조회하는 API
     */
    @GetMapping("/categories/products/cheapest")
    fun getCheapestProductByCategory(): GetCheapestProductByCategoryDTO.Response {
        return coordinatorService.getCheapestProductByCategory()
    }

    /*
        [구현2] 단일 브랜드로 모든 카테고리 상품을 구매할 때 최저가격에 판매하는 브랜드와 카테고리의 상품가격, 총액을 조회하는 API
     */
    @GetMapping("/brands/cheapest/products/collection")
    fun getCheapestBrandCollection(): GetCheapestBrandCollectionDTO.Response {
        return coordinatorService.getCheapestBrandCollection()
    }

    /*
        [구현3] 카테고리 이름으로 최저, 최고 가격 브랜드와 상품 가격을 조회하는 API
     */
    @GetMapping("/categories/{categoryName}/products/pricing")
    fun getMinMaxPricedProductsByCategory(@PathVariable categoryName: String): GetMinMaxPricedProductsByCategoryDTO.Response {
        return coordinatorService.getMinMaxPricedProductsByCategory(categoryName)
    }
}
