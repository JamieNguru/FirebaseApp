package com.firebaseapp.chatapp.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.firebaseapp.chatapp.data.FirebaseRepository
import com.firebaseapp.chatapp.ui.screens.ChatScreen
import com.firebaseapp.chatapp.ui.screens.HomeScreen
import com.firebaseapp.chatapp.ui.screens.LoginScreen
import com.firebaseapp.chatapp.ui.screens.RegisterScreen
import com.firebaseapp.chatapp.ui.theme.ChatAppTheme
import com.firebaseapp.chatapp.ui.viewmodel.AuthViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ChatAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ChatAppNavigation()
                }
            }
            }
        }
    }

@androidx.compose.runtime.Composable
fun ChatAppNavigation() {
    val navController = rememberNavController()
    val repository = FirebaseRepository()
    
    val startDestination = if (repository.getCurrentUserId() != null) {
        "home"
    } else {
        "login"
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable("login") {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate("register")
                }
            )
        }

        composable("register") {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate("home") {
                        popUpTo("register") { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.navigate("login")
                }
            )
        }

        composable("home") {
            HomeScreen(
                onUserClick = { userId ->
                    navController.navigate("chat/$userId")
                },
                onLogout = {
                    navController.navigate("login") {
                        popUpTo("home") { inclusive = true }
                    }
                }
            )
        }

        composable("chat/{receiverId}") { backStackEntry ->
            val receiverId = backStackEntry.arguments?.getString("receiverId") ?: ""
            ChatScreen(
                receiverId = receiverId,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}

