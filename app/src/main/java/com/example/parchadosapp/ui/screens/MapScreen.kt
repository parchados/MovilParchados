package com.example.parchadosapp.ui.screens

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.parchadosapp.R
import com.example.parchadosapp.ui.components.BottomNavigationBar
import com.example.parchadosapp.ui.components.GoogleMapView
import com.example.parchadosapp.ui.theme.BrightRetro

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(navController: NavController, context: Context) {
    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF8F5F0)) // Fondo beige
                .padding(paddingValues),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // 🔹 Sección superior con perfil, título y notificaciones
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    // 🔹 Imagen de perfil
                    Image(
                        painter = painterResource(id = R.drawable.perfil),
                        contentDescription = "Perfil",
                        modifier = Modifier
                            .size(55.dp)
                            .clip(CircleShape)
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    // 🔹 Título centrado
                    Text(
                        text = "Parchados",
                        fontSize = 42.sp,
                        fontFamily = BrightRetro,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF003F5C),
                        modifier = Modifier.padding(horizontal = 24.dp)
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    // 🔹 Icono de notificaciones con insignia roja
                    Box(
                        modifier = Modifier.size(55.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        IconButton(onClick = { /* Acción de notificaciones */ }) {
                            Icon(
                                painter = painterResource(id = R.drawable.notificacion),
                                contentDescription = "Notificaciones",
                                tint = Color(0xFF003F5C),
                                modifier = Modifier.size(40.dp)
                            )
                        }
                        // 🔹 Insignia roja
                        Box(
                            modifier = Modifier
                                .size(12.dp)
                                .align(Alignment.TopEnd)
                                .background(Color.Red, shape = CircleShape)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 🔹 Mapa de Google con ubicación actual
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    GoogleMapView(context)
                }
            }
        }
    }
}
