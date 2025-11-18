package com.example.user_management_service.controller

import com.example.user_management_service.dto.UserDTO
import com.example.user_management_service.response.ApiResponse
import com.example.user_management_service.service.UserService
import jakarta.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/users")
class UserController(private val userService: UserService) {

    private val logger = LoggerFactory.getLogger(UserController::class.java)

    @PostMapping
    fun createUser(@Valid @RequestBody dto: UserDTO): ResponseEntity<ApiResponse<UserDTO>> {
        logger.info("POST /api/users - Creating new user with email: {}", dto.email)
        val createdUser = userService.createUser(dto)
        val response = ApiResponse(
            status = HttpStatus.CREATED.value(),
            message = "User created successfully",
            data = createdUser
        )
        logger.info("POST /api/users - User created successfully with id: {}", createdUser.id)
        return ResponseEntity(response, HttpStatus.CREATED)
    }

    @GetMapping
    fun getAllUsers(): ResponseEntity<ApiResponse<List<UserDTO>>> {
        logger.info("GET /api/users - Fetching all users")
        val users = userService.getAllUsers()
        val response = ApiResponse(
            status = HttpStatus.OK.value(),
            message = "Users retrieved successfully",
            data = users
        )
        logger.info("GET /api/users - Retrieved {} users", users.size)
        return ResponseEntity.ok(response)
    }

    @GetMapping("/{id}")
    fun getUserById(@PathVariable id: Long): ResponseEntity<ApiResponse<UserDTO>> {
        logger.info("GET /api/users/{} - Fetching user by id", id)
        val user = userService.getUserById(id)
        val response = ApiResponse(
            status = HttpStatus.OK.value(),
            message = "User retrieved successfully",
            data = user
        )
        logger.info("GET /api/users/{} - User retrieved successfully", id)
        return ResponseEntity.ok(response)
    }

    @PutMapping("/{id}")
    fun updateUser(@PathVariable id: Long, @Valid @RequestBody dto: UserDTO): ResponseEntity<ApiResponse<UserDTO>> {
        logger.info("PUT /api/users/{} - Updating user with email: {}", id, dto.email)
        val updatedUser = userService.updateUser(id, dto)
        val response = ApiResponse(
            status = HttpStatus.OK.value(),
            message = "User updated successfully",
            data = updatedUser
        )
        logger.info("PUT /api/users/{} - User updated successfully", id)
        return ResponseEntity.ok(response)
    }

    @DeleteMapping("/{id}")
    fun deleteUser(@PathVariable id: Long): ResponseEntity<ApiResponse<String>> {
        logger.info("DELETE /api/users/{} - Deleting user", id)
        userService.deleteUser(id)
        val response = ApiResponse(
            status = HttpStatus.NO_CONTENT.value(),
            message = "User deleted successfully",
            data = "User with id $id has been deleted"
        )
        logger.info("DELETE /api/users/{} - User deleted successfully", id)
        return ResponseEntity(response, HttpStatus.NO_CONTENT)
    }
}