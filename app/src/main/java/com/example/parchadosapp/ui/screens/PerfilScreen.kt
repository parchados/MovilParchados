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
    var showExitDialog by remember { mutableStateOf(false) } // Para salir
    var showNameDialog by remember { mutableStateOf(false) } // Para cambiar nombre
    var nombreUsuario by remember { mutableStateOf("Juan") } // Nombre editable
    var nuevoNombre by remember { mutableStateOf("") }

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
            .background(Color(0xFFF8F5F0)),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(top = 16.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.atras),
                    contentDescription = "Atrás",
                    modifier = Modifier.size(30.dp),
                    tint = Color(0xFF003F5C)
                )
            }

            Text(
                text = nombreUsuario,
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontFamily = BowlbyOneSC,
                    fontSize = 38.sp,
                    color = Color(0xFF003F5C)
                )
            )

            Spacer(modifier = Modifier.height(40.dp))

            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF2F4B7C)),
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
                            modifier = Modifier.size(70.dp),
                            tint = Color.White
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Botones de galería y cámara
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(
                    onClick = { galleryLauncher.launch("image/*") },
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFEAC67A))
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.galeria),
                        contentDescription = "Elegir desde galería",
                        modifier = Modifier.size(30.dp),
                        tint = Color(0xFF003F5C)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                IconButton(
                    onClick = { permissionLauncher.launch(Manifest.permission.CAMERA) },
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFEAC67A))
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.camara),
                        contentDescription = "Tomar foto con cámara",
                        modifier = Modifier.size(30.dp),
                        tint = Color(0xFF003F5C)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Botón para cambiar nombre
            Button(
                onClick = {
                    nuevoNombre = nombreUsuario
                    showNameDialog = true
                },
                modifier = Modifier
                    .height(50.dp)
                    .clip(RoundedCornerShape(10.dp)),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFEAC67A),
                    contentColor = Color(0xFF003F5C)
                )
            ) {
                Text("Cambiar nombre", style = MaterialTheme.typography.bodyLarge.copy(fontSize = 16.sp))
            }

            Spacer(modifier = Modifier.weight(1f))

            // Botón de Salir
            Button(
                onClick = { showExitDialog = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp)
                    .clip(RoundedCornerShape(10.dp)),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF003F5C),
                    contentColor = Color.White
                )
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.salir),
                    contentDescription = "Salir",
                    modifier = Modifier.size(24.dp),
                    tint = Color.White
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Salir", style = MaterialTheme.typography.bodyLarge.copy(color = Color.White))
            }
        }

        // Diálogo para salir
        if (showExitDialog) {
            AlertDialog(
                onDismissRequest = { showExitDialog = false },
                title = { Text("Confirmar salida") },
                text = { Text("¿Estás seguro de que quieres salir de la aplicación?") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            navController.navigate("login")
                            showExitDialog = false
                        }
                    ) {
                        Text("Sí")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showExitDialog = false }) {
                        Text("No")
                    }
                }
            )
        }

        // Diálogo para cambiar nombre
        if (showNameDialog) {
            AlertDialog(
                onDismissRequest = { showNameDialog = false },
                title = { Text("Cambiar nombre") },
                text = {
                    OutlinedTextField(
                        value = nuevoNombre,
                        onValueChange = { nuevoNombre = it },
                        label = { Text("Nuevo nombre") },
                        singleLine = true
                    )
                },
                confirmButton = {
                    TextButton(onClick = {
                        nombreUsuario = nuevoNombre
                        showNameDialog = false
                    }) {
                        Text("Guardar")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showNameDialog = false }) {
                        Text("Cancelar")
                    }
                }
            )
        }
    }
}

