package com.cpen321.usermanagement.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.cpen321.usermanagement.R
import com.cpen321.usermanagement.ui.components.ImagePicker
import com.cpen321.usermanagement.ui.components.MessageSnackbar
import com.cpen321.usermanagement.ui.viewmodel.ProfileViewModel
import com.cpen321.usermanagement.data.api.RetrofitClient

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageProfileScreen(
    profileViewModel: ProfileViewModel,
    onBackClick: () -> Unit
) {
    val uiState by profileViewModel.uiState.collectAsState()
    val snackBarHostState = remember { SnackbarHostState() }
    var showImagePickerDialog by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var bio by remember { mutableStateOf<String?>(null) }
    var originalName by remember { mutableStateOf("") }
    var originalBio by remember { mutableStateOf<String?>(null) }
    
    LaunchedEffect(Unit) {
        profileViewModel.loadProfile()
    }
    
    LaunchedEffect(uiState.user) {
        uiState.user?.let { user ->
            name = user.name
            email = user.email
            bio = user.bio
            originalName = user.name
            originalBio = user.bio
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.manage_profile),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Medium
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        },
        snackbarHost = { 
            MessageSnackbar(
                hostState = snackBarHostState,
                successMessage = uiState.successMessage,
                errorMessage = uiState.errorMessage,
                onSuccessMessageShown = { profileViewModel.clearSuccessMessage() },
                onErrorMessageShown = { profileViewModel.clearError() }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                uiState.user?.let { user ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp)
                            .verticalScroll(rememberScrollState()),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(24.dp)
                    ) {
                        // Profile Picture with Edit Button
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surface
                            )
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(32.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Box(
                                    modifier = Modifier.size(120.dp)
                                ) {
                                    AsyncImage(
                                        model = RetrofitClient.getImageUrl(user.profilePicture),
                                        contentDescription = stringResource(R.string.profile_picture),
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .clip(CircleShape)
                                    )
                                    
                                    // Edit Button Overlay
                                    IconButton(
                                        onClick = { showImagePickerDialog = true },
                                        modifier = Modifier
                                            .align(Alignment.BottomEnd)
                                            .size(32.dp)
                                            .background(
                                                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.9f),
                                                shape = CircleShape
                                            )
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Edit,
                                            contentDescription = stringResource(R.string.edit_profile_picture),
                                            tint = MaterialTheme.colorScheme.onPrimary,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                }
                            }
                        }
                        
                        // Name Field
                        OutlinedTextField(
                            value = name,
                            onValueChange = { name = it },
                            label = { Text(stringResource(R.string.name)) },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                        )
                        
                        // Email Field
                        OutlinedTextField(
                            value = email,
                            onValueChange = { email = it },
                            label = { Text(stringResource(R.string.email)) },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            enabled = false
                        )
                        
                        // Bio Field
                        OutlinedTextField(
                            value = bio ?: "",
                            onValueChange = { bio = it },
                            label = { Text(stringResource(R.string.bio)) },
                            placeholder = { Text(stringResource(R.string.bio_placeholder)) },
                            modifier = Modifier.fillMaxWidth(),
                            minLines = 3,
                            maxLines = 5,
                        )
                        
                        // Save Button
                        Button(
                            onClick = { profileViewModel.updateProfile(name, bio ?: "") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            ),
                            enabled = !uiState.isSaving && 
                                    ((name.isNotBlank() && name != originalName) || 
                                     (bio != originalBio && bio?.isNotBlank() == true))
                        ) {
                            if (uiState.isSaving) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(20.dp),
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Text(
                                    text = stringResource(R.string.save),
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }
            }
        }
    }
    
    // Image Picker Dialog
    if (showImagePickerDialog) {
        ImagePicker(
            onDismiss = { showImagePickerDialog = false },
            onImageSelected = { uri ->
                showImagePickerDialog = false
                profileViewModel.uploadProfilePicture(uri)
            }
        )
    }
}
