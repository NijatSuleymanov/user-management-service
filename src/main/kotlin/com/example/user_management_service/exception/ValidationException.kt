package com.example.user_management_service.exception

class ValidationException(
    message: String,
    val fieldErrors: Map<String, String> = emptyMap()
) : RuntimeException(message)