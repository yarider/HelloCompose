package com.example.hellocompose.navigation

import androidx.compose.runtime.*
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.hellocompose.model.sampleItems
import com.example.hellocompose.screens.ItemDetailsScreen
import com.example.hellocompose.screens.ItemListScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    var searchQuery by remember { mutableStateOf("") }
    
    // Фільтруємо елементи на основі пошукового запиту
    val filteredItems = remember(searchQuery) {
        if (searchQuery.isEmpty()) {
            sampleItems
        } else {
            sampleItems.filter { item ->
                item.title.contains(searchQuery, ignoreCase = true) ||
                item.description.contains(searchQuery, ignoreCase = true)
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = "list"
    ) {
        composable("list") {
            ItemListScreen(
                items = filteredItems,
                searchQuery = searchQuery,
                onSearchQueryChange = { query ->
                    searchQuery = query
                },
                onItemClick = { item ->
                    navController.navigate("details/${item.id}")
                },
                onMenuClick = {
                    // Тут можна додати функціонал відкриття бокового меню
                }
            )
        }

        composable("details/{itemId}") { backStackEntry ->
            val itemId = backStackEntry.arguments?.getString("itemId")?.toIntOrNull()
            val item = sampleItems.find { it.id == itemId }

            if (item != null) {
                ItemDetailsScreen(
                    item = item,
                    onBack = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}
