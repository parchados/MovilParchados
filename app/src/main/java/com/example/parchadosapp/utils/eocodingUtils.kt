package com.example.parchadosapp.utils

import android.content.Context
import android.location.Geocoder
import com.google.android.gms.maps.model.LatLng
import java.util.*

fun geocodeDireccion(context: Context, direccion: String): LatLng? {
    return try {
        val geocoder = Geocoder(context, Locale.getDefault())
        val results = geocoder.getFromLocationName(direccion, 1)
        if (!results.isNullOrEmpty()) {
            val location = results[0]
            LatLng(location.latitude, location.longitude)
        } else null
    } catch (e: Exception) {
        null
    }
}

