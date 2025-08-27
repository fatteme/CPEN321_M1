package com.cpen321.usermanagement.data.repository

import android.content.Context
import com.cpen321.usermanagement.data.api.RetrofitClient
import com.cpen321.usermanagement.data.model.*
import com.cpen321.usermanagement.data.storage.TokenManager

class UserRepository(private val context: Context) {
    private val userApiService = RetrofitClient.userService
    private val tokenManager = TokenManager(context)

    suspend fun getProfile(): Result<User> {
        return try {
            val response = userApiService.getProfile("") // AuthInterceptor handles the token
            if (response.isSuccessful && response.body()?.data != null) {
                Result.success(response.body()!!.data!!.user)
            } else {
                val errorMessage = response.body()?.message ?: "Failed to fetch user information."
                tokenManager.clearToken()
                RetrofitClient.setAuthToken(null)
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateUserHobbies(hobbies: List<String>): Result<User> {
        return try {
            val response = userApiService.updateUserHobbies("", UpdateHobbiesRequest(hobbies)) // AuthInterceptor handles the token
            if (response.isSuccessful && response.body()?.data != null) {
                Result.success(response.body()!!.data!!.user)
            } else {
                val errorMessage = response.body()?.message ?: "Failed to update hobbies."
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
