package com.example.proyectoapi.proyectoapi.pedroluis.data.firebase

import com.example.proyectoapi.proyectoapi.pedroluis.data.model.MediaItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

import kotlinx.coroutines.tasks.await

class FirestoreManager{
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private fun getUserId(): String? = auth.currentUser?.uid

     suspend fun addCocktail(mediaItem: MediaItem) {
        try {
            firestore.collection("cocktails")  // Colección global para los cócteles
                .document(mediaItem.idDrink)  // Usar el id único del cóctel
                .set(mediaItem)  // Guardar el objeto completo
                .await()
            println("Cóctel añadido correctamente a Firestore: ${mediaItem.idDrink}")
        } catch (e: Exception) {
            println("Error al añadir cóctel: ${e.message}")
        }
    }
    
    suspend fun getCocktails(): List<MediaItem> {
        return try {
            val snapshot = firestore.collection("cocktails").get().await()
            snapshot.documents.mapNotNull { it.toObject(MediaItem::class.java) }
        } catch (e: Exception) {
            println("Error al obtener cócteles: ${e.message}")
            emptyList()
        }
    }

    suspend fun removeCocktail(idDrink: String) {
        firestore.collection("cocktails")
            .document(idDrink)
            .delete()
            .await()
    }

    suspend fun addFavorite(mediaItem: MediaItem) {
        val userId = getUserId() ?: return
        try {
            firestore.collection("users")
                .document(userId)
                .collection("favorites")
                .document(mediaItem.idDrink)
                .set(mediaItem)
                .await()
            println("Producto añadido correctamente a Firestore: ${mediaItem.idDrink}")
        } catch (e: Exception) {
            println("Error al añadir producto: ${e.message}")
        }
    }


    suspend fun getFavorites(): List<MediaItem>{
        val userId = getUserId() ?: return emptyList()
        val snapshot = firestore.collection("users")
            .document(userId)
            .collection("favorites")
            .get()
            .await()

        return snapshot.documents.mapNotNull { it.toObject(MediaItem::class.java) }
    }

    suspend fun removeFavorite(idDrink: String) {
        val userId = getUserId() ?: return
        firestore.collection("users")
            .document(userId)
            .collection("favorites")
            .document(idDrink)
            .delete()
            .await()
    }

    // Obtener un cóctel por ID de Firestore
    suspend fun getCocktailById(idDrink: String): MediaItem? {
        return try {
            val snapshot = firestore.collection("cocktails")
                .document(idDrink)
                .get()
                .await()
            snapshot.toObject(MediaItem::class.java)
        } catch (e: Exception) {
            println("Error al obtener cóctel por ID: ${e.message}")
            null
        }
    }
}