package com.cpen321.usermanagement.data.api

import com.cpen321.usermanagement.data.model.*
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface MediaService {
    @Multipart
    @POST("media/upload")
    suspend fun uploadImage(
        @Header("Authorization") authHeader: String,
        @Part media: MultipartBody.Part
    ): Response<ApiResponse<UploadImageData>>
}
