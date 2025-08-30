package com.cpen321.usermanagement.data.api

import com.cpen321.usermanagement.data.model.ApiResponse
import com.cpen321.usermanagement.data.model.HobbiesData
import retrofit2.Response
import retrofit2.http.GET

interface HobbyService {
    @GET("hobbies")
    suspend fun getAvailableHobbies(): Response<ApiResponse<HobbiesData>>
}
