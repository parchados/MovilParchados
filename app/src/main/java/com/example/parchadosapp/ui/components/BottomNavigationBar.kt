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
import com.example.parchadosapp.ui.theme.PrimaryColor // Asegúrate de definir este color en tu tema

@Composable
fun BottomNavigationBar(navController: NavController, modifier: Modifier = Modifier) {
    var selectedItem by remember { mutableStateOf("home") } // 🔹 Estado para la pestaña activa

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(105.dp) // 🔹 Aumenta la altura para mejor alineación
            .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)) // 🔹 Bordes redondeados arriba
            .background(Color.White)
            .zIndex(1f), // 🔹 Asegura que el navbar esté sobre otros elementos
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
                onClick = { selectedItem = "home" }
            )
            NavigationBarItem(
                icon = { IconWithBackground(R.drawable.ic_find_game, isSelected = selectedItem == "buscar") },
                selected = selectedItem == "buscar",
                onClick = { selectedItem = "buscar" }
            )

            Spacer(modifier = Modifier.width(50.dp)) // 🔹 Espacio para el botón flotante

            NavigationBarItem(
                icon = { IconWithBackground(R.drawable.calendar, isSelected = selectedItem == "calendario") },
                selected = selectedItem == "calendario",
                onClick = { selectedItem = "calendario" }
            )
            NavigationBarItem(
                icon = { IconWithBackground(R.drawable.mapa, isSelected = selectedItem == "mapa") },
                selected = selectedItem == "mapa",
                onClick = { selectedItem = "mapa" }
            )
        }

        // 🔹 Botón flotante "P" más bajo y con tipografía más grande
        Box(
            modifier = Modifier
                .offset(y = (-30).dp) // 🔹 Lo bajamos más para que se vea completamente
                .size(85.dp)
                .zIndex(2f), // 🔹 Asegura que esté sobre los demás elementos
            contentAlignment = Alignment.Center
        ) {
            FloatingActionButton(
                onClick = { /* Acción al presionar P */ },
                containerColor = PrimaryColor,
                shape = CircleShape,
                modifier = Modifier.shadow(10.dp, CircleShape) // 🔹 Agrega sombra para destacar más
            ) {
                Text(
                    text = "P",
                    fontSize = 36.sp, // 🔹 P MÁS GRANDE
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
            .size(42.dp)
            .clip(CircleShape)
            .background(if (isSelected) PrimaryColor else Color(0xFFE0E0E0)), // 🔹 Color de fondo dinámico
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(id = iconId),
            contentDescription = null,
            tint = if (isSelected) Color.White else Color.Gray, // 🔹 Ícono cambia de color al seleccionarse
            modifier = Modifier.size(30.dp) // 🔹 Ícono más grande
        )
    }
}
