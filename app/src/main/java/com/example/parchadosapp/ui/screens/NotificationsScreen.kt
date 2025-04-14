package com.example.parchadosapp.ui.screens


import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.ui.platform.LocalContext
import com.example.parchadosapp.R
import com.example.parchadosapp.data.SessionManager.SessionManager
import com.example.parchadosapp.data.api.eliminarNotificacionesPorUsuario
import com.example.parchadosapp.data.api.marcarNotificacionComoLeida
import com.example.parchadosapp.data.api.obtenerNotificacionesPorUsuario
import com.example.parchadosapp.data.models.Notificacion
import com.example.parchadosapp.ui.theme.SecondaryColor
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsScreen(navController: NavController) {
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    var showDeleteDialog by remember { mutableStateOf(false) }
    val notificationsList = remember { mutableStateListOf<Notificacion>() }

    // 🔁 Obtener notificaciones reales al iniciar
    LaunchedEffect(Unit) {
        val userId = SessionManager.getUserId(context)
        if (userId != null) {
            val notificaciones = obtenerNotificacionesPorUsuario(userId)
            notificationsList.clear()
            notificationsList.addAll(notificaciones)
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF8F5F0))
                .padding(padding)
                .padding(16.dp)
        ) {
            // Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        painter = painterResource(id = R.drawable.atras),
                        contentDescription = "Atrás",
                        tint = Color(0xFF003F5C),
                        modifier = Modifier.size(32.dp)
                    )
                }
                Text(
                    text = "Notificaciones",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF003F5C)
                )
            }

            // Botón eliminar
            Button(
                onClick = { showDeleteDialog = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = SecondaryColor,
                    contentColor = Color.White
                )
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.eliminar),
                    contentDescription = "Eliminar",
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Eliminar Notificaciones")
            }

            // Lista de notificaciones
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                notificationsList.forEachIndexed { index, noti ->
                    Box {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp)
                                .clickable(enabled = !noti.leida) {
                                    coroutineScope.launch {
                                        val success = marcarNotificacionComoLeida(noti.id)
                                        if (success) {
                                            notificationsList[index] = noti.copy(leida = true)
                                            snackbarHostState.showSnackbar("Notificación marcada como leída")
                                        }
                                    }
                                },
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(4.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = noti.titulo,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF003F5C)
                                )
                                Text(
                                    text = noti.descripcion,
                                    fontSize = 14.sp,
                                    color = Color.Gray
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "📅 ${noti.fecha_creacion.substring(0, 10)}",
                                    fontSize = 12.sp,
                                    color = Color(0xFF003F5C)
                                )
                            }
                        }

                        // 🔴 Circulito rojo para no leídas
                        if (!noti.leida) {
                            Box(
                                modifier = Modifier
                                    .size(12.dp)
                                    .background(Color.Red, CircleShape)
                                    .align(Alignment.TopEnd)
                                    .offset(x = (-8).dp, y = (-4).dp)
                            )
                        }
                    }
                }
            }

            // Diálogo para eliminar todo
            if (showDeleteDialog) {
                AlertDialog(
                    onDismissRequest = { showDeleteDialog = false },
                    title = { Text("Confirmar eliminación") },
                    text = { Text("¿Estás seguro de que quieres eliminar todas las notificaciones?") },
                    confirmButton = {
                        TextButton(onClick = {
                            coroutineScope.launch {
                                val userId = SessionManager.getUserId(context)
                                val success = userId?.let { eliminarNotificacionesPorUsuario(it) } ?: false
                                if (success) {
                                    notificationsList.clear()
                                    Toast.makeText(context, "Notificaciones eliminadas", Toast.LENGTH_SHORT).show()
                                } else {
                                    Toast.makeText(context, "Error eliminando notificaciones", Toast.LENGTH_SHORT).show()
                                }
                                showDeleteDialog = false
                            }
                        }) {
                            Text("Sí")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showDeleteDialog = false }) {
                            Text("No")
                        }
                    }
                )
            }
        }
    }
}


