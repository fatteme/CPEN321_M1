package com.cpen321.usermanagement.data.model

data class ApiResponse<T>(
    val success: Boolean,
    val message: String,
    val data: T? = null
)

data class AuthResponse(
    val token: String,
    val user: User
)

data class ProfileResponse(
    val user: User
)

data class GoogleLoginRequest(
    val idToken: String
)
