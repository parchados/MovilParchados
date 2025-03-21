package com.example.parchadosapp

import android.Manifest
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.parchadosapp.navigation.NavGraph
import com.example.parchadosapp.ui.theme.ParchadosAppTheme
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.api.services.calendar.Calendar
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.json.JsonFactory
import com.google.api.client.http.HttpTransport
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.util.DateTime
import com.google.api.services.calendar.CalendarScopes
import com.google.android.gms.common.api.Scope
import com.google.api.client.json.gson.GsonFactory
import kotlinx.coroutines.*


class MainActivity : ComponentActivity() {

    private val RC_SIGN_IN = 9001
    private lateinit var googleSignInClient: com.google.android.gms.auth.api.signin.GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ParchadosAppTheme {
                val navController = rememberNavController()

                var hasLocationPermission by remember { mutableStateOf(false) }

                val requestPermissionLauncher =
                    rememberLauncherForActivityResult(
                        contract = ActivityResultContracts.RequestPermission()
                    ) { isGranted: Boolean ->
                        hasLocationPermission = isGranted
                    }

                // Pedir permisos al iniciar la app
                LaunchedEffect(Unit) {
                    requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                }

                // Configurar Google Sign-In
                val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .requestScopes(Scope(CalendarScopes.CALENDAR))
                    .build()

                googleSignInClient = GoogleSignIn.getClient(this, gso)

                // Iniciar el flujo de autenticación si no se ha autenticado
                val account = GoogleSignIn.getLastSignedInAccount(this)

                // Usamos LaunchedEffect para ejecutar tareas en segundo plano
                LaunchedEffect(account) {
                    if (account == null) {
                        val signInIntent = googleSignInClient.signInIntent
                        startActivityForResult(signInIntent, RC_SIGN_IN)
                    } else {
                        // Llamamos a la función suspendida desde una coroutine
                        CoroutineScope(Dispatchers.Main).launch {
                            handleSignInResult(account)
                        }
                    }
                }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavGraph(navController, this)
                }
            }
        }
    }

    // Manejar la respuesta de Google Sign-In
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: android.content.Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                // Llamamos a handleSignInResult dentro de la coroutine
                CoroutineScope(Dispatchers.Main).launch {
                    handleSignInResult(account)
                }
            } catch (e: ApiException) {
                Log.w("MainActivity", "signInResult:failed code=" + e.statusCode)
            }
        }
    }

    // Procesar el resultado del inicio de sesión
    private suspend fun handleSignInResult(account: GoogleSignInAccount?) {
        withContext(Dispatchers.IO) {
            if (account != null) {
                val credential = GoogleAccountCredential.usingOAuth2(
                    this@MainActivity, listOf(CalendarScopes.CALENDAR)
                )
                credential.selectedAccount = account.account

                // Configurar el servicio de Google Calendar
                val transport: HttpTransport = NetHttpTransport()
                val jsonFactory: JsonFactory = GsonFactory.getDefaultInstance()

                val service = Calendar.Builder(transport, jsonFactory, credential)
                    .setApplicationName("ParchadosApp")
                    .build()

                // Obtener eventos del calendario
                val events = service.events().list("primary")
                    .setMaxResults(10)
                    .setTimeMin(DateTime(System.currentTimeMillis()))
                    .setOrderBy("startTime")
                    .setSingleEvents(true)
                    .execute()

                val items = events.items
                withContext(Dispatchers.Main) {
                    // Aquí puedes actualizar la UI con los eventos obtenidos
                    items.forEach {
                        Log.d("GoogleCalendar", "Event: ${it.summary}")
                        // Puedes almacenar los eventos en tu base de datos aquí
                    }
                }
            }
        }
    }
}