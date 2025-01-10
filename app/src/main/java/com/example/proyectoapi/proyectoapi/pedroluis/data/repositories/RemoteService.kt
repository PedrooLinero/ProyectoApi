package com.example.proyectoapi.proyectoapi.pedroluis.data.repositories

import com.example.proyectoapi.proyectoapi.pedroluis.data.model.bebidas
import retrofit2.http.GET

interface RemoteService {
    @GET("search.php?f=a")
    suspend fun getDrinks(): bebidas // Modelo de respuesta
}
