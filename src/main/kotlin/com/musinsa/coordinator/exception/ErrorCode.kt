package com.musinsa.coordinator.exception

import org.springframework.http.HttpStatus

enum class ErrorCode(val httpStatus: HttpStatus, val message: String) {
    // 400
    BAD_REQUEST(HttpStatus.BAD_REQUEST, ""),
    INVALID_CATEGORY_NAME(HttpStatus.BAD_REQUEST, "카테고리 이름이 올바르지 않습니다."),
    INVALID_BRAND_NAME(HttpStatus.BAD_REQUEST, "브랜드 이름이 올바르지 않습니다."),
    CHEAPEST_BRAND_COLLECTION_NOT_FOUND(HttpStatus.BAD_REQUEST, "최저가 브랜드 컬렉션을 찾을 수 없습니다."),
    PRODUCT_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "이미 등록된 상품입니다."),
    INVALID_PRODUCT_ID(HttpStatus.BAD_REQUEST, "상품 ID가 올바르지 않습니다."),
    INVALID_BRAND_ID(HttpStatus.BAD_REQUEST, "브랜드 ID가 올바르지 않습니다."),
}
