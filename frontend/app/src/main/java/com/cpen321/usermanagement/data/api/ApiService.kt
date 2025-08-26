package com.cpen321.usermanagement.data.api

import com.cpen321.usermanagement.data.model.ApiResponse
import com.cpen321.usermanagement.data.model.AuthResponse
import com.cpen321.usermanagement.data.model.GoogleLoginRequest
import com.cpen321.usermanagement.data.model.HobbiesResponse
import com.cpen321.usermanagement.data.model.ProfileResponse
import com.cpen321.usermanagement.data.model.UpdateHobbiesRequest
import com.cpen321.usermanagement.data.model.UploadImageResponse
import com.cpen321.usermanagement.data.model.UpdateProfilePictureRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Multipart
import okhttp3.MultipartBody

interface ApiService {
    @POST("auth/")
    suspend fun googleLogin(@Body request: GoogleLoginRequest): Response<ApiResponse<AuthResponse>>
    
    @POST("auth/logout")
    suspend fun logout(): Response<ApiResponse<Unit>>

    @GET("user/profile")
    suspend fun getProfile(): Response<ApiResponse<ProfileResponse>>

    @GET("hobbies")
    suspend fun getAvailableHobbies(): Response<ApiResponse<HobbiesResponse>>

    @POST("user/hobbies")
    suspend fun updateUserHobbies(
        @Body request: UpdateHobbiesRequest
    ): Response<ApiResponse<ProfileResponse>>
    
    @Multipart
    @POST("media/upload")
    suspend fun uploadImage(
        @Part media: MultipartBody.Part
    ): Response<ApiResponse<UploadImageResponse>>
    
    @POST("user/profile-picture")
    suspend fun updateProfilePicture(
        @Body request: UpdateProfilePictureRequest
    ): Response<ApiResponse<ProfileResponse>>
}
