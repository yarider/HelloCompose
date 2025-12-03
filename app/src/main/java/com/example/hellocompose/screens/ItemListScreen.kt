package com.example.hellocompose.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.hellocompose.model.Item
import com.example.hellocompose.model.sampleItems

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemListScreen(
    items: List<Item>,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onItemClick: (Item) -> Unit,
    onMenuClick: () -> Unit
) {
    Scaffold(
        topBar = {
            MainTopAppBar(
                searchQuery = searchQuery,
                onSearchQueryChange = onSearchQueryChange,
                onMenuClick = onMenuClick
            )
        }
    ) { padding ->
        ItemList(
            items = items,
            onItemClick = onItemClick,
            modifier = Modifier.padding(padding)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTopAppBar(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onMenuClick: () -> Unit
) {
    var isSearchActive by remember { mutableStateOf(false) }

    if (isSearchActive) {
        // Пошуковий режим
        TopAppBar(
            title = {
                TextField(
                    value = searchQuery,
                    onValueChange = onSearchQueryChange,
                    placeholder = { Text("Пошук...") },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                        disabledContainerColor = MaterialTheme.colorScheme.surface,
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            },
            navigationIcon = {
                IconButton(onClick = { isSearchActive = false }) {
                    Icon(Icons.Default.Close, contentDescription = "Закрити пошук")
                }
            }
        )
    } else {
        // Звичайний режим
        CenterAlignedTopAppBar(
            title = { Text("LabScreensApp") },
            navigationIcon = {
                IconButton(onClick = onMenuClick) {
                    Icon(Icons.Default.Menu, contentDescription = "Меню")
                }
            },
            actions = {
                IconButton(onClick = { isSearchActive = true }) {
                    Icon(Icons.Default.Search, contentDescription = "Пошук")
                }
            }
        )
    }
}

@Composable
fun ItemList(
    items: List<Item>,
    onItemClick: (Item) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        items(items) { item ->
            ListItem(
                headlineContent = { Text(item.title) },
                supportingContent = { Text(item.description) },
                leadingContent = {
                    Icon(
                        imageVector = when (item.id % 5) {
                            0 -> Icons.Default.Star
                            1 -> Icons.Default.Favorite
                            2 -> Icons.Default.Build
                            3 -> Icons.Default.Home
                            else -> Icons.Default.Info
                        },
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
                modifier = Modifier.clickable { onItemClick(item) }
            )
            HorizontalDivider()
        }
    }
}
