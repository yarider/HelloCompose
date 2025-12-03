package com.example.hellocompose.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.example.hellocompose.screens.HomeScreen
import com.example.hellocompose.screens.ProfileScreen
import com.example.hellocompose.screens.SettingsScreen

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(navController, startDestination = "home") {
        composable("home") {
            HomeScreen(navController)
        }
        
        composable("settings") {
            SettingsScreen(navController)
        }
        
        composable(
            route = "profile/{userId}",
            arguments = listOf(navArgument("userId") { type = NavType.StringType }),
            deepLinks = listOf(navDeepLink { uriPattern = "myapp://profile/{userId}" })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId")
            ProfileScreen(userId)
        }
    }
}
