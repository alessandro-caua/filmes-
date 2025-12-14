package com.outracoisa.avaliacao3appn2.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.outracoisa.avaliacao3appn2.ui.screen.HomeScreen
import com.outracoisa.avaliacao3appn2.ui.screen.LoginScreen
import com.outracoisa.avaliacao3appn2.ui.screen.RegisterScreen
import com.outracoisa.avaliacao3appn2.ui.viewmodel.AuthViewModel
import com.outracoisa.avaliacao3appn2.ui.viewmodel.MovieViewModel

@Composable
fun AppNavigation(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    movieViewModel: MovieViewModel
) {
    val authState by authViewModel.uiState.collectAsState()
    
    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        composable(Screen.Login.route) {
            LoginScreen(
                authViewModel = authViewModel,
                onNavigateToRegister = {
                    navController.navigate(Screen.Register.route)
                },
                onLoginSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }
        
        composable(Screen.Register.route) {
            RegisterScreen(
                authViewModel = authViewModel,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onRegisterSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }
        
        composable(Screen.Home.route) {
            authState.currentUser?.let { user ->
                HomeScreen(
                    currentUser = user,
                    movieViewModel = movieViewModel,
                    onLogout = {
                        authViewModel.logout()
                        navController.navigate(Screen.Login.route) {
                            popUpTo(Screen.Home.route) { inclusive = true }
                        }
                    }
                )
            }
        }
    }
}
