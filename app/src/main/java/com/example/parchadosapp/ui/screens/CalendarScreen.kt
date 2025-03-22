package com.example.parchadosapp.ui.screens

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.http.HttpTransport
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.services.calendar.Calendar
import com.google.api.services.calendar.CalendarScopes
import com.google.api.client.util.DateTime
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.parchadosapp.ui.components.BottomNavigationBar
import com.google.api.client.json.gson.GsonFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.navigation.compose.rememberNavController


suspend fun fetchEventsFromGoogleCalendar(service: Calendar) {
    // Llamar a Google API en el hilo secundario
    try {
        withContext(Dispatchers.IO) {
            val events = service.events().list("primary")
                .setMaxResults(10)
                .setTimeMin(DateTime(System.currentTimeMillis()))
                .setOrderBy("startTime")
                .setSingleEvents(true)
                .execute()

            // Procesar los eventos obtenidos
            val items = events.items
            items.forEach { event ->
                Log.d("GoogleCalendar", "Event: ${event.summary}")
                // Aquí puedes agregar el código para almacenar los eventos en tu base de datos
            }
        }
    } catch (e: Exception) {
        Log.e("GoogleCalendar", "Error fetching events: ${e.message}")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoogleCalendarScreen(navController: NavController) {
    val context = LocalContext.current
    var events by remember { mutableStateOf<List<com.google.api.services.calendar.model.Event>>(emptyList()) }

    Scaffold(
        bottomBar = { BottomNavigationBar(navController) } // ✅ Mantiene la barra de navegación
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = "Tus eventos de Google Calendar",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            if (events.isEmpty()) {
                Text("No se encontraron eventos.")
            } else {
                LazyColumn {
                    items(events) { event: com.google.api.services.calendar.model.Event ->
                        EventItem(event)
                    }
                }
            }
        }
    }

    // ✅ Cargar eventos si ya hay una cuenta activa
    LaunchedEffect(Unit) {
        val account = GoogleSignIn.getLastSignedInAccount(context)
        if (account != null) {
            val credential = GoogleAccountCredential.usingOAuth2(
                context, listOf(CalendarScopes.CALENDAR)
            )
            credential.selectedAccount = account.account

            val service = Calendar.Builder(
                NetHttpTransport(),
                GsonFactory.getDefaultInstance(),
                credential
            )
                .setApplicationName("ParchadosApp")
                .build()

            try {
                val fetchedEvents = withContext(Dispatchers.IO) {
                    service.events().list("primary")
                        .setMaxResults(10)
                        .setTimeMin(DateTime(System.currentTimeMillis()))
                        .setOrderBy("startTime")
                        .setSingleEvents(true)
                        .execute()
                        .items
                }
                events = fetchedEvents
            } catch (e: Exception) {
                Log.e("GoogleCalendar", "Error al cargar eventos: ${e.message}")
            }
        }
    }
}

@Composable
fun EventItem(event: com.google.api.services.calendar.model.Event) {
    val title = event.summary ?: "(Sin título)"
    val start = event.start?.dateTime ?: event.start?.date
    val dateText = start?.toStringRfc3339() ?: "Sin fecha"

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(text = title, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Fecha: $dateText", style = MaterialTheme.typography.bodySmall)
        }
    }
}



@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    GoogleCalendarScreen(navController = rememberNavController()) // ✅ Pasa un NavController falso
}