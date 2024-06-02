package com.musinsa.coordinator

import com.musinsa.coordinator.dto.BrandAndPrice
import com.musinsa.coordinator.dto.BrandCollection
import com.musinsa.coordinator.dto.ErrorDTO
import com.musinsa.coordinator.dto.GetCheapestBrandCollectionDTO
import com.musinsa.coordinator.dto.GetCheapestProductByCategoryDTO
import com.musinsa.coordinator.dto.GetMinMaxPricedProductsByCategoryDTO
import com.musinsa.coordinator.dto.ProductDTO
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus
import org.springframework.test.annotation.DirtiesContext

@SpringBootTest(
    classes = [CoordinatorApplication::class],
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
class CoordinatorApplicationTests {

    @Autowired
    lateinit var restTemplate: TestRestTemplate

    @Test
    fun `구현1 - 카테고리별 최저가격 브랜드와 상품 조회`() {
        val response = restTemplate.getForEntity("/categories/products/cheapest", GetCheapestProductByCategoryDTO.Response::class.java)
        val expectedResponse = GetCheapestProductByCategoryDTO.Response(
            cheapestProductCollection = listOf(
                ProductDTO(id = 17, name = "상의", price = 10000, brandName = "C", categoryName = "Tops"),
                ProductDTO(id = 34, name = "아우터", price = 5000, brandName = "E", categoryName = "Outerwear"),
                ProductDTO(id = 27, name = "바지", price = 3000, brandName = "D", categoryName = "Pants"),
                ProductDTO(id = 4, name = "스니커즈", price = 9000, brandName = "A", categoryName = "Sneakers"),
                ProductDTO(id = 5, name = "가방", price = 2000, brandName = "A", categoryName = "Bags"),
                ProductDTO(id = 30, name = "모자", price = 1500, brandName = "D", categoryName = "Hats"),
                ProductDTO(id = 71, name = "양말", price = 1700, brandName = "I", categoryName = "Socks"),
                ProductDTO(id = 48, name = "액세서리", price = 1900, brandName = "F", categoryName = "Accessories")
            )
        )
        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(expectedResponse, response.body)
    }

    @Test
    fun `구현2 - 단일 브랜드 최저가 컬랙션 조회`() {
        val response = restTemplate.getForEntity("/brands/cheapest/products/collection", GetCheapestBrandCollectionDTO.Response::class.java)

        val expectedResponse = GetCheapestBrandCollectionDTO.Response(
            cheapestBrandCollection = BrandCollection(
                brandName = "D",
                categories = listOf(
                    BrandCollection.CategoryAndPrice(categoryName = "Accessories", price = 2000),
                    BrandCollection.CategoryAndPrice(categoryName = "Bags", price = 2500),
                    BrandCollection.CategoryAndPrice(categoryName = "Hats", price = 1500),
                    BrandCollection.CategoryAndPrice(categoryName = "Outerwear", price = 5100),
                    BrandCollection.CategoryAndPrice(categoryName = "Pants", price = 3000),
                    BrandCollection.CategoryAndPrice(categoryName = "Sneakers", price = 9500),
                    BrandCollection.CategoryAndPrice(categoryName = "Socks", price = 2400),
                    BrandCollection.CategoryAndPrice(categoryName = "Tops", price = 10100)
                ),
                totalPrice = 36100
            )
        )

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(expectedResponse, response.body)
    }

    @Test
    fun `구현3 - tops2 카테고리 최저 최대가 조회 - 없는 카테고리로 조회 시`() {
        val response = restTemplate.getForEntity("/categories/Tops2/products/pricing", ErrorDTO::class.java)
        assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
        assertEquals("카테고리 이름이 올바르지 않습니다.", response.body?.message)
    }

    @Test
    fun `구현3 - Tops 카테고리 최저 최대가 조회`() {
        val response = restTemplate.getForEntity("/categories/Tops/products/pricing", GetMinMaxPricedProductsByCategoryDTO.Response::class.java)

        val expectedResponse = GetMinMaxPricedProductsByCategoryDTO.Response(
            categoryName = "Tops",
            min = BrandAndPrice(
                brandName = "C",
                price = 10000
            ),
            max = BrandAndPrice(
                brandName = "I",
                price = 11400
            )
        )

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(expectedResponse, response.body)
    }
}
