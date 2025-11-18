package com.example.user_management_service.exception

class BusinessException(
    message: String,
    val errorCode: String = "BUSINESS_ERROR"
) : RuntimeException(message)