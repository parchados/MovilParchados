package com.example.parchadosapp.ui.screens

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
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

@Composable
fun HomeScreen(navController: NavController, context: Context) {
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
            // 🟡 Sección de perfil y logo
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_profile),
                    contentDescription = "Perfil",
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                )
                Text(
                    text = "Parchados",
                    fontSize = 32.sp,
                    fontFamily = BrightRetro,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF003F5C) // Azul profundo
                )
                IconButton(onClick = { /* Acción de chat */ }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_chat),
                        contentDescription = "Chat",
                        tint = Color(0xFF003F5C)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // 🔹 Barra de accesos rápidos
            QuickActionsBar()

            Spacer(modifier = Modifier.height(20.dp))

            // 🔹 Sección "Destacados"
            Text(
                text = "DESTACADOS",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF003F5C),
                modifier = Modifier.align(Alignment.Start)
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                FeatureCard(
                    title = "Cómo jugar",
                    description = "Aprende lo básico y posicionamiento en la cancha.",
                    icon = R.drawable.ic_court
                )
                FeatureCard(
                    title = "Clínica Virtual",
                    description = "Entrena con videos de profesionales.",
                    icon = R.drawable.ic_video
                )
            }
        }

        // 🔹 Navbar pegado abajo
        BottomNavigationBar(navController, Modifier.align(Alignment.BottomCenter))
    }
}

@Composable
fun QuickActionsBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFD1E8FF), shape = RoundedCornerShape(20.dp)) // Azul claro
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        QuickActionItem("Crear juego", R.drawable.ic_add_game)
        QuickActionItem("Buscar juegos", R.drawable.ic_find_game)
        QuickActionItem("Tomar lección", R.drawable.ic_book)
        QuickActionItem("Aprender a jugar", R.drawable.ic_learn)
        QuickActionItem("Encontrar cancha", R.drawable.ic_court)
    }
}

@Composable
fun QuickActionItem(label: String, iconId: Int) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            painter = painterResource(id = iconId),
            contentDescription = label,
            tint = Color(0xFF003F5C),
            modifier = Modifier.size(30.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = label, fontSize = 10.sp, textAlign = TextAlign.Center)
    }
}

@Composable
fun FeatureCard(title: String, description: String, icon: Int, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth() // 🔹 Ahora ocupa todo el ancho posible
            .padding(8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth() // 🔹 Asegura que el contenido no se colapse
                .padding(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = title,
                tint = Color(0xFF003F5C),
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF003F5C) // 🔹 Asegurar color visible
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = description,
                fontSize = 12.sp,
                color = Color.Gray,
                maxLines = 2 // 🔹 Evitar que el texto sea demasiado largo
            )
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavController, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp) // 🔹 Ajuste de altura
            .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)) // 🔹 Bordes superiores redondeados
            .background(Color.White), // 🔹 Fondo blanco
        contentAlignment = Alignment.BottomCenter
    ) {
        NavigationBar(
            containerColor = Color.White,
            tonalElevation = 8.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            NavigationBarItem(
                icon = { Icon(Icons.Filled.Home, contentDescription = "Inicio") },
                label = { Text("Hoy") },
                selected = true,
                onClick = { /* Navegar a Inicio */ }
            )
            NavigationBarItem(
                icon = { Icon(Icons.Filled.LocationOn, contentDescription = "Cerca") },
                label = { Text("Cerca") },
                selected = false,
                onClick = { /* Navegar a Cerca */ }
            )

            // 🔹 Botón flotante central (+)
            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(top = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                FloatingActionButton(
                    onClick = { /* Acción al presionar + */ },
                    containerColor = Color(0xFF003F5C),
                    shape = CircleShape,
                    modifier = Modifier.size(60.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Nuevo Juego",
                        tint = Color.White
                    )
                }
            }

            NavigationBarItem(
                icon = { Icon(Icons.Filled.Create, contentDescription = "Juegos") },
                label = { Text("Juegos") },
                selected = false,
                onClick = { /* Navegar a Juegos */ }
            )
            NavigationBarItem(
                icon = { Icon(Icons.Filled.Person, contentDescription = "Grupos") },
                label = { Text("Grupos") },
                selected = false,
                onClick = { /* Navegar a Grupos */ }
            )
        }
    }
}
