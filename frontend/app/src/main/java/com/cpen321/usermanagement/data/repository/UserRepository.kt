package com.cpen321.usermanagement.data.repository

import android.content.Context
import android.net.Uri
import com.cpen321.usermanagement.data.api.RetrofitClient
import com.cpen321.usermanagement.data.model.UpdateProfileRequest
import com.cpen321.usermanagement.data.model.User
import com.cpen321.usermanagement.data.storage.TokenManager
import com.cpen321.usermanagement.util.MediaUtils.uriToFile
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody

class UserRepository(private val context: Context) {
    private val userApiService = RetrofitClient.userService
    private val mediaApiService = RetrofitClient.mediaService
    private val tokenManager = TokenManager(context)

    suspend fun getProfile(): Result<User> {
        return try {
            val response = userApiService.getProfile()
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
            val updateRequest = UpdateProfileRequest(hobbies = hobbies)
            val response = userApiService.updateProfile(updateRequest)
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

    suspend fun uploadProfilePicture(imageUri: Uri): Result<User> {
        return try {
            val file = uriToFile(context, imageUri)
            val requestBody = file.asRequestBody("image/*".toMediaType())
            val multipartBody = MultipartBody.Part.createFormData("media", file.name, requestBody)

            val uploadResponse = mediaApiService.uploadImage(multipartBody)

            if (uploadResponse.isSuccessful && uploadResponse.body()?.data != null) {
                val uploadData = uploadResponse.body()!!.data!!
                val updateRequest =
                    UpdateProfileRequest(profilePicture = uploadData.image)

                val updateResponse = userApiService.updateProfile(updateRequest)

                if (updateResponse.isSuccessful && updateResponse.body()?.data != null) {
                    Result.success(updateResponse.body()!!.data!!.user)
                } else {
                    val errorMessage =
                        updateResponse.body()?.message ?: "Failed to update profile picture."
                    Result.failure(Exception(errorMessage))
                }
            } else {
                val errorMessage = uploadResponse.body()?.message ?: "Failed to upload image."
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Failed to upload profile picture: ${e.message}"))
        }
    }

    suspend fun updateProfile(name: String, bio: String): Result<User> {
        return try {
            val updateRequest = UpdateProfileRequest(name = name, bio = bio)
            val response = userApiService.updateProfile(updateRequest)
            if (response.isSuccessful && response.body()?.data != null) {
                Result.success(response.body()!!.data!!.user)
            } else {
                val errorMessage = response.body()?.message ?: "Failed to update profile."
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteProfile(): Result<Unit> {
        return try {
            val response = userApiService.deleteProfile()
            if (response.isSuccessful) {
                tokenManager.clearToken()
                RetrofitClient.setAuthToken(null)
                Result.success(Unit)
            } else {
                val errorMessage = response.body()?.message ?: "Failed to delete profile."
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
