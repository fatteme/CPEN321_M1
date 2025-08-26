package com.cpen321.usermanagement.data.repository

import android.content.Context
import androidx.credentials.*
import androidx.credentials.exceptions.GetCredentialException
import com.cpen321.usermanagement.config.AppConfig
import com.cpen321.usermanagement.data.api.RetrofitClient
import com.cpen321.usermanagement.data.model.*
import com.cpen321.usermanagement.data.storage.TokenManager
import com.google.android.libraries.identity.googleid.*
import kotlinx.coroutines.flow.first

class AuthRepository(context: Context) {
    private val apiService = RetrofitClient.authService
    private val tokenManager = TokenManager(context)
    private val credentialManager = CredentialManager.create(context)
    val signInWithGoogleOption: GetSignInWithGoogleOption = GetSignInWithGoogleOption.Builder(
        serverClientId = AppConfig.GOOGLE_CLIENT_ID
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

    suspend fun logout(): Result<Unit> {
        tokenManager.clearToken()
        RetrofitClient.setAuthToken(null)
        return Result.success(Unit)
    }

    suspend fun isLoggedIn(): Boolean {
        return tokenManager.getToken().first() != null
    }
}
