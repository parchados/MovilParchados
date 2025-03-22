package com.example.parchadosapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.example.parchadosapp.R
import com.example.parchadosapp.ui.theme.BrightRetro
import com.example.parchadosapp.ui.theme.PrimaryColor

@Composable
fun BottomNavigationBar(navController: NavController, modifier: Modifier = Modifier) {
    val currentRoute = navController.currentBackStackEntry?.destination?.route
    var selectedItem by remember { mutableStateOf(currentRoute ?: "home") }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(105.dp)
            .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
            .background(Color.White)
            .zIndex(1f),
        contentAlignment = Alignment.BottomCenter
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            NavigationBarItem(
                icon = { IconWithBackground(R.drawable.casa, isSelected = selectedItem == "home") },
                selected = selectedItem == "home",
                onClick = {
                    if (selectedItem != "home") {
                        selectedItem = "home"
                        navController.navigate("home") {
                            popUpTo("home") { inclusive = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )

            NavigationBarItem(
                icon = { IconWithBackground(R.drawable.ic_find_game, isSelected = selectedItem == "buscar") },
                selected = selectedItem == "buscar",
                onClick = { selectedItem = "buscar" }
            )

            Spacer(modifier = Modifier.width(50.dp)) // Espacio para el botón flotante

            // Botón para el calendario
            NavigationBarItem(
                icon = { IconWithBackground(R.drawable.calendar, isSelected = selectedItem == "calendar") },
                selected = selectedItem == "calendar",
                onClick = {
                    if (selectedItem != "calendar") {
                        selectedItem = "calendar"
                        navController.navigate("calendar") {
                            popUpTo("calendar") { inclusive = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )

            NavigationBarItem(
                icon = { IconWithBackground(R.drawable.mapa, isSelected = selectedItem == "map") },
                selected = selectedItem == "map",
                onClick = {
                    if (selectedItem != "map") {
                        selectedItem = "map"
                        navController.navigate("map") {
                            popUpTo("map") { inclusive = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )
        }

        // Botón flotante "P"
        Box(
            modifier = Modifier
                .offset(y = (-28).dp)
                .size(90.dp)
                .zIndex(2f),
            contentAlignment = Alignment.Center
        ) {
            FloatingActionButton(
                onClick = {
                    if (selectedItem != "parche") {
                        selectedItem = "parche"
                        navController.navigate("parche") {
                            popUpTo("parche") { inclusive = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                containerColor = PrimaryColor,
                shape = CircleShape,
                modifier = Modifier.shadow(10.dp, CircleShape)
            ) {
                Text(
                    text = "P",
                    fontSize = 40.sp,
                    fontFamily = BrightRetro,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}


/**
 * 🔹 Componente para los íconos con fondo y bordeado
 */
@Composable
fun IconWithBackground(iconId: Int, isSelected: Boolean) {
    Box(
        modifier = Modifier
            .size(50.dp) // 🔹 Ícono más grande
            .clip(CircleShape)
            .background(if (isSelected) PrimaryColor else Color(0xFFE0E0E0)), // Cambia color al seleccionar
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(id = iconId),
            contentDescription = null,
            tint = if (isSelected) Color.White else Color.Gray,
            modifier = Modifier.size(35.dp) // 🔹 Ícono más grande
        )
    }
}
