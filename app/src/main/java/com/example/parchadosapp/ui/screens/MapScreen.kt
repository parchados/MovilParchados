package com.example.parchadosapp.ui.screens

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.parchadosapp.ui.components.BottomNavigationBar
import com.example.parchadosapp.ui.components.OpenStreetMapView

@Composable
fun MapScreen(navController: NavController, context: Context) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F5F0)) // Fondo beige
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 80.dp), // 🔹 Espacio para que el navbar no cubra contenido
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 🔹 Mapa centrado
            Spacer(modifier = Modifier.height(20.dp))
            OpenStreetMapView(context)
        }

        // 🔹 Navbar pegado abajo para navegar entre pantallas
        BottomNavigationBar(navController, Modifier.align(Alignment.BottomCenter))
    }
}
