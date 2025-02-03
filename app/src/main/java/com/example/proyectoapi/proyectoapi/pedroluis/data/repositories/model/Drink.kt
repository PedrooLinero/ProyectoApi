package com.example.proyectoapi.proyectoapi.pedroluis.data.repositories.model

import kotlinx.serialization.Serializable

@Serializable
data class DrinkResponse(
    val drinks: List<bebidas>
)
