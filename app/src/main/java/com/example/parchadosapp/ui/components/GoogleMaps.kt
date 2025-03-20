package com.example.parchadosapp.ui.components

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

@Composable
fun GoogleMapView(context: Context) {
    val mapView = remember { MapView(context) }
    var googleMap by remember { mutableStateOf<GoogleMap?>(null) }

    // 🔹 Ubicación fija basada en la imagen proporcionada
    val fixedLocation = LatLng(4.6275028, -74.0642699)

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

    AndroidView(
        factory = { ctx ->
            mapView.apply {
                onCreate(null)
                getMapAsync { map ->
                    googleMap = map
                    map.uiSettings.isZoomControlsEnabled = true

                    // 🔹 Centrar el mapa y agregar marcador
                    val marker = MarkerOptions()
                        .position(fixedLocation)
                        .title("Ubicación Actual")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                    map.addMarker(marker)
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(fixedLocation, 15f))
                }
            }
        },
        modifier = Modifier.fillMaxSize()
    )
}
