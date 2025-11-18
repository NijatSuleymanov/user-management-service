package com.example.user_management_service.service

import com.example.user_management_service.entity.User

interface UserService {
    fun createUser(user: User): User
    fun getUserById(id: Long): User?
    fun getAllUsers(): List<User>
    fun updateUser(id: Long, user: User): User?
    fun deleteUser(id: Long): Boolean

}