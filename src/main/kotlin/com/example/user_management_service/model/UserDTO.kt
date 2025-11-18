package com.example.user_management_service.model

import jakarta.persistence.*

@Entity
@Table(name = "users")
data class UserDTO(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    val name: String,

    @Column(nullable = false, unique = true)
    val email: String,

    val phone: String? = null,

    @Enumerated(EnumType.STRING)
    val role: Role? = Role.USER
)
