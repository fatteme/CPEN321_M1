package com.cpen321.usermanagement.data.api

import com.cpen321.usermanagement.data.model.ApiResponse
import com.cpen321.usermanagement.data.model.AuthData
import com.cpen321.usermanagement.data.model.GoogleLoginRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST("auth/signin")
    suspend fun googleSignIn(@Body request: GoogleLoginRequest): Response<ApiResponse<AuthData>>

    @POST("auth/signup")
    suspend fun googleSignUp(@Body request: GoogleLoginRequest): Response<ApiResponse<AuthData>>
}
