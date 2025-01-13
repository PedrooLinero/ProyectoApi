package com.example.proyectoapi.proyectoapi.pedroluis.data.repositories

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// conexión remota con la API
object RemoteConnection {
    private val retrofit = Retrofit.Builder() // configuración de la conexión
        .baseUrl("https://www.thecocktaildb.com/api/json/v1/1/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    //Se crea e inicializa la interfaz `RemoteService`, que define los métodos para interactuar con la API
    val remoteService: RemoteService = retrofit.create(RemoteService::class.java)
}
