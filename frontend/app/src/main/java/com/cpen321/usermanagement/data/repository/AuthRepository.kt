package com.cpen321.usermanagement.data.repository

import android.content.Context
import android.content.Intent
import android.util.JsonToken
import android.util.Log
import androidx.credentials.Credential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import com.cpen321.usermanagement.data.api.RetrofitClient
import com.cpen321.usermanagement.data.model.ApiResponse
import com.cpen321.usermanagement.data.model.AuthResponse
import com.cpen321.usermanagement.data.model.GoogleLoginRequest
import com.cpen321.usermanagement.data.model.ProfileResponse
import com.cpen321.usermanagement.data.model.User
import com.cpen321.usermanagement.data.storage.TokenManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialResponse
import androidx.credentials.PasswordCredential
import androidx.credentials.PublicKeyCredential
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.tasks.await
import kotlin.math.log
import com.cpen321.usermanagement.data.model.UpdateHobbiesRequest
import com.cpen321.usermanagement.data.model.UpdateProfilePictureRequest
import com.cpen321.usermanagement.data.model.UploadImageResponse
import com.cpen321.usermanagement.data.api.ApiService
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import android.net.Uri

class AuthRepository(private val context: Context) {
    private val TAG = "AuthRepository"
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

    suspend fun googleLogin(tokenId: String): Result<AuthResponse>{
        val googleLoginReq = GoogleLoginRequest(tokenId)
        val response = apiService.googleLogin(googleLoginReq)
        if (response.isSuccessful) {
            val authResponse = response.body()!!.data!!
            tokenManager.saveToken(authResponse.token)
            return Result.success(response.body()!!.data!!)
        } else {
            val errorMessage = response.body()?.message ?: "Failed to login with Google."
            throw Exception(errorMessage)
        }
    }

    suspend fun getProfile(): Result<User> {
        return try {
            val token = tokenManager.getToken().first()
            if (token == null) {
                return Result.failure(Exception("No authentication token found"))
            }
            
            val response = apiService.getProfile("Bearer $token")

            Log.d(TAG, "getProfile response: ${response.isSuccessful}")
            if (response.isSuccessful) {
                Log.d(TAG, "getProfile response: ${response.body()}")
                val profileResponse = response.body()!!.data!!
                Result.success(profileResponse.user)
            } else {
                val errorMessage = response.body()?.message ?: "Failed to fetch user information."
                tokenManager.clearToken()
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Failed to fetch user information."))
        }
    }
    
    suspend fun logout(): Result<Unit> {
        return try {
            val token = tokenManager.getToken().first()
            if (token == null) {
                return Result.failure(Exception("No authentication token found"))
            }

            apiService.logout("Bearer $token")
            // Clear local token and sign out from Google
            tokenManager.clearToken()
            Result.success(Unit)
        } catch (e: Exception) {
            // Even if backend call fails, we should clear local state
            tokenManager.clearToken()
            Result.success(Unit)
        }
    }
    
    suspend fun isLoggedIn(): Boolean {
        return tokenManager.getToken().first() != null
    }
    
    suspend fun getAvailableHobbies(): Result<List<String>> {
        return try {
            val token = tokenManager.getToken().first()
            if (token == null) {
                return Result.failure(Exception("No authentication token found"))
            }
            val response = apiService.getAvailableHobbies("Bearer $token")

            if (response.isSuccessful) {
                val hobbiesResponse = response.body()!!.data!!
                Result.success(hobbiesResponse.hobbies)
            } else {
                val errorMessage = response.body()?.message ?: "Failed to fetch available hobbies."
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Failed to fetch available hobbies."))
        }
    }
    
    suspend fun updateUserHobbies(hobbies: List<String>): Result<User> {
        return try {
            val token = tokenManager.getToken().first()
            if (token == null) {
                return Result.failure(Exception("No authentication token found"))
            }
            
            val request = UpdateHobbiesRequest(hobbies)
            val response = apiService.updateUserHobbies("Bearer $token", request)
            
            if (response.isSuccessful) {
                val profileResponse = response.body()!!.data!!
                Result.success(profileResponse.user)
            } else {
                val errorMessage = response.body()?.message ?: "Failed to update user hobbies."
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Failed to update user hobbies."))
        }
    }
    
    suspend fun uploadProfilePicture(imageUri: Uri): Result<User> {
        return try {
            val token = tokenManager.getToken().first()
            if (token == null) {
                return Result.failure(Exception("No authentication token found"))
            }
            
            // Convert URI to file and create MultipartBody.Part
            val file = uriToFile(context, imageUri)
            val requestBody = file.asRequestBody("image/*".toMediaType())
            val multipartBody = MultipartBody.Part.createFormData("media", file.name, requestBody)
            
            // First upload the image
            val uploadResponse = apiService.uploadImage("Bearer $token", multipartBody)
            
            if (uploadResponse.isSuccessful) {
                val uploadResult = uploadResponse.body()!!.data!!
                val profilePicture = uploadResult.image
                val updateRequest = UpdateProfilePictureRequest(profilePicture)

                // Then update the user's profile picture URL
                val updateResponse = apiService.updateProfilePicture("Bearer $token", updateRequest)
                
                if (updateResponse.isSuccessful) {
                    val profileResponse = updateResponse.body()!!.data!!
                    Result.success(profileResponse.user)
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
