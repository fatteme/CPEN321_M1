package com.cpen321.usermanagement

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.rememberNavController
import com.cpen321.usermanagement.ui.navigation.AppNavigation
import com.cpen321.usermanagement.ui.theme.UserManagementTheme
import com.cpen321.usermanagement.ui.viewmodel.AuthViewModel
import com.cpen321.usermanagement.ui.viewmodel.ProfileViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            UserManagementTheme {
                UserManagementApp()
            }
        }
    }
}

@Composable
fun UserManagementApp() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val authViewModel = AuthViewModel(context)
    val profileViewModel = ProfileViewModel(context)

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        AppNavigation(
            navController = navController,
            authViewModel = authViewModel,
            profileViewModel = profileViewModel
        )
    }
}
