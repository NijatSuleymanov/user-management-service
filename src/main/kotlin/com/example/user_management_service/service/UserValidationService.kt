package com.example.user_management_service.service

import com.example.user_management_service.dto.UserDTO

interface UserValidationService {
    fun validateUser(user: UserDTO)
    fun validateUserEmail(email: String): String
    fun validateUserName(name: String): String
    fun validateUserPhone(phone: String?): String
}