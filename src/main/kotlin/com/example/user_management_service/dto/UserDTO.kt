package com.example.user_management_service.dto

import com.example.user_management_service.model.Role

data class UserDTO(
    val id: Long? = null,
    val name: String,
    val email: String,
    val phone: String,
    val role: Role,
)
