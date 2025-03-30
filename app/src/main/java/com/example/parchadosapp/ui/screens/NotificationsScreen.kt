package com.example.parchadosapp.ui.screens

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.compose.ui.platform.LocalContext
import androidx.compose.material3.Snackbar
import androidx.core.app.ActivityCompat
import com.example.parchadosapp.R
import com.example.parchadosapp.ui.theme.SecondaryColor
import kotlinx.coroutines.launch

// Función para verificar si el permiso de notificación está concedido
fun checkNotificationPermission(context: Context): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        // Verificar si el permiso fue concedido en Android 13 o superior
        ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
    } else {
        // Para versiones anteriores, no es necesario pedir el permiso
        true
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsScreen(navController: NavController) {
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val userName = "Juan"

    val notificationsList = remember { mutableStateListOf<String>() }
    var showDeleteDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        if (notificationsList.isEmpty()) {
            notificationsList.addAll(
                listOf(
                    "🎉 ¡$userName, nuevo torneo de fútbol este sábado!",
                    "📅 $userName, has sido invitado a un partido de tenis",
                    "🕒 $userName, recuerda tu partido de baloncesto a las 5PM"
                )
            )
        }
    }

    // Función para crear canal de notificación
    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Default Channel"
            val descriptionText = "Canal para notificaciones"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("default_channel", name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    // ✅ FUNCION CLAVE: VERIFICACIÓN DE PERMISOS DE NOTIFICACIÓN
    fun checkNotificationPermission(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Verificar si el permiso fue concedido en Android 13 o superior
            ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            // Para versiones anteriores, no es necesario pedir el permiso
            true
        }
    }

    // Función para enviar la notificación
    fun sendNotification(context: Context) {
        createNotificationChannel(context)

        val notificationId = (System.currentTimeMillis() % Int.MAX_VALUE).toInt()

        val notification = NotificationCompat.Builder(context, "default_channel")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Nueva Notificación")
            .setContentText("¡Hora de Parchar! 🎉")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(context).notify(notificationId, notification)
    }

    // Solicitud de permiso (Android 13+)
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            sendNotification(context)
            val mensaje = "📢 $userName, tienes una nueva notificación enviada manualmente"
            notificationsList.add(0, mensaje)
            coroutineScope.launch {
                snackbarHostState.showSnackbar("¡Tienes una nueva notificación!")
            }
        } else {
            println("Permiso para notificaciones denegado.")
        }
    }

    // Función que envía notificación a sistema y agrega a la UI
    fun sendNotificationToUI() {
        val mensaje = "📢 $userName, tienes una nueva notificación enviada manualmente"

        if (checkNotificationPermission(context)) {
            sendNotification(context)
            notificationsList.add(0, mensaje)

            coroutineScope.launch {
                snackbarHostState.showSnackbar("¡Tienes una nueva notificación!")
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            } else {
                sendNotification(context)
                notificationsList.add(0, mensaje)

                coroutineScope.launch {
                    snackbarHostState.showSnackbar("¡Tienes una nueva notificación!")
                }
            }
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

            // Botón Enviar
            Button(
                onClick = { sendNotificationToUI() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFEAC67A),
                    contentColor = Color(0xFF003F5C)
                )
            ) {
                Text(text = "Enviar Notificación", fontSize = 18.sp)
            }

            // Botón Eliminar con diálogo de confirmación
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
                notificationsList.forEach { notification ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Text(
                            text = notification,
                            modifier = Modifier.padding(16.dp),
                            fontSize = 16.sp,
                            color = Color(0xFF003F5C)
                        )
                    }
                }
            }

            // Diálogo de confirmación
            if (showDeleteDialog) {
                AlertDialog(
                    onDismissRequest = { showDeleteDialog = false },
                    title = { Text("Confirmar eliminación") },
                    text = { Text("¿Estás seguro de que quieres eliminar todas las notificaciones?") },
                    confirmButton = {
                        TextButton(onClick = {
                            notificationsList.clear()
                            showDeleteDialog = false
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



