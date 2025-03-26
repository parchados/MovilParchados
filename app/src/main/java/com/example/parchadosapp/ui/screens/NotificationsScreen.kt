package com.example.parchadosapp.ui.screens

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.ComponentActivity
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

@Composable
fun NotificationsScreen(navController: NavController) {
    val context = LocalContext.current
    var showNotification by remember { mutableStateOf(false) } // Estado para controlar la visualización de la Snackbar

    // Función para crear el canal de notificación (solo necesario para Android 8.0 o superior)
    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Default Channel"
            val descriptionText = "Channel for notifications"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("default_channel", name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    // Función para enviar una notificación al teléfono
    fun sendNotification(context: Context, notificationId: Int) {
        // Verificar si el permiso de notificación está concedido
        if (checkNotificationPermission(context)) {
            createNotificationChannel(context)  // Crear el canal si es necesario

            val notification = NotificationCompat.Builder(context, "default_channel")
                .setSmallIcon(R.drawable.ic_launcher_foreground) // Cambiar con tu icono
                .setContentTitle("Nueva Notificación")
                .setContentText("¡Hora de Parchar! 🎉")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .build()

            val notificationManager = NotificationManagerCompat.from(context)
            notificationManager.notify(notificationId, notification)
        } else {
            // Si no se tiene el permiso, manejar el error o pedir permiso
            println("Permiso para notificaciones no concedido.")
        }
    }

    // Función para simular el envío de una notificación
    fun sendNotificationToUI() {
        sendNotification(context, 1) // Enviar la notificación al teléfono
        showNotification = true // Mostrar la notificación entrante en la UI
    }

    // Función para solicitar el permiso si es necesario
    @Composable
    fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val activity = context as ComponentActivity
            val requestPermissionLauncher = activity.activityResultRegistry.register(
                "requestNotificationPermission",
                ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                if (isGranted) {
                    // Permiso concedido, puedes enviar notificaciones
                    sendNotificationToUI()
                } else {
                    // Permiso no concedido
                    println("Permiso para notificaciones denegado.")
                }
            }

            LaunchedEffect(Unit) {
                // Solicitar el permiso
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        } else {
            // No es necesario pedir permiso en versiones anteriores a Android 13
            sendNotificationToUI()
        }
    }

    // Comprobamos si el permiso está concedido antes de enviar notificaciones
    if (checkNotificationPermission(context)) {
        sendNotificationToUI()
    } else {
        requestNotificationPermission()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F5F0)) // Fondo gris claro
            .padding(16.dp)
    ) {
        // 🔙 Flecha de regreso
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

        // 🔔 Botón para simular el envío de una notificación
        Button(
            onClick = { sendNotificationToUI() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            Text(text = "Enviar Notificación", fontSize = 18.sp)
        }

        // Mostrar notificación en la parte superior como un "Snackbar"
        if (showNotification) {
            Snackbar(
                modifier = Modifier.padding(16.dp),
                action = {
                    TextButton(onClick = { showNotification = false }) {
                        Text("Cerrar")
                    }
                }
            ) {
                Text("¡Tienes una nueva notificación!")
            }
        }

        // 🔔 Lista de notificaciones (con scroll)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            val userName = "Juan" // Nombre provisional

            val notificationsList = listOf(
                "🎉 ¡$userName, nuevo torneo de fútbol este sábado!",
                "📅 $userName, has sido invitado a un partido de tenis",
                "🕒 $userName, recuerda tu partido de baloncesto a las 5PM",
                "📍 $userName, hay una nueva ubicación disponible para jugar billar",
                "👥 $userName, tienes 3 nuevos parches cerca",
                "💬 Álvaro te envió un mensaje, $userName",
                "🚨 $userName, se canceló un partido por clima",
                "✅ $userName, tu reserva ha sido confirmada",
                "🏆 $userName, subiste 2 posiciones en el ranking",
                "⚠️ $userName, no olvides confirmar tu asistencia al evento"
            )

            // Mostrar notificaciones
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
    }
}