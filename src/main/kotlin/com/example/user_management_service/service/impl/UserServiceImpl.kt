package com.example.user_management_service.service.impl

import com.example.user_management_service.dto.UserDTO
import com.example.user_management_service.exception.DuplicateResourceException
import com.example.user_management_service.exception.ResourceNotFoundException
import com.example.user_management_service.mapper.UserMapper
import com.example.user_management_service.repository.UserRepository
import com.example.user_management_service.service.UserService
import com.example.user_management_service.service.UserValidationService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val validationService: UserValidationService
) : UserService {

    private val logger = LoggerFactory.getLogger(UserServiceImpl::class.java)

    override fun createUser(user: UserDTO): UserDTO {
        logger.info("Creating new user with email: {}", user.email)
        validationService.validateUser(user)

        userRepository.findByEmail(user.email)?.let {
            logger.warn("Attempt to create duplicate user with email: {}", user.email)
            throw DuplicateResourceException(
                message = "User with email '${user.email}' already exists",
                resourceName = "User",
                fieldName = "email",
                fieldValue = user.email
            )
        }

        val entityUser = UserMapper.toEntity(user)
        val saved = userRepository.save(entityUser)
        logger.info("User created successfully with id: {}, email: {}", saved.id, saved.email)
        return UserMapper.toDTO(saved)
    }

    override fun getUserById(id: Long): UserDTO? {
        logger.debug("Fetching user by id: {}", id)
        val user = userRepository.findById(id).orElse(null) ?: throw ResourceNotFoundException(
            message = "User with id '$id' not found",
            resourceName = "User",
            fieldName = "id",
            fieldValue = id
        )
        logger.debug("User found with id: {}", id)
        return UserMapper.toDTO(user)
    }

    override fun getAllUsers(): List<UserDTO> {
        logger.debug("Fetching all users")
        val users = userRepository.findAll().map { UserMapper.toDTO(it) }
        logger.info("Retrieved {} users", users.size)
        return users
    }

    override fun updateUser(id: Long, user: UserDTO): UserDTO? {
        logger.info("Updating user with id: {}", id)
        val existingUser = userRepository.findById(id).orElse(null) ?: throw ResourceNotFoundException(
            message = "User with id '$id' not found",
            resourceName = "User",
            fieldName = "id",
            fieldValue = id
        )

        validationService.validateUser(user)

        if (existingUser.email != user.email) {
            userRepository.findByEmail(user.email)?.let {
                logger.warn("Attempt to update user with duplicate email: {}", user.email)
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
        logger.info("User updated successfully with id: {}", id)
        return UserMapper.toDTO(saved)
    }

    override fun deleteUser(id: Long): Boolean {
        logger.info("Deleting user with id: {}", id)
        if (!userRepository.existsById(id)) {
            logger.warn("Attempt to delete non-existent user with id: {}", id)
            throw ResourceNotFoundException(
                message = "User with id '$id' not found",
                resourceName = "User",
                fieldName = "id",
                fieldValue = id
            )
        }
        userRepository.deleteById(id)
        logger.info("User deleted successfully with id: {}", id)
        return true
    }
}