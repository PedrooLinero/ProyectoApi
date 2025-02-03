package com.example.proyectoapi.proyectoapi.pedroluis.data.repositories

import com.example.proyectoapi.proyectoapi.pedroluis.data.repositories.model.DrinkResponse
import com.example.proyectoapi.proyectoapi.pedroluis.data.repositories.model.bebidas
import retrofit2.http.GET
import retrofit2.http.Path

// Interfaz que define los métodos para hacer peticiones a la API
interface RemoteService {
    @GET("search.php?f=a")
    suspend fun getDrinks(): DrinkResponse

    @GET("lookup.php?i={id}")
    suspend fun getDrinkById(@Path("id") id: String): DrinkResponse
}