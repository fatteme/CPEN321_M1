package com.cpen321.usermanagement.ui.navigation

import androidx.compose.runtime.Composable
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
import com.cpen321.usermanagement.ui.viewmodel.AuthViewModel
import com.cpen321.usermanagement.ui.viewmodel.ProfileViewModel

object NavRoutes {
    const val AUTH = "auth"
    const val MAIN = "main"
    const val PROFILE = "profile"
    const val MANAGE_PROFILE = "manage_profile"
    const val MANAGE_HOBBIES = "manage_hobbies"
}

@Composable
fun AppNavigation(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    profileViewModel: ProfileViewModel
) {
    val authUiState by authViewModel.uiState.collectAsState()
    
    NavHost(
        navController = navController,
        startDestination = if (authUiState.isAuthenticated) NavRoutes.MAIN else NavRoutes.AUTH
    ) {
        composable(NavRoutes.AUTH) {
            AuthScreen(
                authViewModel = authViewModel,
                onAuthSuccess = {
                    navController.navigate(NavRoutes.MAIN) {
                        popUpTo(NavRoutes.AUTH) { inclusive = true }
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
