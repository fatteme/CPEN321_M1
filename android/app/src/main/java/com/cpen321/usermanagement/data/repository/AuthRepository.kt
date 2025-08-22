package com.cpen321.usermanagement.data.repository

import android.content.Context
import android.content.Intent
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
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.tasks.await

class AuthRepository(private val context: Context) {
    private val apiService = RetrofitClient.apiService
    private val tokenManager = TokenManager(context)
    
    private val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken("YOUR_GOOGLE_CLIENT_ID") // Replace with your actual Google Client ID
        .requestEmail()
        .build()
    
    private val googleSignInClient: GoogleSignInClient = GoogleSignIn.getClient(context, googleSignInOptions)
    
    fun getGoogleSignInIntent(): Intent = googleSignInClient.signInIntent
    
    suspend fun handleGoogleSignInResult(account: GoogleSignInAccount): Result<User> {
        return try {
            val idToken = account.idToken ?: throw Exception("Failed to get ID token")
            
            val response = apiService.googleLogin(GoogleLoginRequest(idToken))
            
            if (response.isSuccessful && response.body()?.success == true) {
                val authResponse = response.body()!!.data!!
                tokenManager.saveToken(authResponse.token)
                Result.success(authResponse.user)
            } else {
                val errorMessage = if (response.code() == 503) {
                    "Authentication service temporarily unavailable. Please try again later."
                } else {
                    response.body()?.message ?: "Authentication failed"
                }
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getProfile(): Result<User> {
        return try {
            val token = tokenManager.getToken().first()
            if (token == null) {
                return Result.failure(Exception("No authentication token found"))
            }
            
            val response = apiService.getProfile("Bearer $token")
            
            if (response.isSuccessful && response.body()?.success == true) {
                val profileResponse = response.body()!!.data!!
                Result.success(profileResponse.user)
            } else {
                val errorMessage = response.body()?.message ?: "Failed to fetch user information."
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Failed to fetch user information."))
        }
    }
    
    suspend fun logout(): Result<Unit> {
        return try {
            val token = tokenManager.getToken().first()
            if (token != null) {
                apiService.logout("Bearer $token")
            }
            
            // Clear local token and sign out from Google
            tokenManager.clearToken()
            googleSignInClient.signOut().await()
            
            Result.success(Unit)
        } catch (e: Exception) {
            // Even if backend call fails, we should clear local state
            tokenManager.clearToken()
            try {
                googleSignInClient.signOut().await()
            } catch (ignored: Exception) {}
            Result.success(Unit)
        }
    }
    
    suspend fun isLoggedIn(): Boolean {
        return tokenManager.getToken().first() != null
    }
}
