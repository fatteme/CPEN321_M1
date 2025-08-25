package com.cpen321.usermanagement.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cpen321.usermanagement.data.model.User
import com.cpen321.usermanagement.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import android.net.Uri

data class ProfileUiState(
    val isLoading: Boolean = false,
    val user: User? = null,
    val availableHobbies: List<String> = emptyList(),
    val selectedHobbies: Set<String> = emptySet(),
    val isSaving: Boolean = false,
    val isUploadingProfilePicture: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null
)

class ProfileViewModel(context: Context) : ViewModel() {
    private val authRepository = AuthRepository(context)
    
    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()
    
    fun loadProfile() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            
            // Load profile and available hobbies in parallel
            val profileResult = authRepository.getProfile()
            val hobbiesResult = authRepository.getAvailableHobbies()
            
            if (profileResult.isSuccess && hobbiesResult.isSuccess) {
                val user = profileResult.getOrNull()!!
                val availableHobbies = hobbiesResult.getOrNull()!!
                val selectedHobbies = user.hobbies.toSet()
                
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    user = user,
                    availableHobbies = availableHobbies,
                    selectedHobbies = selectedHobbies
                )
            } else {
                val errorMessage = when {
                    profileResult.isFailure -> profileResult.exceptionOrNull()?.message ?: "Failed to load profile"
                    hobbiesResult.isFailure -> hobbiesResult.exceptionOrNull()?.message ?: "Failed to load hobbies"
                    else -> "Failed to load data"
                }
                
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = errorMessage
                )
            }
        }
    }
    
    fun toggleHobby(hobby: String) {
        val currentSelected = _uiState.value.selectedHobbies.toMutableSet()
        if (currentSelected.contains(hobby)) {
            currentSelected.remove(hobby)
        } else {
            currentSelected.add(hobby)
        }
        _uiState.value = _uiState.value.copy(selectedHobbies = currentSelected)
    }
    
    fun saveHobbies() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSaving = true, errorMessage = null, successMessage = null)
            
            val selectedHobbiesList = _uiState.value.selectedHobbies.toList()
            val result = authRepository.updateUserHobbies(selectedHobbiesList)
            
            if (result.isSuccess) {
                val updatedUser = result.getOrNull()!!
                _uiState.value = _uiState.value.copy(
                    isSaving = false,
                    user = updatedUser,
                    successMessage = "Hobbies updated successfully!"
                )
            } else {
                val errorMessage = result.exceptionOrNull()?.message ?: "Failed to update hobbies"
                _uiState.value = _uiState.value.copy(
                    isSaving = false,
                    errorMessage = errorMessage
                )
            }
        }
    }
    
    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
    
    fun clearSuccessMessage() {
        _uiState.value = _uiState.value.copy(successMessage = null)
    }
    
    fun onEditProfilePictureClick() {
        // This will be handled by the UI to show image picker
        // The UI will call this function and then show the dialog
    }
    
    fun uploadProfilePicture(imageUri: Uri) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isUploadingProfilePicture = true, errorMessage = null)
            
            try {
                val result = authRepository.uploadProfilePicture(imageUri)
                if (result.isSuccess) {
                    val updatedUser = result.getOrNull()!!
                    _uiState.value = _uiState.value.copy(
                        isUploadingProfilePicture = false,
                        user = updatedUser,
                        successMessage = "Profile picture updated successfully!"
                    )
                } else {
                    val errorMessage = result.exceptionOrNull()?.message ?: "Failed to upload profile picture"
                    _uiState.value = _uiState.value.copy(
                        isUploadingProfilePicture = false,
                        errorMessage = errorMessage
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isUploadingProfilePicture = false,
                    errorMessage = e.message ?: "Failed to upload profile picture"
                )
            }
        }
    }
}
