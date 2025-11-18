package com.example.user_management_service.service

import com.example.user_management_service.dto.UserDTO

interface UserService {
    fun createUser(user: UserDTO): UserDTO
    fun getUserById(id: Long): UserDTO?
    fun getAllUsers(): List<UserDTO>
    fun updateUser(id: Long, user: UserDTO): UserDTO?
    fun deleteUser(id: Long): Boolean

}