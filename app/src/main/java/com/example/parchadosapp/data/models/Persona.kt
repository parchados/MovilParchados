package com.example.parchadosapp.data.models

import kotlinx.serialization.Serializable

@Serializable
data class Persona(
    val id: String,
    val patron_id: String? = null,
    val nombre: String,
    val email: String,
    val telefono: String,
    val rol: String,
    val estado: String,
    val contrasena: String,
    val foto_perfil: String? = null
)
