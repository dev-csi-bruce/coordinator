package com.musinsa.coordinator.service

import com.musinsa.coordinator.dto.ProductDTO
import com.musinsa.coordinator.dto.admin.AddBrandDTO
import com.musinsa.coordinator.dto.admin.AddProductDTO
import com.musinsa.coordinator.dto.admin.BrandDTO
import com.musinsa.coordinator.dto.admin.DeleteBrandDTO
import com.musinsa.coordinator.dto.admin.DeleteProductDTO
import com.musinsa.coordinator.dto.admin.UpdateBrandDTO
import com.musinsa.coordinator.dto.admin.UpdateProductDTO
import com.musinsa.coordinator.exception.CoordinatorServerException
import com.musinsa.coordinator.exception.ErrorCode
import com.musinsa.coordinator.model.BrandState
import com.musinsa.coordinator.repository.BrandRepository
import com.musinsa.coordinator.repository.CategoryRepository
import com.musinsa.coordinator.repository.ProductRepository
import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class CoordinatorAdminService(
    private val productRepository: ProductRepository,
    private val brandRepository: BrandRepository,
    private val categoryRepository: CategoryRepository
) {
    @Transactional
    fun addProduct(product: ProductDTO): AddProductDTO.Response {
        if (product.id != null && product.id < 0) throw CoordinatorServerException(ErrorCode.INVALID_PRODUCT_ID)

        val brand = brandRepository.findByName(product.brandName) ?: throw CoordinatorServerException(ErrorCode.INVALID_BRAND_NAME)
        val category = categoryRepository.findByName(product.categoryName) ?: throw CoordinatorServerException(ErrorCode.INVALID_CATEGORY_NAME)

        val newProduct = productRepository.save(product.toEntity(brand, category))
        return AddProductDTO.Response(newProduct.toDto())
    }

    @Transactional
    fun addBrand(brand: BrandDTO): AddBrandDTO.Response {
        if (brand.id != null && brand.id < 0) throw CoordinatorServerException(ErrorCode.INVALID_BRAND_ID)

        val newBrand = brandRepository.save(brand.toEntity())
        return AddBrandDTO.Response(newBrand.toDto())
    }

    @Transactional
    fun updateProduct(product: ProductDTO): UpdateProductDTO.Response {
        // 대상이 있는지 확인 (상품, 카테고리, 브랜드)
        val targetProduct = productRepository.findByIdOrNull(product.id!!) ?: throw CoordinatorServerException(ErrorCode.INVALID_PRODUCT_ID)
        val brand = brandRepository.findByName(product.brandName) ?: throw CoordinatorServerException(ErrorCode.INVALID_BRAND_NAME)
        val category = categoryRepository.findByName(product.categoryName) ?: throw CoordinatorServerException(ErrorCode.INVALID_CATEGORY_NAME)

        // 업데이트
        val updatedProduct = targetProduct.update(
            name = product.name,
            price = product.price,
            category = category,
            brand = brand
        )

        return UpdateProductDTO.Response(updatedProduct.toDto())
    }

    @Transactional
    fun deleteProduct(id: Int): DeleteProductDTO.Response {
        val targetProduct = productRepository.findByIdOrNull(id) ?: throw CoordinatorServerException(ErrorCode.INVALID_PRODUCT_ID)
        productRepository.delete(targetProduct)

        return DeleteProductDTO.Response(id)
    }

    @Transactional
    fun updateBrand(brand: BrandDTO): UpdateBrandDTO.Response {
        val targetBrand = brandRepository.findByIdOrNull(brand.id!!) ?: throw CoordinatorServerException(ErrorCode.INVALID_BRAND_ID)
        val updatedBrand = targetBrand.updateName(brand.name)

        return UpdateBrandDTO.Response(updatedBrand.toDto())
    }

    /*
        NOTE: 브랜드 삭제는 연결된 상품들 처리가 필요해 상태를 inactive로 변경하는 것으로 처리합니다.
        브랜드 삭제 시 연결된 상품 처리 정책이 정해지면 추가 구현이 필요.
     */
    @Transactional
    fun deleteBrand(id: Int): DeleteBrandDTO.Response {
        val targetBrand = brandRepository.findByIdOrNull(id) ?: throw CoordinatorServerException(ErrorCode.INVALID_BRAND_ID)

        targetBrand.updateState(BrandState.INACTIVE)

        return DeleteBrandDTO.Response(id)
    }
}
