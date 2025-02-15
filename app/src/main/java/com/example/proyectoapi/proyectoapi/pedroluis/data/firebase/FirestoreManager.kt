package com.example.proyectoapi.proyectoapi.pedroluis.data.firebase

import android.content.Context
import bebidas
import com.example.proyectoapi.proyectoapi.pedroluis.data.model.MediaItem
import com.example.proyectoapi.proyectoapi.pedroluis.data.repositories.db.CarritoDB
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await

class FirestoreManager(auth: AuthManager, context: Context) {
    private val firestore = FirebaseFirestore.getInstance()
    private val userId = auth.getCurrentUser()?.uid

    companion object {
        const val PRODUCTOS = "Producto"
        const val CARRITO = "Carrito"
    }

    suspend fun getProductos(): Flow<List<bebidas>> {
        return firestore.collection(PRODUCTOS).snapshots().map { querySnapshot ->
            querySnapshot.documents.mapNotNull { document ->
                document.toObject(bebidas::class.java)
            }
        }
    }

    suspend fun getProductoById(id: String): bebidas {
        val document = firestore.collection(PRODUCTOS).document(id).get().await()
        return document.toObject(bebidas::class.java) ?: throw Exception("Producto no encontrado")
    }

    suspend fun addProducto(producto: bebidas) {
        firestore.collection(PRODUCTOS).add(producto).await()
    }

    suspend fun deleteAllProducts() {
        firestore.collection(PRODUCTOS).get().await().documents.forEach {
            it.reference.delete().await()
        }
    }

    suspend fun getCarrito(userId: String): Flow<List<CarritoDB>> {
        return firestore.collection(CARRITO)
            .whereEqualTo("idCliente", userId)
            .snapshots()
            .map { querySnapshot ->
                querySnapshot.documents.mapNotNull { document ->
                    try {
                        // Extrae datos del carrito
                        val idProducto = document.getString("idProducto") ?: return@mapNotNull null
                        val cantidad =
                            document.getLong("cantidad")?.toInt() ?: return@mapNotNull null
                        val precio = document.getDouble("precio") ?: return@mapNotNull null
                        val nombre = document.getString("nombre") ?: return@mapNotNull null
                        val imagen = document.getString("imagen") ?: return@mapNotNull null

                        // Busca el producto en Firestore
                        val productoSnapshot = firestore.collection(PRODUCTOS)
                            .document(idProducto)
                            .get()
                            .await()

                        val producto = productoSnapshot.toObject(bebidas::class.java)
                            ?: return@mapNotNull null


                        // Crea un objeto CarritoDB
                        CarritoDB(
                            idCliente = userId,
                            nombre = nombre,
                            imagen = imagen,
                            bebida = producto
                        )

                    } catch (e: Exception) {
                        null
                    }
                }
            }
            .flowOn(Dispatchers.IO)
    }

    suspend fun addCarrito(item: MediaItem, userid: String) {
        val carritoRef = firestore.collection(CARRITO)
        val docId = "${userid}_${item.idDrink}" // ID compuesto único

        firestore.runTransaction { transaction ->
            val docSnapshot = transaction.get(carritoRef.document(docId))

            if (docSnapshot.exists()) {
                // Incrementar unidades si ya existe
                transaction.update(
                    carritoRef.document(docId),
                    "unidades",
                    FieldValue.increment(1)
                )
            } else {
                // Crear nuevo registro
                val newItem = hashMapOf(
                    "idCliente" to userid,
                    "idProducto" to item.idDrink,
                    "unidades" to 1,
                    "nombre" to item.strDrink,
                    "imagen" to item.strDrinkThumb
                )
                transaction.set(carritoRef.document(docId), newItem)
            }
        }.await()
    }
}