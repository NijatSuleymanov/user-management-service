package com.example.user_management_service.mapper

import com.example.user_management_service.dto.UserDTO
import com.example.user_management_service.model.Role

object UserMapper {

    fun toDTO(user: com.example.user_management_service.model.UserDTO): UserDTO = UserDTO(
        id = user.id,
        name = user.name,
        email = user.email,
        phone = user.phone ?: "",
        role = user.role ?: Role.USER
    )

    fun toEntity(dto: UserDTO): com.example.user_management_service.model.UserDTO = com.example.user_management_service.model.UserDTO(
        name = dto.name,
        email = dto.email,
        phone = dto.phone,
        role = dto.role
    )
}
