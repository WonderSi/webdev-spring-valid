package com.example.lab5.web.dto

import com.example.lab5.domain.model.User

data class UserCreateRequest(
    val email: String,
    val firstName: String,
    val lastName: String,
    val isActive: Boolean = true
)

data class UserUpdateRequest(
    val email: String,
    val firstName: String,
    val lastName: String,
    val isActive: Boolean
)

data class UserResponse(
    val id: Long,
    val email: String,
    val firstName: String,
    val lastName: String,
    val isActive: Boolean
)

fun User.toResponse() = UserResponse(
    id = id,
    email = email,
    firstName = firstName,
    lastName = lastName,
    isActive = isActive
)

fun UserCreateRequest.toDomain() = User(
    email = email,
    firstName = firstName,
    lastName = lastName,
    isActive = isActive
)

fun UserUpdateRequest.toDomain() = User(
    email = email,
    firstName = firstName,
    lastName = lastName,
    isActive = isActive
)