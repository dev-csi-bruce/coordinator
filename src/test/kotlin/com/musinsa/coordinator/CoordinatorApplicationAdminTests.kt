package com.musinsa.coordinator

import com.musinsa.coordinator.dto.ProductDTO
import com.musinsa.coordinator.dto.admin.AddProductDTO
import com.musinsa.coordinator.dto.admin.BrandDTO
import com.musinsa.coordinator.dto.admin.DeleteBrandDTO
import com.musinsa.coordinator.dto.admin.DeleteProductDTO
import com.musinsa.coordinator.dto.admin.UpdateBrandDTO
import com.musinsa.coordinator.dto.admin.UpdateProductDTO
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.annotation.DirtiesContext

@SpringBootTest(
    classes = [CoordinatorApplication::class],
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class CoordinatorApplicationAdminTests {

    @Autowired
    lateinit var restTemplate: TestRestTemplate

    @Test
    @Order(1)
    fun `test add new brand`() {
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON

        val request = HttpEntity("""{"brand": {"name": "Nike"}}""", headers)
        val response = restTemplate.postForEntity("/admin/brands", request, String::class.java)
        assertEquals(HttpStatus.OK, response.statusCode)
    }

    @Test
    @Order(2)
    fun `test add new product`() {
        val request = AddProductDTO.Request(product = ProductDTO(name = "T-shirt", price = 10000, brandName = "A", categoryName = "Tops"))
        val response = restTemplate.postForObject("/admin/products", request, AddProductDTO.Response::class.java)
        val expectedResponse = AddProductDTO.Response(
            product = ProductDTO(id = response.product.id, name = "T-shirt", price = 10000, brandName = "A", categoryName = "Tops")
        )
        assertEquals(expectedResponse, response)
    }

    @Test
    @Order(3)
    fun `test update product`() {
        val request = UpdateProductDTO.Reqeust(product = ProductDTO(id = 1, name = "T-shirt", price = 10000, brandName = "B", categoryName = "Tops"))
        val response = restTemplate.patchForObject("/admin/products", request, UpdateProductDTO.Response::class.java)

        val expectedResponse = UpdateProductDTO.Response(
            product = ProductDTO(id = 1, name = "T-shirt", price = 10000, brandName = "B", categoryName = "Tops")
        )
        assertEquals(expectedResponse, response)
    }

    @Test
    @Order(4)
    fun `test update brand`() {

        val request = UpdateBrandDTO.Request(brand = BrandDTO(id = 1, name = "M"))
        val response = restTemplate.patchForObject("/admin/brands", request, UpdateBrandDTO.Response::class.java)

        val expectedResponse = UpdateBrandDTO.Response(
            brand = BrandDTO(id = 1, name = "M")
        )

        assertEquals(expectedResponse, response)
    }

    @Test
    @Order(5)
    fun `test delete product`() {
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON

        val request = HttpEntity("""{"id": 10}""", headers)
        val response = restTemplate.exchange("/admin/products", HttpMethod.DELETE, request, DeleteProductDTO.Response::class.java)
        val expectedResponseBody = DeleteProductDTO.Response(id = 10)

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(expectedResponseBody, response.body)
    }

    @Test
    @Order(6)
    fun `test delete brand`() {
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON

        val request = HttpEntity("""{"id": 5}""", headers)
        val response = restTemplate.exchange("/admin/brands", HttpMethod.DELETE, request, DeleteBrandDTO.Response::class.java)
        val expectedResponseBody = DeleteBrandDTO.Response(id = 5)

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(expectedResponseBody, response.body)
    }
}
