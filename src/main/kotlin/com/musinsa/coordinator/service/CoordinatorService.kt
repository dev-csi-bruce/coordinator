package com.musinsa.coordinator.service

import com.musinsa.coordinator.dto.BrandCollection
import com.musinsa.coordinator.dto.GetCheapestBrandCollectionDTO
import com.musinsa.coordinator.dto.GetCheapestProductByCategoryDTO
import com.musinsa.coordinator.dto.GetMinMaxPricedProductsByCategoryDTO
import com.musinsa.coordinator.exception.CoordinatorServerException
import com.musinsa.coordinator.exception.ErrorCode
import com.musinsa.coordinator.model.Brand
import com.musinsa.coordinator.model.Category
import com.musinsa.coordinator.model.Product
import com.musinsa.coordinator.repository.BrandRepository
import com.musinsa.coordinator.repository.CategoryRepository
import com.musinsa.coordinator.repository.ProductRepository
import org.springframework.stereotype.Service

@Service
class CoordinatorService(
    private val categoryRepository: CategoryRepository,
    private val productRepository: ProductRepository,
    private val brandRepository: BrandRepository
) {
    fun getCheapestProductByCategory(): GetCheapestProductByCategoryDTO.Response {
        val allCategories = categoryRepository.findAll().sortedBy { it.id }
        val cheapestProductsByCategory = allCategories.mapNotNull { category ->
            val cheapestProduct = productRepository.cheapestProductByCategory(category)
            cheapestProduct
        }

        return GetCheapestProductByCategoryDTO.Response(
            cheapestProductCollection = cheapestProductsByCategory.map { it.toDto() }
        )
    }

    /*
         고려사항: 브랜드와 카테고리가 많아질 수록 성능이 저하될 수 있음. 성능 저하로 운영에 문제가 있을 시 아래 방법중 하나로 해결 가능.
         1. 브랜드별 컬랙션 최저가 정보를 일정 주기 배치로 관리
         2. 브랜드별 컬랙션 최저가를 캐싱
     */
    fun getCheapestBrandCollection(): GetCheapestBrandCollectionDTO.Response {
        val allBrands = brandRepository.findAll()
        val allCategories = categoryRepository.findAll()
        // 브랜드별 최저가 상품 컬랙션을 조회
        val brandToCheapestCollectionMap = allBrands.map { brand ->
            brand to allCategories.mapNotNull { category ->
                getCheapestProductByBrandAndCategory(brand, category)
            }
        }.toMap()
        // 그 중 컬랙션 가격이 가장 낮은 브랜드를 조회
        val brandToCollectionMap = brandToCheapestCollectionMap.minByOrNull { (_, collection) ->
            collection.sumOf { it.price }
        } ?: throw CoordinatorServerException(ErrorCode.CHEAPEST_BRAND_COLLECTION_NOT_FOUND)

        // 결과 출력
        return GetCheapestBrandCollectionDTO.Response(
            cheapestBrandCollection = BrandCollection(
                brandName = brandToCollectionMap.key.name,
                categories = brandToCollectionMap.value.map {
                    it.toCategoryAndPrice()
                },
                totalPrice = brandToCollectionMap.value.sumOf { it.price }
            )
        )
    }

    private fun getCheapestProductByBrandAndCategory(brand: Brand, category: Category): Product? {
        // 브랜드, 카테고리별 최저가 상품을 조회
        return productRepository.findCheapestProductByCategoryAndBrand(category, brand)
    }

    fun getMinMaxPricedProductsByCategory(categoryName: String): GetMinMaxPricedProductsByCategoryDTO.Response {
        val category = categoryRepository.findByName(categoryName)
            ?: throw CoordinatorServerException(ErrorCode.INVALID_CATEGORY_NAME)
        val cheapestProduct = productRepository.cheapestProductByCategory(category)
        val mostExpensiveProduct = productRepository.mostExpensiveProductByCategory(category)

        return GetMinMaxPricedProductsByCategoryDTO.Response(
            categoryName = categoryName,
            min = cheapestProduct.toBrandAndPrice(),
            max = mostExpensiveProduct.toBrandAndPrice()
        )
    }
}
