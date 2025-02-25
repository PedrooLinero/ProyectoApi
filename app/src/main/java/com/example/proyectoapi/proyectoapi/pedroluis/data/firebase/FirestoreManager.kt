package com.example.proyectoapi.proyectoapi.pedroluis.data.firebase

import android.util.Log
import com.example.proyectoapi.proyectoapi.pedroluis.data.model.MediaItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

import kotlinx.coroutines.tasks.await

class FirestoreManager{
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private fun getUserId(): String? = auth.currentUser?.uid

    suspend fun addCocktail(mediaItem: MediaItem) {
        val userId = getUserId() ?: return
        try {
            firestore.collection("users")
                .document(userId)
                .collection("cocktails")
                .document(mediaItem.idDrink)
                .set(mediaItem)
                .await()
            println("Cóctel añadido para el usuario $userId: ${mediaItem.idDrink}")
        } catch (e: Exception) {
            println("Error al añadir cóctel: ${e.message}")
        }
    }


    suspend fun getCocktails(): List<MediaItem> {
        val userId = getUserId() ?: return emptyList()
        return try {
            val snapshot = firestore.collection("users")
                .document(userId)
                .collection("cocktails")
                .get()
                .await()
            snapshot.documents.mapNotNull { it.toObject(MediaItem::class.java) }
        } catch (e: Exception) {
            println("Error al obtener cócteles: ${e.message}")
            emptyList()
        }
    }

    suspend fun updateCocktail(mediaItem: MediaItem) {
        val userId = getUserId() ?: return
        try {
            firestore.collection("users")
                .document(userId)
                .collection("cocktails")
                .document(mediaItem.idDrink)
                .set(mediaItem)  // Sobreescribe el documento con los nuevos datos
                .await()
            Log.d("FirestoreManager", "Cóctel actualizado: ${mediaItem.idDrink}")
        } catch (e: Exception) {
            Log.e("FirestoreManager", "Error al actualizar cóctel: ${e.message}")
        }
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
    suspend fun getCocktailById(id: String): MediaItem? {
        val userId = getUserId() ?: return null
        return try {
            val document = firestore.collection("users")
                .document(userId)
                .collection("cocktails")
                .document(id)  // Buscar el cóctel dentro de la colección de cócteles del usuario
                .get()
                .await()

            if (document.exists()) {
                document.toObject(MediaItem::class.java)
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e("FirestoreManager", "Error al obtener cóctel por ID: ${e.message}")
            null
        }
    }

}