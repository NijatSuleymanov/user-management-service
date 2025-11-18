package com.example.user_management_service.dto

import com.example.user_management_service.model.Role
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class UserDTO(
    val id: Long? = null,

    @NotBlank(message = "Name cannot be blank")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    val name: String,

    @Email(message = "Email must be valid")
    @NotBlank(message = "Email cannot be blank")
    val email: String,

    @Size(min = 10, max = 20, message = "Phone must be between 10 and 20 characters")
    val phone: String? = null,

    val role: Role = Role.USER,
)
