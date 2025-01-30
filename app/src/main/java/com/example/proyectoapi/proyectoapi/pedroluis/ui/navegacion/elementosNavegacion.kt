package com.example.proyectoapi.proyectoapi.pedroluis.ui.navegacion

import kotlinx.serialization.Serializable

@Serializable
object Login

@Serializable
object SignUp

@Serializable
object ForgotPassword

@Serializable
object Pantalla1

@Serializable
data class Pantalla2(val usuario: String)

@Serializable
data class Pantalla3(val idDrink: String, val usuario: String)

