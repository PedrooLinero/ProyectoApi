package com.example.proyectoapi.proyectoapi.pedroluis.data.repositories.model

import bebidas
import kotlinx.serialization.Serializable

@Serializable
data class DrinkResponse(
    val drinks: List<bebidas>
)
