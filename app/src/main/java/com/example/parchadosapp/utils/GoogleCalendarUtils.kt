package com.example.parchadosapp.utils


import android.content.Context
import android.util.Log
import com.example.parchadosapp.data.models.ParcheRequest
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.util.DateTime
import com.google.api.services.calendar.CalendarScopes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import com.example.parchadosapp.ui.screens.getOrCreateParchadosCalendarId
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.services.calendar.Calendar
import com.google.api.client.json.gson.GsonFactory
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*



fun createParcheEventInGoogleCalendar(
    context: Context,
    lifecycleScope: CoroutineScope,
    parche: ParcheRequest // <-- objeto real del parche
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

        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        val startDate = formatter.parse("${parche.fecha} ${parche.hora_inicio}")
        val endDate = formatter.parse("${parche.fecha} ${parche.hora_fin}")

        val startDateTime = DateTime(startDate)
        val endDateTime = DateTime(endDate)


        val event = com.google.api.services.calendar.model.Event().apply {
            summary = parche.nombre
            location = "Escenario Deportivo"
            description = parche.descripcion
            start = com.google.api.services.calendar.model.EventDateTime()
                .setDateTime(startDateTime).setTimeZone("America/Bogota")
            end = com.google.api.services.calendar.model.EventDateTime()
                .setDateTime(endDateTime).setTimeZone("America/Bogota")
        }

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val calendarId = getOrCreateParchadosCalendarId(service)
                if (calendarId != null) {
                    service.events().insert(calendarId, event).execute()
                    withContext(Dispatchers.Main) {
                        Log.d("GoogleCalendar", "✅ Evento creado para el parche")
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.e("GoogleCalendar", "❌ Error al crear evento del parche: ${e.message}")
                }
            }
        }
    } else {
        Log.e("GoogleCalendar", "❗ No hay cuenta activa de Google")
    }
}

