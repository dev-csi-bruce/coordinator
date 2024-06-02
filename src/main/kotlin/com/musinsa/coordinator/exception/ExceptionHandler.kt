package com.musinsa.coordinator.exception

import com.musinsa.coordinator.dto.ErrorDTO
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.method.HandlerMethod

@ControllerAdvice(value = ["com.musinsa.coordinator"])
class ExceptionHandler {
    private val logger = LoggerFactory.getLogger(this::class.java)
    @ExceptionHandler(CoordinatorServerException::class)
    fun handleSocarAdsServerException(exception: CoordinatorServerException, handlerMethod: HandlerMethod): ResponseEntity<ErrorDTO> {
        val message = if (exception.code.message.isNotBlank()) {
            exception.code.message
        } else {
            exception.description.takeIf { it.isNotBlank() } ?: ""
        }

        val error = ErrorDTO(
            message,
            exception.code.name,
        )
        return ResponseEntity.status(exception.code.httpStatus).body(error)
    }

    @ExceptionHandler(Throwable::class)
    fun handleException(exception: Throwable, handlerMethod: HandlerMethod): ResponseEntity<ErrorDTO> {
        logger.error("Internal server error", exception)
        val error = ErrorDTO(
            exception.message ?: "",
            "INTERNAL_SERVER_ERROR",
        )
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error)
    }
}
