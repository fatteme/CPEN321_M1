package com.cpen321.usermanagement.data.model

data class User(
    val id: String,
    val email: String,
    val name: String,
    val profilePicture: String,
    val createdAt: String? = null,
    val updatedAt: String? = null
)
