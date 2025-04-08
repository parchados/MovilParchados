package com.example.parchadosapp.data.models

import kotlinx.serialization.Serializable

@Serializable
data class TodoItem(
    val id: Int,
    val name: String
)
