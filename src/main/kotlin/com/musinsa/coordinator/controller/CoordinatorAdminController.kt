package com.musinsa.coordinator.controller

import com.musinsa.coordinator.dto.admin.AddBrandDTO
import com.musinsa.coordinator.dto.admin.AddProductDTO
import com.musinsa.coordinator.dto.admin.DeleteBrandDTO
import com.musinsa.coordinator.dto.admin.DeleteProductDTO
import com.musinsa.coordinator.dto.admin.UpdateBrandDTO
import com.musinsa.coordinator.dto.admin.UpdateProductDTO
import com.musinsa.coordinator.service.CoordinatorAdminService
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/admin")
class CoordinatorAdminController(
    private val coordinatorAdminService: CoordinatorAdminService
) {
    /*
        [구현4] 브랜드 및 상품을 추가 / 업데이트 / 삭제하는 API
     */
    @PostMapping("/products")
    fun addProduct(@RequestBody request: AddProductDTO.Request): AddProductDTO.Response {
        return coordinatorAdminService.addProduct(request.product)
    }

    @PostMapping("/brands")
    fun addBrand(@RequestBody request: AddBrandDTO.Request): AddBrandDTO.Response {
        return coordinatorAdminService.addBrand(request.brand)
    }

    @PatchMapping("/products")
    fun updateProduct(@RequestBody request: UpdateProductDTO.Reqeust): UpdateProductDTO.Response {
        return coordinatorAdminService.updateProduct(request.product)
    }

    @PatchMapping("/brands")
    fun updateBrand(@RequestBody request: UpdateBrandDTO.Request): UpdateBrandDTO.Response {
        return coordinatorAdminService.updateBrand(request.brand)
    }

    @DeleteMapping("/products")
    fun deleteProduct(@RequestBody request: DeleteProductDTO.Request): DeleteProductDTO.Response {
        return coordinatorAdminService.deleteProduct(request.id)
    }

    @DeleteMapping("/brands")
    fun deleteBrand(@RequestBody request: DeleteBrandDTO.Request): DeleteBrandDTO.Response {
        return coordinatorAdminService.deleteBrand(request.id)
    }
}
