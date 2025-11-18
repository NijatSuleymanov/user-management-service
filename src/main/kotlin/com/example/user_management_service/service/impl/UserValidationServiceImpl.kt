package com.example.user_management_service.service.impl

import com.example.user_management_service.dto.UserDTO
import com.example.user_management_service.exception.ValidationException
import com.example.user_management_service.service.UserValidationService
import org.springframework.stereotype.Service

@Service
class UserValidationServiceImpl : UserValidationService {

    override fun validateUser(user: UserDTO) {
        val errors = mutableMapOf<String, String>()

        val nameError = validateUserName(user.name)
        if (nameError.isNotEmpty()) {
            errors["name"] = nameError
        }

        val emailError = validateUserEmail(user.email)
        if (emailError.isNotEmpty()) {
            errors["email"] = emailError
        }

        val phoneError = validateUserPhone(user.phone)
        if (phoneError.isNotEmpty()) {
            errors["phone"] = phoneError
        }

        if (errors.isNotEmpty()) {
            throw ValidationException(
                message = "User validation failed",
                fieldErrors = errors
            )
        }
    }

    override fun validateUserName(name: String): String {
        return when {
            name.isBlank() -> "Name cannot be blank"
            name.length < 2 -> "Name must be at least 2 characters"
            name.length > 100 -> "Name cannot exceed 100 characters"
            else -> ""
        }
    }

    override fun validateUserEmail(email: String): String {
        return when {
            email.isBlank() -> "Email cannot be blank"
            !isValidEmail(email) -> "Email must be valid"
            else -> ""
        }
    }

    override fun validateUserPhone(phone: String?): String {
        if (phone.isNullOrBlank()) {
            return ""
        }

        return when {
            phone.length < 10 -> "Phone must be at least 10 characters"
            phone.length > 20 -> "Phone cannot exceed 20 characters"
            else -> ""
        }
    }

    private fun isValidEmail(email: String): Boolean {
        val emailRegex = Regex(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
        )
        return emailRegex.matches(email)
    }
}