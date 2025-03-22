package com.example.parchadosapp.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.parchadosapp.ui.screens.GoogleCalendarScreen
import com.example.parchadosapp.ui.screens.HomeScreen
import com.example.parchadosapp.ui.screens.LoginScreen
import com.example.parchadosapp.ui.screens.RegisterScreen
import com.example.parchadosapp.ui.screens.SplashScreen
import com.example.parchadosapp.ui.screens.MapScreen // 🔹 Se importa la nueva pantalla
import com.example.parchadosapp.ui.screens.ParcheScreen

@Composable
fun NavGraph(navController: NavHostController, context: Context) {
    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") { SplashScreen(navController) }
        composable("login") { LoginScreen(navController) }
        composable("register") { RegisterScreen(navController) }
        composable("home") { HomeScreen(navController, context) }
        composable("map") { MapScreen(navController, context) }
        composable("parche") { ParcheScreen(navController, context) }
        composable(route = "calendar") { GoogleCalendarScreen(navController) }
    }
}
