package com.cpen321.usermanagement.data.api

import com.cpen321.usermanagement.data.model.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST("auth/")
    suspend fun googleLogin(@Body request: GoogleLoginRequest): Response<ApiResponse<AuthData>>
}
