package com.cpen321.usermanagement.data.repository

import android.content.Context
import com.cpen321.usermanagement.data.api.RetrofitClient
import com.cpen321.usermanagement.data.model.*

class HobbyRepository(context: Context) {
    private val apiService = RetrofitClient.hobbyService

    suspend fun getAvailableHobbies(): Result<List<String>> {
        return try {
            val response = apiService.getAvailableHobbies()
            if (response.isSuccessful && response.body()?.data != null) {
                Result.success(response.body()!!.data!!.hobbies)
            } else {
                val errorMessage = response.body()?.message ?: "Failed to fetch hobbies."
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
