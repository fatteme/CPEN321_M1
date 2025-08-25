package com.cpen321.usermanagement.data.model

data class User(
    val _id: String,
    val email: String,
    val name: String,
    val profilePicture: String,
    val hobbies: List<String> = emptyList(),
    val createdAt: String? = null,
    val updatedAt: String? = null
)
