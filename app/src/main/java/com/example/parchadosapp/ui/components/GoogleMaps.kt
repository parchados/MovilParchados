package com.example.parchadosapp.ui.components

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import androidx.compose.ui.Alignment
import com.example.parchadosapp.R


@Composable
fun GoogleMapView(context: Context) {
    val mapView = remember { MapView(context) }
    var googleMap by remember { mutableStateOf<GoogleMap?>(null) }

    // Coordenadas específicas (de la imagen)
    val fixedLocation = LatLng(4.6275, -74.0643)

    LaunchedEffect(Unit) {
        googleMap?.let { map ->
            map.clear()
            val marker = MarkerOptions()
                .position(fixedLocation)
                .title("Ubicación Actual")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))

            map.addMarker(marker)
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(fixedLocation, 15f))
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            factory = { ctx ->
                mapView.apply {
                    onCreate(null)
                    getMapAsync { map ->
                        googleMap = map
                        map.uiSettings.isZoomControlsEnabled = true
                        map.uiSettings.isMyLocationButtonEnabled = false

                        val marker = MarkerOptions()
                            .position(fixedLocation)
                            .title("Ubicación Actual")
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))

                        map.addMarker(marker)
                        map.animateCamera(CameraUpdateFactory.newLatLngZoom(fixedLocation, 15f))
                    }
                }
            },
            modifier = Modifier.fillMaxSize()
        )

        // 🔹 Botón para centrar el mapa en la ubicación fija
        FloatingActionButton(
            onClick = {
                googleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(fixedLocation, 15f))
            },
            modifier = Modifier
                .align(Alignment.BottomStart) // Botón en la izquierda
                .padding(16.dp),
            containerColor = MaterialTheme.colorScheme.primary
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_location), // Asegúrate de tener este icono
                contentDescription = "Centrar Mapa"
            )
        }
    }
}


/**
 * Obtiene la ubicación actual del usuario de forma asincrónica.
 */
@SuppressLint("MissingPermission")
suspend fun getUserLocation(context: Context): LatLng? {
    return try {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        val location: Location = fusedLocationClient.getLastLocationAsync() ?: return null
        LatLng(location.latitude, location.longitude)
    } catch (e: Exception) {
        Log.e("GoogleMapView", "Error obteniendo ubicación: ${e.message}")
        null
    }
}

/**
 * Función de extensión para obtener la última ubicación de forma asincrónica.
 */
@SuppressLint("MissingPermission")
suspend fun FusedLocationProviderClient.getLastLocationAsync(): Location? {
    return suspendCancellableCoroutine { cont ->
        lastLocation.addOnSuccessListener { location ->
            cont.resume(location)
        }.addOnFailureListener { e ->
            cont.resumeWithException(e)
        }
    }
}
