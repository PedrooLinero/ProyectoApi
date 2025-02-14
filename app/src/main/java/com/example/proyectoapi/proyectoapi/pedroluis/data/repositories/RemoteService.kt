package com.example.proyectoapi.proyectoapi.pedroluis.data.repositories

import com.example.proyectoapi.proyectoapi.pedroluis.data.repositories.model.DrinkResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface RemoteService {
    @GET("search.php?f=a")
    suspend fun getDrinks(): DrinkResponse

    @GET("lookup.php")
    suspend fun getDrinkById(@Query("i") id: String): DrinkResponse
}
