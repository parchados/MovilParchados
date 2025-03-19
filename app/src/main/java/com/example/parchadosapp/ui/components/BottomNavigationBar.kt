package com.example.parchadosapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.parchadosapp.R
import com.example.parchadosapp.ui.theme.BrightRetro
import com.example.parchadosapp.ui.theme.PrimaryColor // Asegúrate de definir este color en tu tema

@Composable
fun BottomNavigationBar(navController: NavController, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp)
            .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)) // Bordes redondeados arriba
            .background(Color.White),
        contentAlignment = Alignment.BottomCenter
    ) {
        NavigationBar(
            containerColor = Color.White,
            tonalElevation = 8.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            NavigationBarItem(
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.casa),
                        contentDescription = "Inicio",
                        modifier = Modifier.size(28.dp)
                    )
                },
                selected = true,
                onClick = { /* Navegar a Inicio */ }
            )
            NavigationBarItem(
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_find_game),
                        contentDescription = "Buscar",
                        modifier = Modifier.size(28.dp)
                    )
                },
                selected = false,
                onClick = { /* Navegar a Buscar */ }
            )

            // 🔹 Botón flotante con "P"
            Box(
                modifier = Modifier
                    .offset(y = (-35).dp) // Ajuste para que sobresalga del navbar
                    .size(65.dp),
                contentAlignment = Alignment.Center
            ) {
                FloatingActionButton(
                    onClick = { /* Acción al presionar P */ },
                    containerColor = PrimaryColor,
                    shape = CircleShape
                ) {
                    Text(
                        text = "P",
                        fontSize = 24.sp,
                        fontFamily = BrightRetro, // Fuente del título
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )
                }
            }

            NavigationBarItem(
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.calendar),
                        contentDescription = "Calendario",
                        modifier = Modifier.size(28.dp)
                    )
                },
                selected = false,
                onClick = { /* Navegar a Calendario */ }
            )
            NavigationBarItem(
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.mapa),
                        contentDescription = "Mapa",
                        modifier = Modifier.size(28.dp)
                    )
                },
                selected = false,
                onClick = { /* Navegar a Mapa */ }
            )
        }
    }
}

