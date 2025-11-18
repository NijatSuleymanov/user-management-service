package com.example.user_management_service.exception

class ResourceNotFoundException(
    message: String,
    val resourceName: String,
    val fieldName: String,
    val fieldValue: Any?
) : RuntimeException(message)