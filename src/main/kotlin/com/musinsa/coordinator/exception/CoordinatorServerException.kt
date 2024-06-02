package com.musinsa.coordinator.exception

class CoordinatorServerException(
    val code: ErrorCode = ErrorCode.BAD_REQUEST,
    val description: String = "",
) : RuntimeException(description)
