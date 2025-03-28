package com.example.parchadosapp.ui.screens

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.parchadosapp.R
import com.example.parchadosapp.ui.theme.*
import androidx.compose.ui.graphics.Color

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilScreen(navController: NavController) {
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    var showDialog by remember { mutableStateOf(false) } // Para controlar el pop-up

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? -> uri?.let { imageUri = it } }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            bitmap = data?.extras?.get("data") as? Bitmap
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted ->
            if (granted) {
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                cameraLauncher.launch(intent)
            }
        }
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F5F0)), // Fondo beige elegante
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            // Icono de "Atrás" en la parte superior izquierda
            IconButton(
                onClick = { navController.popBackStack() }, // Regresar a la pantalla anterior
                modifier = Modifier
                    .align(Alignment.Start) // Alineado a la izquierda
                    .padding(top = 16.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.atras), // Imagen del ícono de atrás
                    contentDescription = "Atrás",
                    modifier = Modifier.size(30.dp),
                    tint = Color(0xFF003F5C) // Azul profundo para el ícono
                )
            }

            // Título "Parchados" con el nombre de usuario
            Text(
                text = "Juan", // Nombre de usuario
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontFamily = BowlbyOneSC,
                    fontSize = 38.sp,
                    color = Color(0xFF003F5C) // Azul profundo
                )
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Imagen de perfil
            Box(
                modifier = Modifier
                    .size(120.dp) // Imagen más pequeña
                    .clip(CircleShape)
                    .background(Color(0xFF2F4B7C)), // Azul de fondo
                contentAlignment = Alignment.Center
            ) {
                when {
                    bitmap != null -> {
                        Image(
                            bitmap = bitmap!!.asImageBitmap(),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape)
                        )
                    }
                    imageUri != null -> {
                        Image(
                            painter = rememberAsyncImagePainter(imageUri),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape)
                        )
                    }
                    else -> {
                        Icon(
                            painter = painterResource(id = R.drawable.perfil),
                            contentDescription = null,
                            modifier = Modifier.size(70.dp), // Tamaño más pequeño de la imagen
                            tint = Color.White // Cambié a blanco para contrastar con el fondo azul
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Botones horizontales con imágenes, centrados
            Row(
                horizontalArrangement = Arrangement.Center, // Centramos los botones
                modifier = Modifier.fillMaxWidth()
            ) {
                // Botón para elegir desde galería
                IconButton(
                    onClick = { galleryLauncher.launch("image/*") },
                    modifier = Modifier
                        .size(50.dp) // Tamaño más pequeño del botón
                        .clip(CircleShape)
                        .background(Color(0xFFEAC67A)) // Amarillo de fondo
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.galeria), // Imagen de galería
                        contentDescription = "Elegir desde galería",
                        modifier = Modifier.size(30.dp), // Ícono más pequeño
                        tint = Color(0xFF003F5C) // Azul de los íconos
                    )
                }

                Spacer(modifier = Modifier.width(16.dp)) // Espacio entre los botones

                // Botón para tomar foto con cámara
                IconButton(
                    onClick = { permissionLauncher.launch(Manifest.permission.CAMERA) },
                    modifier = Modifier
                        .size(50.dp) // Tamaño más pequeño del botón
                        .clip(CircleShape)
                        .background(Color(0xFFEAC67A)) // Amarillo de fondo
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.camara), // Imagen de cámara
                        contentDescription = "Tomar foto con cámara",
                        modifier = Modifier.size(30.dp), // Ícono más pequeño
                        tint = Color(0xFF003F5C) // Azul de los íconos
                    )
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Botón azul de "Salir" al final
            Spacer(modifier = Modifier.weight(1f)) // Esto empuja el botón "Salir" hacia abajo
            Button(
                onClick = { showDialog = true }, // Mostrar el diálogo de confirmación
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp)
                    .clip(RoundedCornerShape(10.dp)),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF003F5C), // Azul de fondo
                    contentColor = Color.White // Blanco para el texto
                )
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.salir), // Logo de "Salir" (en azul)
                    contentDescription = "Salir",
                    modifier = Modifier.size(24.dp), // Tamaño del ícono
                    tint = Color.White // Blanco para el ícono
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Salir", style = MaterialTheme.typography.bodyLarge.copy(color = Color.White))
            }
        }

        // Mostrar el diálogo de confirmación cuando se presiona el botón "Salir"
        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Confirmar salida") },
                text = { Text("¿Estás seguro de que quieres salir de la aplicación?") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            navController.navigate("login") // Navegar al login si se confirma
                            showDialog = false
                        }
                    ) {
                        Text("Sí")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDialog = false }) {
                        Text("No")
                    }
                }
            )
        }
    }
}
