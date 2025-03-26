package com.example.parchadosapp.ui.screens

import android.util.Log
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
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.services.calendar.Calendar
import com.google.api.services.calendar.CalendarScopes
import com.google.api.client.util.DateTime
import androidx.compose.ui.platform.LocalContext
import com.example.parchadosapp.ui.components.BottomNavigationBar
import com.google.api.client.json.gson.GsonFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import androidx.compose.ui.viewinterop.AndroidView
import com.github.sundeepk.compactcalendarview.CompactCalendarView
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.navigation.NavController
import com.github.sundeepk.compactcalendarview.domain.Event
import androidx.navigation.compose.rememberNavController
import java.util.Date

// Función para obtener los eventos de un calendario específico
suspend fun fetchEventsFromGoogleCalendar(service: Calendar, calendarId: String): List<com.google.api.services.calendar.model.Event> {
    return try {
        withContext(Dispatchers.IO) {
            val events = service.events().list(calendarId)
                .setMaxResults(10)
                .setTimeMin(DateTime(System.currentTimeMillis()))
                .setOrderBy("startTime")
                .setSingleEvents(true)
                .execute()

            events.items // Retorna la lista de eventos
        }
    } catch (e: Exception) {
        Log.e("GoogleCalendar", "Error fetching events: ${e.message}")
        emptyList()
    }
}

// Composable principal para la pantalla de Google Calendar
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoogleCalendarScreen(navController: NavController) {
    val context = LocalContext.current
    var events by remember { mutableStateOf<List<com.google.api.services.calendar.model.Event>>(emptyList()) }

    // Calendario específico para "Parchados"
    val calendarId = "ParchadosAppCalendarId" // Aquí debería estar el ID del calendario de "Parchados"

    val navController = rememberNavController() // Inicializamos el NavController

    Scaffold(
        bottomBar = { BottomNavigationBar(navController = navController) } // Pasamos el navController correctamente
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

            // Cargar los eventos desde Google Calendar si ya hay una cuenta activa
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

                    // Obtener eventos del calendario específico
                    events = fetchEventsFromGoogleCalendar(service, calendarId)
                }
            }

            // Mostrar los eventos en un calendario visual si hay eventos
            if (events.isEmpty()) {
                Text("No se encontraron eventos.")
            } else {
                CalendarView(events = events)
            }
        }
    }
}

@Composable
fun CalendarView(events: List<com.google.api.services.calendar.model.Event>) {
    // Creación de la vista del calendario fuera de remember
    val context = LocalContext.current
    val calendarView = remember { CompactCalendarView(context) }

    // Configuración del calendario
    calendarView.setUseThreeLetterAbbreviation(true) // Opcional
    calendarView.setBackgroundColor(Color.LightGray.toArgb()) // Color de fondo temporal
    calendarView.setCurrentDate(Date(System.currentTimeMillis())) // Establece la fecha actual como la predeterminada

    // Agregar eventos al calendario visual
    events.forEach { event ->
        Log.d("CalendarEvent", "Event: ${event.summary} on ${event.start?.dateTime}")
        val date = event.start?.dateTime?.value ?: event.start?.date?.value ?: System.currentTimeMillis()
        val calendarEvent = Event(Color.Red.toArgb(), date)
        calendarView.addEvent(calendarEvent)
    }

    // Mostrar el calendario
    AndroidView(
        modifier = Modifier
            .fillMaxWidth()
            .height(400.dp), // Ajuste de altura
        factory = { calendarView }
    )
}

// Item para cada evento
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
    val navController = rememberNavController() // Inicializa un NavController
    GoogleCalendarScreen(navController = navController) // Pasa el NavController a GoogleCalendarScreen
}
