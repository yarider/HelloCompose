package com.example.hellocompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.example.hellocompose.navigation.AppNavHost
import com.example.hellocompose.ui.theme.LabScreensTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LabScreensTheme {
                val navController = rememberNavController()
                AppNavHost(navController)
            }
        }
    }
}
