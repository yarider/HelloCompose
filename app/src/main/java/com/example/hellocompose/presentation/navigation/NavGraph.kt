package com.example.hellocompose.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.hellocompose.presentation.ui.UserDetailScreen
import com.example.hellocompose.presentation.ui.UserListScreen

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(navController, startDestination = "list") {
        composable("list") {
            UserListScreen(navController)
        }

        composable("detail/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")?.toIntOrNull() ?: 0
            UserDetailScreen(userId = id, navController = navController)
        }
    }
}
