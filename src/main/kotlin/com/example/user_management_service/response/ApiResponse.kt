package com.example.user_management_service.response

import java.time.LocalDateTime

data class ApiResponse<T>(
    val timestamp: LocalDateTime = LocalDateTime.now(),
    val status: Int,
    val message: String,
    val data: T? = null,
    val error: String? = null
)