package com.musinsa.coordinator.service

import com.musinsa.coordinator.dto.BrandCollection
import com.musinsa.coordinator.dto.GetCheapestBrandCollectionDTO
import com.musinsa.coordinator.dto.GetCheapestProductByCategoryDTO
import com.musinsa.coordinator.dto.GetMinMaxPricedProductsByCategoryDTO
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
    /*
        TODO:
            추가 고려사항: 지금은 쿼리로 상품을 다 조회해서 최저가를 찾지만, 이 방법은 성능이슈가 있을 수 있습니다.
            쿼리에서 최저값을 찾게 하는 방법도 있고, 캐싱을 이용하는 방법도 있습니다.
     */
    fun getCheapestProductByCategory(): GetCheapestProductByCategoryDTO.Response {
        val allCategories = categoryRepository.findAll().sortedBy { it.id }
        val cheapestProductsByCategory = allCategories.mapNotNull { category ->
            val products = productRepository.findAllByCategory(category)
            products.minByOrNull { it.price }
        }

        return GetCheapestProductByCategoryDTO.Response(
            cheapestProductCollection = cheapestProductsByCategory.map { it.toDto() }
        )
    }

    fun getCheapestBrandCollection(): GetCheapestBrandCollectionDTO.Response {
        val allBrands = brandRepository.findAll()
        val allCategories = categoryRepository.findAll()
        // 브랜드별 최저가 상품 컬랙션을 조회
        val brandToCheapestCollectionMap = allBrands.map { brand ->
            brand to allCategories.map { category ->
                getCheapestProductByBrandAndCategory(brand, category)
            }
        }.toMap()
        // 그 중 컬랙션 가격이 가장 낮은 브랜드를 조회
        val brandToCollectionMap = brandToCheapestCollectionMap.minByOrNull { (brand, collection) ->
            collection.sumOf { it.price }
        } ?: throw IllegalStateException("브랜드별 최저가 상품 컬랙션을 조회할 수 없습니다.")

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

    private fun getCheapestProductByBrandAndCategory(brand: Brand, category: Category): Product {
        // 브랜드, 카테고리별 최저가 상품을 조회
        return productRepository.findCheapestProductByCategoryAndBrand(category, brand)
    }

    fun getMinMaxPricedProductsByCategory(categoryName: String): GetMinMaxPricedProductsByCategoryDTO.Response {
        val category = categoryRepository.findByName(categoryName)
            ?: throw IllegalArgumentException("카테고리 이름이 잘못되었습니다.")
        val cheapestProduct = productRepository.cheapestProductByCategory(category)
        val mostExpensiveProduct = productRepository.mostExpensiveProductByCategory(category)

        return GetMinMaxPricedProductsByCategoryDTO.Response(
            categoryName = categoryName,
            min = cheapestProduct.toBrandAndPrice(),
            max = mostExpensiveProduct.toBrandAndPrice()
        )
    }
}