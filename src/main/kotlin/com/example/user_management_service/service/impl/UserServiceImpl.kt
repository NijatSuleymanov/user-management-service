package com.example.user_management_service.service.impl

import com.example.user_management_service.dto.UserDTO
import com.example.user_management_service.exception.DuplicateResourceException
import com.example.user_management_service.exception.ResourceNotFoundException
import com.example.user_management_service.mapper.UserMapper
import com.example.user_management_service.repository.UserRepository
import com.example.user_management_service.service.UserService
import com.example.user_management_service.service.UserValidationService
import org.springframework.stereotype.Service

@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val validationService: UserValidationService
) : UserService {

    override fun createUser(user: UserDTO): UserDTO {
        validationService.validateUser(user)

        userRepository.findByEmail(user.email)?.let {
            throw DuplicateResourceException(
                message = "User with email '${user.email}' already exists",
                resourceName = "User",
                fieldName = "email",
                fieldValue = user.email
            )
        }

        val entityUser = UserMapper.toEntity(user)
        val saved = userRepository.save(entityUser)
        return UserMapper.toDTO(saved)
    }

    override fun getUserById(id: Long): UserDTO? {
        val user = userRepository.findById(id).orElse(null) ?: throw ResourceNotFoundException(
            message = "User with id '$id' not found",
            resourceName = "User",
            fieldName = "id",
            fieldValue = id
        )
        return UserMapper.toDTO(user)
    }

    override fun getAllUsers(): List<UserDTO> {
        return userRepository.findAll().map { UserMapper.toDTO(it) }
    }

    override fun updateUser(id: Long, user: UserDTO): UserDTO? {
        val existingUser = userRepository.findById(id).orElse(null) ?: throw ResourceNotFoundException(
            message = "User with id '$id' not found",
            resourceName = "User",
            fieldName = "id",
            fieldValue = id
        )

        validationService.validateUser(user)

        if (existingUser.email != user.email) {
            userRepository.findByEmail(user.email)?.let {
                throw DuplicateResourceException(
                    message = "User with email '${user.email}' already exists",
                    resourceName = "User",
                    fieldName = "email",
                    fieldValue = user.email
                )
            }
        }

        val updatedUser = existingUser.copy(
            name = user.name,
            email = user.email,
            phone = user.phone,
            role = user.role
        )
        val saved = userRepository.save(updatedUser)
        return UserMapper.toDTO(saved)
    }

    override fun deleteUser(id: Long): Boolean {
        if (!userRepository.existsById(id)) {
            throw ResourceNotFoundException(
                message = "User with id '$id' not found",
                resourceName = "User",
                fieldName = "id",
                fieldValue = id
            )
        }
        userRepository.deleteById(id)
        return true
    }
}