package com.cpen321.usermanagement.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cpen321.usermanagement.data.model.User
import com.cpen321.usermanagement.data.repository.AuthRepository
import com.cpen321.usermanagement.data.repository.UserRepository
import com.cpen321.usermanagement.data.api.RetrofitClient
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class AuthUiState(
    val isLoading: Boolean = false,
    val isAuthenticated: Boolean = false,
    val user: User? = null,
    val errorMessage: String? = null
)

class AuthViewModel(context: Context) : ViewModel() {
    private val authRepository = AuthRepository(context)
    private val userRepository = UserRepository(context)
    
    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()
    
    init {
        checkAuthenticationStatus()
    }
    
    private fun checkAuthenticationStatus() {
        viewModelScope.launch {
            val isLoggedIn = authRepository.isLoggedIn()

            if (isLoggedIn) {
                val token = authRepository.getStoredToken()
                token?.let { RetrofitClient.setAuthToken(it) }
                
                userRepository.getProfile().onSuccess { user ->
                    _uiState.value = _uiState.value.copy(
                        isAuthenticated = true,
                        user = user
                    )
                }.onFailure { error ->
                    _uiState.value = _uiState.value.copy(isAuthenticated = false)
                }
            } else {
                _uiState.value = _uiState.value.copy(isAuthenticated = false)
            }
        }
    }

    suspend fun signInWithGoogle(context: Context): Result<GoogleIdTokenCredential> {
        return authRepository.signInWithGoogle(context)
    }
    
    fun handleGoogleSignInResult(credential: GoogleIdTokenCredential) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            authRepository.googleSignIn(credential.idToken)
                .onSuccess { body ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isAuthenticated = true,
                        user = body.user,
                        errorMessage = null
                    )
                }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = error.message
                    )
                }
        }
    }

    fun handleGoogleSignUpResult(credential: GoogleIdTokenCredential) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            authRepository.googleSignUp(credential.idToken)
                .onSuccess { body ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isAuthenticated = true,
                        user = body.user,
                        errorMessage = null
                    )
                }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = error.message
                    )
                }
        }
    }
    
    fun removeToken() {
        viewModelScope.launch {
            authRepository.removeToken()
                .onSuccess {
                    _uiState.value = AuthUiState(isAuthenticated = false)
                }
        }
    }
    
    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}
