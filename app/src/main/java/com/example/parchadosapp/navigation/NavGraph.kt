package com.example.parchadosapp.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.parchadosapp.data.PatchRepository
import com.example.parchadosapp.ui.screens.BuscarScreen
import com.example.parchadosapp.ui.screens.CrearParcheDetallesScreen
import com.example.parchadosapp.ui.screens.GoogleCalendarScreen
import com.example.parchadosapp.ui.screens.HomeScreen
import com.example.parchadosapp.ui.screens.LoginScreen
import com.example.parchadosapp.ui.screens.RegisterScreen
import com.example.parchadosapp.ui.screens.SplashScreen
import com.example.parchadosapp.ui.screens.MapScreen
import com.example.parchadosapp.ui.screens.ParcheScreen
import com.example.parchadosapp.ui.screens.NotificationsScreen // ✅ Importa NotificationsScreen
import com.example.parchadosapp.ui.screens.PatchDetailScreen
import com.example.parchadosapp.ui.screens.PerfilScreen


@Composable
fun NavGraph(navController: NavHostController, context: Context) {
    NavHost(navController = navController, startDestination = "splash") {

        composable("splash") { SplashScreen(navController) }
        composable("login") { LoginScreen(navController) }
        composable("register") { RegisterScreen(navController) }
        composable("home") { HomeScreen(navController, context) }

        composable("map") {
            MapScreen(navController, context, null)
        }

        composable("map/{selectedSport}") { backStackEntry ->
            val selectedSport = backStackEntry.arguments?.getString("selectedSport")
            MapScreen(navController, context, selectedSport)
        }

        composable("parche") {
            ParcheScreen(navController, context)
        }

        // ✅ Nueva pantalla para continuar la creación del parche
        composable(
            route = "crear_parche_detalles/{nombre}/{jugadores}/{fecha}/{hora}/{nombreLugar}/{direccion}",
            arguments = listOf(
                navArgument("nombre") { type = NavType.StringType },
                navArgument("jugadores") { type = NavType.StringType },
                navArgument("fecha") { type = NavType.StringType },
                navArgument("hora") { type = NavType.StringType },
                navArgument("nombreLugar") { type = NavType.StringType },
                navArgument("direccion") { type = NavType.StringType },
            )
        ) { backStackEntry ->
            val nombre = backStackEntry.arguments?.getString("nombre") ?: ""
            val jugadores = backStackEntry.arguments?.getString("jugadores") ?: ""
            val fecha = backStackEntry.arguments?.getString("fecha") ?: ""
            val hora = backStackEntry.arguments?.getString("hora") ?: ""
            val nombreLugar = backStackEntry.arguments?.getString("nombreLugar") ?: ""
            val direccion = backStackEntry.arguments?.getString("direccion") ?: ""

            CrearParcheDetallesScreen(
                navController = navController,
                nombre = nombre,
                jugadores = jugadores,
                fecha = fecha,
                hora = hora,
                nombreLugar = nombreLugar,
                direccion = direccion
            )
        }

        composable("calendar") { GoogleCalendarScreen(navController) }
        composable("notifications") { NotificationsScreen(navController) }
        composable("perfil") { PerfilScreen(navController) }

        composable("patch_detail/{patchIndex}") { backStackEntry ->
            val patchIndex = backStackEntry.arguments?.getString("patchIndex")?.toIntOrNull()
            val patch = patchIndex?.let { PatchRepository.patches.getOrNull(it) }
            if (patch != null) {
                PatchDetailScreen(navController, patch)
            }
        }

        composable("buscar") {
            BuscarScreen(navController = navController, context = context)
        }
    }
}
