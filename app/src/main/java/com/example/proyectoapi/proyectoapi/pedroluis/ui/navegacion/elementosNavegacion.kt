package com.example.proyectoapi.proyectoapi.pedroluis.ui.navegacion

import kotlinx.serialization.Serializable

@Serializable
object Pantalla1

@Serializable
data class Pantalla2(val usuario: String)

@Serializable
data class Pantalla3(val usuario: String, val id: String)