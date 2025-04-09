package com.example.parchadosapp.data.models

import kotlinx.serialization.Serializable

@Serializable
data class Espacio(
    val id: String,
    val lugar_id: String,
    val nombre: String,
    val tipo: String,
    val capacidad: Int,
    val precio_hora: Double,
    val descripcion: String,
    val disponibilidad: String,
    val caract_cesped_natural: Boolean,
    val caract_iluminacion: Boolean,
    val caract_vestuarios: Boolean,
    val caract_gradas: Boolean,
    val caract_techado: Boolean,
    val caract_tipo_superficie: String
)
