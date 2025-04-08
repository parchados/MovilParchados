package com.example.parchadosapp.data.models

import kotlinx.serialization.Serializable

@Serializable
data class Lugar(
    val id: String,
    val nombre: String,
    val direccion: String,
    val ciudad: String,
    val descripcion: String,
    val estado: String
)
