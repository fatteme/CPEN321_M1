package com.cpen321.usermanagement.data.api

import com.cpen321.usermanagement.data.model.ApiResponse
import com.cpen321.usermanagement.data.model.*
import retrofit2.Response
import retrofit2.http.*
import okhttp3.MultipartBody

interface ApiService {
    @POST("auth/")
    suspend fun googleLogin(@Body request: GoogleLoginRequest): Response<ApiResponse<AuthData>>

    @GET("user/profile")
    suspend fun getProfile(): Response<ApiResponse<ProfileData>>

    @POST("user/hobbies")
    suspend fun updateUserHobbies(
        @Body request: UpdateHobbiesRequest
    ): Response<ApiResponse<ProfileData>>

    @POST("user/profile-picture")
    suspend fun updateProfilePicture(
        @Body request: UpdateProfilePictureRequest
    ): Response<ApiResponse<ProfileData>>

    @GET("hobbies")
    suspend fun getAvailableHobbies(): Response<ApiResponse<HobbiesData>>

    @Multipart
    @POST("media/upload")
    suspend fun uploadImage(
        @Part media: MultipartBody.Part
    ): Response<ApiResponse<UploadImageData>>

}
