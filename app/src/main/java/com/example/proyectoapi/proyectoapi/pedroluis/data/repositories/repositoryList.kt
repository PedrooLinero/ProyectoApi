package com.example.proyectoapi.proyectoapi.pedroluis.data.repositories

import com.example.proyectoapi.proyectoapi.pedroluis.data.model.Drink
import com.example.proyectoapi.proyectoapi.pedroluis.data.repositories.RemoteConnection

object repositoryList {

    // Función suspendida para obtener bebidas de la API
    suspend fun getListaBebidas(): List<Drink> {
        return try {
            val response = RemoteConnection.remoteService.getDrinks()
            response.drinks ?: emptyList() // Devuelve una lista vacía si no hay bebidas
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList() // Devuelve una lista vacía en caso de error
        }
    }

    // Ejemplo de llamada
    suspend fun mostrarBebidas() {
        val bebidas = repositoryList.getListaBebidas()
        bebidas.forEach { bebida ->
            println("Nombre: ${bebida.strDrink}, Imagen: ${bebida.strDrinkThumb}")

        }
    }

}
