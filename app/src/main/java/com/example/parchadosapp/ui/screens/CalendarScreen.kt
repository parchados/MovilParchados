package com.example.parchadosapp.ui.screens


import android.util.Log
import androidx.compose.foundation.background
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.parchadosapp.ui.components.BottomNavigationBar
import com.google.api.client.json.gson.GsonFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import android.content.Context
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.example.parchadosapp.R
import com.google.api.services.calendar.model.CalendarListEntry
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoogleCalendarScreen(navController: NavController) {
    val context = LocalContext.current
    var events by remember { mutableStateOf<List<com.google.api.services.calendar.model.Event>>(emptyList()) }
    val lifecycle = LocalLifecycleOwner.current

    Scaffold(
        bottomBar = { BottomNavigationBar(navController) },
        floatingActionButton = {
            Box(
                modifier = Modifier
                    .size(60.dp) // ajusta el tamaño total del contenedor
                    .padding(4.dp)
            ) {
                IconButton(
                    onClick = {
                        createSampleEventInParchadosCalendar(context, lifecycle.lifecycleScope)
                    },
                    modifier = Modifier.fillMaxSize()
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.googlecalendar),
                        contentDescription = "Agregar evento",
                        tint = Color.Unspecified, // ✅ importante para que NO aplique ningún color
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    )

    { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF8F5F0))
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp)
            ) {
                Text(
                    text = "Tus eventos 📆 ",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF003F5C),
                    modifier = Modifier.padding(vertical = 16.dp)
                )

                if (events.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 50.dp),
                        contentAlignment = Alignment.TopCenter
                    ) {
                        Text(
                            text = "No se encontraron eventos programados.",
                            color = Color.Gray,
                            fontSize = 16.sp
                        )
                    }
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(events) { event ->
                            EventCard(event)
                        }
                    }
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        while (true) {
            val fetchedEvents = fetchEventsFromParchadosCalendar(context)
            events = fetchedEvents
            kotlinx.coroutines.delay(3000)
        }
    }
}


@Composable
fun EventCard(event: com.google.api.services.calendar.model.Event) {
    val title = event.summary ?: "(Sin título)"
    val start = event.start?.dateTime ?: event.start?.date
    val dateText = start?.toStringRfc3339() ?: "Sin fecha definida"

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(6.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color(0xFF003F5C)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "🗓 Fecha: $dateText",
                fontSize = 14.sp,
                color = Color.Gray
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    GoogleCalendarScreen(navController = rememberNavController()) // ✅ Pasa un NavController falso
}


fun createSampleEvent(context: Context) {
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
        ).setApplicationName("ParchadosApp").build()

        // Define hora de inicio y fin del evento
        val startDateTime = DateTime(System.currentTimeMillis() + 3600_000) // 1 hora desde ahora
        val endDateTime = DateTime(System.currentTimeMillis() + 7200_000)   // 2 horas desde ahora

        // Crear el objeto del evento
        val event = com.google.api.services.calendar.model.Event().apply {
            summary = "Partido con los panas ⚽"
            location = "Parque Simón Bolívar, Bogotá"
            description = "Nos reunimos a jugar un rato y pasarla bien."

            start = com.google.api.services.calendar.model.EventDateTime().setDateTime(startDateTime)
            end = com.google.api.services.calendar.model.EventDateTime().setDateTime(endDateTime)
        }

        // Ejecutar en un hilo separado
        Thread {
            try {
                val createdEvent = service.events().insert("primary", event).execute()
                Log.d("GoogleCalendar", "✅ Evento creado: ${createdEvent.htmlLink}")
            } catch (e: Exception) {
                Log.e("GoogleCalendar", "❌ Error al crear evento: ${e.message}")
            }
        }.start()
    } else {
        Log.e("GoogleCalendar", "❗ No se encontró una cuenta de Google activa")
    }
}


suspend fun getOrCreateParchadosCalendarId(service: Calendar): String? {
    return withContext(Dispatchers.IO) {
        try {
            val calendarList = service.calendarList().list().execute()
            val existing: CalendarListEntry? = calendarList.items.find { it.summary == "ParchadosApp" }
            existing?.id ?: createParchadosCalendar(service)
        } catch (e: Exception) {
            Log.e("GoogleCalendar", "❌ Error al buscar calendario: ${e.message}")
            null
        }
    }
}


fun createParchadosCalendar(service: Calendar): String? {
    return try {
        val calendar = com.google.api.services.calendar.model.Calendar().apply {
            summary = "ParchadosApp"
            timeZone = "America/Bogota"
        }

        val created = service.calendars().insert(calendar).execute()
        Log.d("GoogleCalendar", "✅ Calendario creado: ${created.id}")
        created.id
    } catch (e: Exception) {
        Log.e("GoogleCalendar", "❌ Error al crear calendario: ${e.message}")
        null
    }
}

fun createSampleEventInParchadosCalendar(
    context: Context,
    lifecycleScope: CoroutineScope
) {
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
        ).setApplicationName("ParchadosApp").build()

        val startDateTime = DateTime(System.currentTimeMillis() + 3600_000)
        val endDateTime = DateTime(System.currentTimeMillis() + 7200_000)

        val event = com.google.api.services.calendar.model.Event().apply {
            summary = "Partido con los panas ⚽"
            location = "Parque Simón Bolívar, Bogotá"
            description = "Nos reunimos a jugar un rato y pasarla bien."
            start = com.google.api.services.calendar.model.EventDateTime().setDateTime(startDateTime)
            end = com.google.api.services.calendar.model.EventDateTime().setDateTime(endDateTime)
        }

        // ✅ CORRECTAMENTE ENVUELTO EN CORRUTINA
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val calendarId = getOrCreateParchadosCalendarId(service) // ✅ Suspend function llamada correctamente
                if (calendarId != null) {
                    service.events().insert(calendarId, event).execute()
                    withContext(Dispatchers.Main) {
                        Log.d("GoogleCalendar", "✅ Evento creado en calendario $calendarId")
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.e("GoogleCalendar", "❌ Error al crear evento: ${e.message}")
                }
            }
        }
    } else {
        Log.e("GoogleCalendar", "❗ No hay cuenta activa de Google")
    }
}



suspend fun fetchEventsFromParchadosCalendar(context: Context): List<com.google.api.services.calendar.model.Event> {
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
        ).setApplicationName("ParchadosApp").build()

        return try {
            val calendarId = getOrCreateParchadosCalendarId(service)
            if (calendarId != null) {
                val events = withContext(Dispatchers.IO) {
                    service.events().list(calendarId)
                        .setMaxResults(20)
                        .setTimeMin(DateTime(System.currentTimeMillis()))
                        .setOrderBy("startTime")
                        .setSingleEvents(true)
                        .execute()
                }
                events.items
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            Log.e("GoogleCalendar", "❌ Error al cargar eventos: ${e.message}")
            emptyList()
        }
    }
    return emptyList()
}


