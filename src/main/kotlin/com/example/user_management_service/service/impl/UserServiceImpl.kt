package com.example.user_management_service.service.impl

import com.example.user_management_service.dto.UserDTO
import com.example.user_management_service.mapper.UserMapper
import com.example.user_management_service.repository.UserRepository
import com.example.user_management_service.service.UserService
import org.springframework.stereotype.Service

@Service
class UserServiceImpl(private val userRepository: UserRepository) : UserService {
    override fun createUser(user: UserDTO): UserDTO {
        val user = UserMapper.toEntity(user)
        val saved = userRepository.save(user)
        return UserMapper.toDTO(saved)
    }

    override fun getUserById(id: Long): UserDTO? {
       val user = userRepository.findById(id).orElse(null) ?: return null
        return UserMapper.toDTO(user)
    }

    override fun getAllUsers(): List<UserDTO> {
        return userRepository.findAll().map { UserMapper.toDTO(it) }
    }

    override fun updateUser(id: Long, user: UserDTO): UserDTO? {
        val existingUser = userRepository.findById(id).orElse(null) ?: return null
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
        return if (userRepository.existsById(id)) {
            userRepository.deleteById(id)
            true
        }else false
    }
}