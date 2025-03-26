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
import com.example.parchadosapp.ui.screens.MapScreen
import com.example.parchadosapp.ui.screens.ParcheScreen
import com.example.parchadosapp.ui.screens.NotificationsScreen // ✅ Importa NotificationsScreen
import com.example.parchadosapp.ui.screens.PerfilScreen

@Composable
fun NavGraph(navController: NavHostController, context: Context) {
    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") { SplashScreen(navController) }
        composable("login") { LoginScreen(navController) }
        composable("register") { RegisterScreen(navController) }
        composable("home") { HomeScreen(navController, context) }
        composable("map") {
            MapScreen(navController, context, null)  // Aquí no pasas filtro, simplemente 'null'
        }

// Ruta con filtro (cuando se hace clic en una card del carrusel)
        composable("map/{selectedSport}") { backStackEntry ->
            val selectedSport = backStackEntry.arguments?.getString("selectedSport")
            MapScreen(navController, context, selectedSport)  // Aquí pasas el filtro
        }
        composable("parche") { ParcheScreen(navController, context) }
        composable("calendar") { GoogleCalendarScreen(navController) }
        composable("notifications") { NotificationsScreen(navController) }


        composable("perfil") { PerfilScreen(navController) }
    }
}
