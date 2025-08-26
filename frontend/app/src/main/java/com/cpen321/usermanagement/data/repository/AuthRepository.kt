package com.cpen321.usermanagement.data.repository

import android.content.Context
import android.net.Uri
import androidx.credentials.*
import androidx.credentials.exceptions.GetCredentialException
import com.cpen321.usermanagement.data.api.RetrofitClient
import com.cpen321.usermanagement.data.model.*
import com.cpen321.usermanagement.data.storage.TokenManager
import com.google.android.libraries.identity.googleid.*
import kotlinx.coroutines.flow.first
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class AuthRepository(private val context: Context) {
    private val apiService = RetrofitClient.apiService
    private val tokenManager = TokenManager(context)
    private val credentialManager = CredentialManager.create(context)
    val signInWithGoogleOption: GetSignInWithGoogleOption = GetSignInWithGoogleOption.Builder(
        serverClientId = "482701217775-3maplltka0g713ntauacrc6ueopbgbm5.apps.googleusercontent.com"
    ).build()

    suspend fun signInWithGoogle(context: Context): Result<GoogleIdTokenCredential> {
        val request = GetCredentialRequest.Builder()
            .addCredentialOption(signInWithGoogleOption)
            .build()

        return try {
            val response = credentialManager.getCredential(context, request)
            handleSignInWithGoogleOption(response)
        } catch (e: GetCredentialException) {
            Result.failure(e)
        }
    }

    private fun handleSignInWithGoogleOption(
        result: GetCredentialResponse
    ): Result<GoogleIdTokenCredential> {
        val credential = result.credential
        return when (credential) {
            is CustomCredential -> {
                if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    try {
                        val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                        Result.success(googleIdTokenCredential)
                    } catch (e: GoogleIdTokenParsingException) {
                        Result.failure(e)
                    }
                } else {
                    Result.failure(Exception("Unexpected type of credential"))
                }
            }
            else -> Result.failure(Exception("Unexpected type of credential"))
        }
    }

    suspend fun googleLogin(tokenId: String): Result<AuthData> {
        val googleLoginReq = GoogleLoginRequest(tokenId)
        return try {
            val response = apiService.googleLogin(googleLoginReq)
            if (response.isSuccessful && response.body()?.data != null) {
                val authData = response.body()!!.data!!
                tokenManager.saveToken(authData.token)
                RetrofitClient.setAuthToken(authData.token)
                Result.success(authData)
            } else {
                val errorMessage = response.body()?.message ?: "Failed to login with Google."
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getProfile(): Result<User> {
        return try {
            val response = apiService.getProfile()
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
    
    suspend fun logout(): Result<Unit> {
        tokenManager.clearToken()
        RetrofitClient.setAuthToken(null)
        return Result.success(Unit)
    }
    
    suspend fun isLoggedIn(): Boolean {
        return tokenManager.getToken().first() != null
    }
    
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

    suspend fun updateUserHobbies(hobbies: List<String>): Result<User> {
        return try {
            val response = apiService.updateUserHobbies(UpdateHobbiesRequest(hobbies))
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
            // Convert URI to file and create MultipartBody.Part
            val file = uriToFile(context, imageUri)
            val requestBody = file.asRequestBody("image/*".toMediaType())
            val multipartBody = MultipartBody.Part.createFormData("media", file.name, requestBody)
            
            // First upload the image
            val uploadResponse = apiService.uploadImage(multipartBody)

            if (uploadResponse.isSuccessful && uploadResponse.body()?.data != null) {
                val uploadData = uploadResponse.body()!!.data!!
                val profilePicture = uploadData.url  // Using the new UploadImageData's url property
                val updateRequest = UpdateProfilePictureRequest(profilePicture)

                // Then update the user's profile picture URL
                val updateResponse = apiService.updateProfilePicture(updateRequest)

                if (updateResponse.isSuccessful && updateResponse.body()?.data != null) {
                    Result.success(updateResponse.body()!!.data!!.user)
                } else {
                    val errorMessage = updateResponse.body()?.message ?: "Failed to update profile picture."
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
    
    private fun uriToFile(context: Context, uri: Uri): File {
        return when (uri.scheme) {
            "file" -> File(uri.path!!)
            "content" -> {
                val inputStream = context.contentResolver.openInputStream(uri)
                val file = File.createTempFile("profile_", ".jpg", context.cacheDir)
                inputStream?.use { input ->
                    file.outputStream().use { output ->
                        input.copyTo(output)
                    }
                }
                file
            }
            else -> throw IllegalArgumentException("Unsupported URI scheme: ${uri.scheme}")
        }
    }
}
