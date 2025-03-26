package com.example.parchadosapp.ui.screens

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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

import com.example.parchadosapp.ui.components.Patch
import com.example.parchadosapp.ui.components.PatchCard
import com.example.parchadosapp.ui.components.SportsCarousel
import com.example.parchadosapp.ui.theme.BrightRetro

@Composable
fun HomeScreen(navController: NavController, context: Context) {
    // Lista de parches
    val patches = listOf(
        Patch(
            image = R.drawable.campo_futbol, // Imagen del lugar
            name = "Campo de Fútbol A",
            address = "Calle Fútbol 123",
            date = "Sábado, 24 de Marzo",
            time = "3:00 PM",
            remaining = 5,
            sport = "Fútbol"
        ),
        Patch(
            image = R.drawable.cancha_basket, // Imagen del lugar
            name = "Cancha de Baloncesto B",
            address = "Avenida Basket 456",
            date = "Domingo, 25 de Marzo",
            time = "6:00 PM",
            remaining = 3,
            sport = "Baloncesto"
        ),
        Patch(
            image = R.drawable.billarl, // Imagen del lugar
            name = "Sala de Billar Central",
            address = "Calle Billar 789",
            date = "Lunes, 26 de Marzo",
            time = "8:00 PM",
            remaining = 2,
            sport = "Billar"
        ),
        Patch(
            image = R.drawable.cancha_tenis, // Imagen del lugar
            name = "Cancha de Tenis A",
            address = "Avenida Tenis 321",
            date = "Martes, 27 de Marzo",
            time = "5:00 PM",
            remaining = 4,
            sport = "Tenis"
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F5F0)), // Fondo beige
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
            // 🔹 Sección superior (Perfil + Título + Notificaciones)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.perfil),
                    contentDescription = "Perfil",
                    modifier = Modifier
                        .size(55.dp)
                        .clip(CircleShape)
                        .clickable {
                            navController.navigate("perfil")
                        }
                )

                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    text = "Parchados",
                    fontSize = 42.sp,
                    fontFamily = BrightRetro,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF003F5C),
                    modifier = Modifier.padding(horizontal = 24.dp)
                )

                Spacer(modifier = Modifier.width(16.dp))

                Box(
                    modifier = Modifier.size(55.dp),
                    contentAlignment = Alignment.Center
                ) {
                    IconButton(onClick = {
                        navController.navigate("notifications")
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.notificacion),
                            contentDescription = "Notificaciones",
                            tint = Color(0xFF003F5C),
                            modifier = Modifier.size(40.dp)
                        )
                    }
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .align(Alignment.TopEnd)
                            .background(Color.Red, shape = CircleShape)
                    )
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            // 🔹 Carrusel de deportes
            SportsCarousel(navController = navController, onSportSelected = { selectedSport ->

                navController.navigate("map/$selectedSport")
            })

            Spacer(modifier = Modifier.height(40.dp))

            // 🔹 Lista de parches disponibles envuelta en LazyColumn
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)  // Esto hace que ocupe el espacio disponible antes del navbar
            ) {
                item {
                    Text(
                        text = "Parchando Ahora",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF003F5C),
                        modifier = Modifier.padding(vertical = 16.dp)
                    )
                }
                items(patches) { patch ->
                    PatchCard(patch = patch)
                }
            }

            // Agregar un espacio al final para evitar que el navbar lo tape
            Spacer(modifier = Modifier.height(120.dp)) // Aumenté el espacio a 120dp
        }

        // 🔹 Navbar en la parte inferior
        BottomNavigationBar(navController, Modifier.align(Alignment.BottomCenter))
    }
}