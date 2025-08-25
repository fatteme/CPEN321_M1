package com.cpen321.usermanagement.data.model

data class ApiResponse<T>(
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

data class HobbiesResponse(
    val hobbies: List<String>
)

data class UpdateHobbiesRequest(
    val hobbies: List<String>
)
