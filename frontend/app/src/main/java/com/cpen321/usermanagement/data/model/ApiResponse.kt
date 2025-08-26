package com.cpen321.usermanagement.data.model

data class ApiResponse<T>(
    val message: String,
    val data: T? = null
)
