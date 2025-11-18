package com.example.user_management_service.service.impl

import com.example.user_management_service.entity.User
import com.example.user_management_service.repository.UserRepository
import com.example.user_management_service.service.UserService
import org.springframework.stereotype.Service

@Service
class UserServiceImpl(private val userRepository: UserRepository) : UserService {
    override fun createUser(user: User): User {
        return userRepository.save(user)
    }

    override fun getUserById(id: Long): User? {
        return userRepository.findById(id).orElse(null)
    }

    override fun getAllUsers(): List<User> {
        return userRepository.findAll()
    }

    override fun updateUser(
        id: Long,
        user: User
    ): User? {
        val existingUser = userRepository.findById(id).orElse(null) ?: return null
        val updatedUser = existingUser.copy(
            name = user.name,
            email = user.email,
            phone = user.phone,
            role = user.role
        )
        return userRepository.save(updatedUser)

    }

    override fun deleteUser(id: Long): Boolean {
        return if (userRepository.existsById(id)) {
            userRepository.deleteById(id)
            true
        }else false
    }
}