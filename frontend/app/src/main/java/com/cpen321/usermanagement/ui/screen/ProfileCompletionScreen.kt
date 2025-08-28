package com.cpen321.usermanagement.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cpen321.usermanagement.ui.viewmodel.ProfileViewModel
import com.cpen321.usermanagement.ui.components.MessageSnackbar

@Composable
fun ProfileCompletionScreen(
    profileViewModel: ProfileViewModel,
    onProfileCompleted: () -> Unit
) {
    val uiState by profileViewModel.uiState.collectAsState()
    val snackBarHostState = remember { SnackbarHostState() }
    var bioText by remember { mutableStateOf("") }
    
    LaunchedEffect(uiState.user) {
        uiState.user?.let { user ->
            if (user.bio != null && user.bio.isNotBlank()) {
                onProfileCompleted()
            }
        }
    }
    
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Welcome Title
            Text(
                text = "Complete Your Profile",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.primary
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "Tell us a bit about yourself to get started",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            Spacer(modifier = Modifier.height(48.dp))
            
            // Bio Input Field
            OutlinedTextField(
                value = bioText,
                onValueChange = { bioText = it },
                label = { Text("Bio") },
                placeholder = { Text("Share something about yourself...") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
                maxLines = 5,
                shape = RoundedCornerShape(12.dp),
                enabled = !uiState.isSaving
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Save Button
            Button(
                onClick = {
                    if (bioText.isNotBlank()) {
                        profileViewModel.updateProfile(
                            name = uiState.user?.name ?: "",
                            bio = bioText
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(28.dp),
                enabled = bioText.isNotBlank() && !uiState.isSaving
            ) {
                if (uiState.isSaving) {
                    CircularProgressIndicator(
                        modifier = Modifier.height(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text(
                        text = "Save & Continue",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Skip Button (Optional)
            Button(
                onClick = {
                    onProfileCompleted()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurface
                ),
                enabled = !uiState.isSaving
            ) {
                Text(
                    text = "Skip for now",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
        
        MessageSnackbar(
            hostState = snackBarHostState,
            successMessage = uiState.successMessage,
            errorMessage = uiState.errorMessage,
            onSuccessMessageShown = { 
                profileViewModel.clearSuccessMessage()
                onProfileCompleted()
            },
            onErrorMessageShown = { profileViewModel.clearError() },
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}
