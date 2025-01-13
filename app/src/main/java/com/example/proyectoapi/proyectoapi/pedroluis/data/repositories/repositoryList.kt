package com.example.proyectoapi.proyectoapi.pedroluis.data.repositories

import com.example.proyectoapi.proyectoapi.pedroluis.data.model.Drink

object repositoryList {

    // Función suspendida para obtener bebidas de la API
    suspend fun getListaBebidas(): List<Drink> {
        return try {
            val response = RemoteConnection.remoteService.getDrinks()
            response.drinks  // Devuelve una lista vacía si no hay bebidas
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList() // Devuelve una lista vacía en caso de error
        }
    }

    // Nueva función que recibe un ID y devuelve la bebida seleccionada
    suspend fun getBebidaPorId(id: String): Drink? {
        return try {
            val response = getListaBebidas()
            return response.find { it.idDrink == id }
        } catch (e: Exception) {
            e.printStackTrace()
            null // Devuelve null en caso de error
        }
    }

}
