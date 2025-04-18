package com.example.parchadosapp.ui.screens

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.parchadosapp.R
import com.example.parchadosapp.data.SessionManager.SessionManager
import com.example.parchadosapp.data.api.obtenerParchesConImagen
import com.example.parchadosapp.data.api.obtenerPersonaPorId
import com.example.parchadosapp.data.models.ParcheConImagen
import com.example.parchadosapp.data.models.ParcheRequest
import com.example.parchadosapp.ui.components.BottomNavigationBar
import com.example.parchadosapp.ui.components.PatchCardFromSupabase
import com.example.parchadosapp.ui.components.SportsCarousel
import com.example.parchadosapp.ui.theme.BrightRetro
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(navController: NavController, context: Context) {
    val scope = rememberCoroutineScope()

    var patches by remember { mutableStateOf<List<ParcheConImagen>>(emptyList()) }
    var fotoPerfil by remember { mutableStateOf<String?>(null) }

    // Traer foto de perfil y parches
    LaunchedEffect(Unit) {
        scope.launch {
            val userId = SessionManager.getUserId(context)
            userId?.let {
                val persona = obtenerPersonaPorId(it)
                fotoPerfil = persona?.foto_perfil
            }

            patches = obtenerParchesConImagen().take(4)
        }
    }

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

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                // ✅ Imagen de perfil dinámica
                if (fotoPerfil != null) {
                    Image(
                        painter = rememberAsyncImagePainter(fotoPerfil),
                        contentDescription = "Foto de perfil",
                        modifier = Modifier
                            .size(55.dp)
                            .clip(CircleShape)
                            .clickable { navController.navigate("perfil") },
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.perfil),
                        contentDescription = "Perfil por defecto",
                        modifier = Modifier
                            .size(55.dp)
                            .clip(CircleShape)
                            .clickable { navController.navigate("perfil") }
                    )
                }

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

            SportsCarousel(navController = navController)

            Spacer(modifier = Modifier.height(40.dp))

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

                itemsIndexed(patches) { _, parcheConImagen ->
                    PatchCardFromSupabase(parcheConImagen = parcheConImagen) {
                        parcheConImagen.parche.id?.let { id ->
                            navController.navigate("detalle_parche/$id")
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(120.dp))
        }

        BottomNavigationBar(navController, Modifier.align(Alignment.BottomCenter))
    }
}
