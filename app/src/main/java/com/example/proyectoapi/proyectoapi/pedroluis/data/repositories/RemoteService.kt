package com.example.proyectoapi.proyectoapi.pedroluis.data.repositories

import com.example.proyectoapi.proyectoapi.pedroluis.data.repositories.model.bebidas
import retrofit2.http.GET
import retrofit2.http.Path

// Interfaz que define los métodos para hacer peticiones a la API
interface RemoteService {
    // Método para hacer una solicitud HTTP GET a la API
    @GET("search.php?f=a")
    suspend fun getDrinks(): bebidas // Modelo de respuesta de la API
}
