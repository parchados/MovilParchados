package com.example.parchadosapp.data.models

import kotlinx.serialization.Serializable

@Serializable
data class ParcheRequest(
    val id: String? = null,
    val espacio_id: String,
    val creador_id: String,
    val nombre: String,
    val descripcion: String,
    val fecha: String,         // formato: YYYY-MM-DD
    val hora_inicio: String,   // formato: HH:mm
    val hora_fin: String,      // formato: HH:mm
    val cupo_maximo: Int,
    val estado: String = "Activo",
    val deporte: String
)

