package com.cpen321.usermanagement.data.api

import com.cpen321.usermanagement.data.model.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface UserService {
    @GET("user/profile")
    suspend fun getProfile(@Header("Authorization") authHeader: String): Response<ApiResponse<ProfileData>>

    @POST("user/hobbies")
    suspend fun updateUserHobbies(
        @Header("Authorization") authHeader: String,
        @Body request: UpdateHobbiesRequest
    ): Response<ApiResponse<ProfileData>>
}
