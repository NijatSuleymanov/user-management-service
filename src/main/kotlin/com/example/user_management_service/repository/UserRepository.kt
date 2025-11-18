package com.example.user_management_service.repository

import com.example.user_management_service.model.UserDTO
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<UserDTO, Long> {
    fun findByEmail(email: String): UserDTO?
}