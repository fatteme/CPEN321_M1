package com.cpen321.usermanagement.ui.components

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier

/**
 * A reusable Snackbar component that handles both success and error messages
 * with consistent short duration to prevent UI blocking.
 */
@Composable
fun MessageSnackbar(
    hostState: SnackbarHostState,
    successMessage: String?,
    errorMessage: String?,
    onSuccessMessageShown: () -> Unit,
    onErrorMessageShown: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Handle success messages
    LaunchedEffect(successMessage) {
        successMessage?.let { message ->
            hostState.showSnackbar(
                message = message,
                duration = SnackbarDuration.Short
            )
            onSuccessMessageShown()
        }
    }
    
    // Handle error messages
    LaunchedEffect(errorMessage) {
        errorMessage?.let { message ->
            hostState.showSnackbar(
                message = message,
                duration = SnackbarDuration.Short
            )
            onErrorMessageShown()
        }
    }
    
    // Render the SnackbarHost
    SnackbarHost(
        hostState = hostState,
        modifier = modifier
    )
}
