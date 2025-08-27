package com.cpen321.usermanagement.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.cpen321.usermanagement.ui.screen.AuthScreen
import com.cpen321.usermanagement.ui.screen.MainScreen
import com.cpen321.usermanagement.ui.screen.ProfileScreen
import com.cpen321.usermanagement.ui.screen.ManageProfileScreen
import com.cpen321.usermanagement.ui.screen.ManageHobbiesScreen
import com.cpen321.usermanagement.ui.screen.ProfileCompletionScreen
import com.cpen321.usermanagement.ui.viewmodel.AuthViewModel
import com.cpen321.usermanagement.ui.viewmodel.ProfileViewModel
import android.util.Log

object NavRoutes {
    const val AUTH = "auth"
    const val MAIN = "main"
    const val PROFILE = "profile"
    const val MANAGE_PROFILE = "manage_profile"
    const val MANAGE_HOBBIES = "manage_hobbies"
    const val PROFILE_COMPLETION = "profile_completion"
}

@Composable
fun AppNavigation(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    profileViewModel: ProfileViewModel
) {
    val authUiState by authViewModel.uiState.collectAsState()
    val profileUiState by profileViewModel.uiState.collectAsState()
    
    // Handle navigation only when user first authenticates
    LaunchedEffect(authUiState.isAuthenticated) {
        if (authUiState.isAuthenticated) {
            // Only navigate if we're currently on the AUTH screen
            val currentRoute = navController.currentBackStackEntry?.destination?.route
            Log.d("Navigation", "Auth state changed: isAuthenticated=true, currentRoute=$currentRoute")
            
            if (currentRoute == NavRoutes.AUTH) {
                val needsProfileCompletion = profileUiState.user?.bio == null || 
                    profileUiState.user?.bio?.isBlank() == true
                
                Log.d("Navigation", "Navigating from AUTH: needsProfileCompletion=$needsProfileCompletion")
                
                if (needsProfileCompletion) {
                    navController.navigate(NavRoutes.PROFILE_COMPLETION) {
                        popUpTo(NavRoutes.AUTH) { inclusive = true }
                    }
                } else {
                    navController.navigate(NavRoutes.MAIN) {
                        popUpTo(NavRoutes.AUTH) { inclusive = true }
                    }
                }
            } else {
                Log.d("Navigation", "Not on AUTH screen, skipping navigation. Current route: $currentRoute")
            }
        }
    }
    
    // Load profile when authentication state changes
    LaunchedEffect(authUiState.isAuthenticated) {
        if (authUiState.isAuthenticated && profileUiState.user == null) {
            Log.d("Navigation", "Loading profile for authenticated user")
            profileViewModel.loadProfile()
        }
    }
    
    NavHost(
        navController = navController,
        startDestination = NavRoutes.AUTH
    ) {
        composable(NavRoutes.AUTH) {
            AuthScreen(
                authViewModel = authViewModel,
                onAuthSuccess = {
                    profileViewModel.loadProfile()
                }
            )
        }
        
        composable(NavRoutes.PROFILE_COMPLETION) {
            ProfileCompletionScreen(
                profileViewModel = profileViewModel,
                onProfileCompleted = {
                    navController.navigate(NavRoutes.MAIN) {
                        popUpTo(NavRoutes.PROFILE_COMPLETION) { inclusive = true }
                    }
                }
            )
        }
        
        composable(NavRoutes.MAIN) {
            MainScreen(
                onProfileClick = {
                    navController.navigate(NavRoutes.PROFILE)
                }
            )
        }
        
        composable(NavRoutes.PROFILE) {
            ProfileScreen(
                profileViewModel = profileViewModel,
                onBackClick = {
                    navController.popBackStack()
                },
                onSignOut = {
                    authViewModel.logout()
                    navController.navigate(NavRoutes.AUTH) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onManageProfileClick = {
                    navController.navigate(NavRoutes.MANAGE_PROFILE)
                },
                onManageHobbiesClick = {
                    navController.navigate(NavRoutes.MANAGE_HOBBIES)
                },
                onAccountDeleted = {
                    authViewModel.logout()
                    navController.navigate(NavRoutes.AUTH) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
        
        composable(NavRoutes.MANAGE_PROFILE) {
            ManageProfileScreen(
                profileViewModel = profileViewModel,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
        
        composable(NavRoutes.MANAGE_HOBBIES) {
            ManageHobbiesScreen(
                profileViewModel = profileViewModel,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}
