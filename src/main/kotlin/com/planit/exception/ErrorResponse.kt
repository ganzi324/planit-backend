package com.planit.exception

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * 에러 응답 DTO
 */
data class ErrorResponse(
    val timestamp: String,
    val status: Int,
    val error: String,
    val message: String,
    val errorCode: String,
    val path: String
) {
    companion object {
        fun of(
            status: Int,
            error: String,
            message: String,
            errorCode: String,
            path: String
        ): ErrorResponse {
            return ErrorResponse(
                timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                status = status,
                error = error,
                message = message,
                errorCode = errorCode,
                path = path
            )
        }
    }
} 