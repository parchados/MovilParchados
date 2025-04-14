package com.example.parchadosapp.data.models

import kotlinx.serialization.Serializable

@Serializable
data class Notificacion(
    val id: String,
    val tipo: String,
    val titulo: String,
    val descripcion: String,
    val destinatario_id: String,
    val remitente_id: String? = null,
    val referencia_id: String? = null,
    val referencia_tipo: String? = null,
    val leida: Boolean,
    val fecha_creacion: String,
    val fecha_lectura: String? = null,
    val estado: String,
    val created_at: String,
    val updated_at: String
)
