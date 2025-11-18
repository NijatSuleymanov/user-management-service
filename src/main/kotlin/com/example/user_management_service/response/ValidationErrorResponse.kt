package com.example.user_management_service.response

import java.time.LocalDateTime

data class ValidationErrorResponse(
    val timestamp: LocalDateTime = LocalDateTime.now(),
    val status: Int,
    val error: String = "Validation Failed",
    val message: String,
    val path: String? = null,
    val fieldErrors: Map<String, String> = emptyMap()
)