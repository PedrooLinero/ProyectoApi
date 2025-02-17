package com.example.proyectoapi.proyectoapi.pedroluis.data.firebase

import android.content.Context
import bebidas
import com.example.proyectoapi.proyectoapi.pedroluis.data.model.MediaItem
import com.example.proyectoapi.proyectoapi.pedroluis.data.repositories.db.CarritoDB
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await

class FirestoreManager{
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private fun getUserId(): String? = auth.currentUser?.uid

    suspend fun addFavorite(mediaItem: MediaItem){
        val userId = getUserId() ?: return
        val snapshot = firestore.collection("users")
            .document(userId)
            .collection("favorites")
            .document(mediaItem.idDrink.toString())
            .set(mediaItem)
            .await()
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
            .document(idDrink.toString())
            .delete()
            .await()
    }
}