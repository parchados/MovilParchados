package com.example.parchadosapp.ui.screens

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
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
import com.example.parchadosapp.data.PatchRepository
import com.example.parchadosapp.ui.components.BottomNavigationBar

import com.example.parchadosapp.ui.components.Patch
import com.example.parchadosapp.ui.components.PatchCard
import com.example.parchadosapp.ui.components.SportsCarousel
import com.example.parchadosapp.ui.theme.BrightRetro

@Composable
fun HomeScreen(navController: NavController, context: Context) {
    // Lista de parches
    val patches = PatchRepository.patches

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F5F0)),
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
            SportsCarousel(navController = navController)

            Spacer(modifier = Modifier.height(40.dp))

            // 🔹 Lista de parches disponibles
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
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
                itemsIndexed(patches) { index, patch ->
                    PatchCard(patch = patch) {
                        navController.navigate("patch_detail/$index")
                    }
                }
            }

            Spacer(modifier = Modifier.height(120.dp))
        }

        BottomNavigationBar(navController, Modifier.align(Alignment.BottomCenter))
    }
}