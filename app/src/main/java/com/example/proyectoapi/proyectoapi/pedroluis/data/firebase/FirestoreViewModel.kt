package com.example.proyectoapi.proyectoapi.pedroluis.data.firebase

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import bebidas
import com.example.proyectoapi.proyectoapi.pedroluis.data.model.MediaItem
import com.example.proyectoapi.proyectoapi.pedroluis.data.repositories.db.CarritoDB
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch


class FirestoreViewModel(
    private val firestoreManager: FirestoreManager
) : ViewModel() {

    private val _firestoreProducts = MutableLiveData<List<bebidas>>()
    val firestoreProducts: LiveData<List<bebidas>> = _firestoreProducts

    private val _firestoreProduct = MutableLiveData<bebidas>()
    val firestoreProduct: LiveData<bebidas> = _firestoreProduct

    private val _carrito = MutableLiveData<List<bebidas>>()
    val carrito: LiveData<List<bebidas>> = _carrito

    private val _syncState = MutableLiveData<SyncState>()
    val syncState: LiveData<SyncState> = _syncState

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    sealed class SyncState {
        object Loading : SyncState()
        data class Success(val message: String) : SyncState()
        data class Error(val exception: Throwable) : SyncState()
    }

    // Cargar productos de Firestore
    suspend fun loadFirestoreProducts() {
        viewModelScope.launch {
            _syncState.value = SyncState.Loading
            try {
                firestoreManager.getProductos()
                    .collect { productos ->
                        _firestoreProducts.value = productos
                    }
            } catch (e: Exception) {
                _syncState.value = SyncState.Error(e)
            }
        }
    }

    // Comparar y sincronizar productos
    suspend fun syncProducts(apiProducts: List<MediaItem>) {  // Cambiado a List<ApiBebida> si es necesario
        viewModelScope.launch {
            _syncState.value = SyncState.Loading
            _isLoading.value = true
            try {
                val firestoreProducts = _firestoreProducts.value ?: emptyList()

                // Convierte apiProducts si es necesario
                val apiProductosConvertidos = apiProducts.map { item ->
                    item.toBebidas()
                }

                if (debenSincronizarse(apiProductosConvertidos, firestoreProducts)) {
                    firestoreManager.deleteAllProducts()
                    apiProductosConvertidos.forEach { producto ->
                        firestoreManager.addProducto(producto)
                    }
                } else {
                    _syncState.value = SyncState.Success("No se necesitan cambios")
                }
            } catch (e: Exception) {
                _syncState.value = SyncState.Error(e)
            }
            _isLoading.value = false
        }
    }


    suspend fun cargarProductoPorId(id: String) {
        _isLoading.value = true
        viewModelScope.launch {
            val producto = firestoreManager.getProductoById(id)
            _firestoreProduct.value = producto
        }
        _isLoading.value = false
    }

    // Lógica de comparación para saber si hay que sincronizar los datos de Firestore
    private fun debenSincronizarse(
        apiList: List<bebidas>,  // Cambiado de List<Unit> a List<bebidas>
        firestoreList: List<bebidas>
    ): Boolean {
        return apiList.size != firestoreList.size || !apiList.containsAll(firestoreList)
    }

    fun addCarrito(item: bebidas, userid: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val mediaItem = convertBebidasToMediaItem(item)
                firestoreManager.addCarrito(mediaItem, userid) // Ahora pasas un objeto de tipo 'MediaItem'
                _syncState.value = SyncState.Success("Producto añadido al carrito")
            } catch (e: Exception) {
                _syncState.value = SyncState.Error(e)
            }
        }
        _isLoading.value = false
    }

    // Función para convertir 'bebidas' a 'MediaItem'
    fun convertBebidasToMediaItem(bebida: bebidas): MediaItem {
        return MediaItem(
            idDrink = bebida.idDrink, // Asegúrate de que las propiedades sean las mismas
            strDrink = bebida.strDrink, // Nombre de la bebida
            strDrinkThumb = bebida.strDrinkThumb // URL de la imagen
            // Agrega las demás propiedades si es necesario
        )
    }


    fun getCarrito(userid: String?, context: Context) {
        _isLoading.value = true
        viewModelScope.launch {
            val carrito: Flow<List<CarritoDB>>
            if (userid != null) {
                carrito = firestoreManager.getCarrito(userid)
                carrito.collect { carritoDB ->
                    _carrito.value = carritoDB.map {
                        it.bebida
                    }
                }
            } else {
                Toast.makeText(context, "Usuario no encontrado", Toast.LENGTH_SHORT).show()
            }
        }
        _isLoading.value = false
    }


//    fun marcarComoFavorito(item: MediaItem, userId: String) {
//        val db = FirebaseFirestore.getInstance()
//        val favoritoRef = db.collection("usuarios").document(userId).collection("favoritos").document(item.idDrink)
//
//        favoritoRef.set(item) // Guarda el cóctel como favorito
//    }
//
//    fun eliminarFavorito(item: MediaItem, userId: String) {
//        val db = FirebaseFirestore.getInstance()
//        val favoritoRef = db.collection("usuarios").document(userId).collection("favoritos").document(item.idDrink)
//
//        favoritoRef.delete() // Elimina el cóctel de favoritos
//    }
//
//    fun getFavoritos(userId: String): List<MediaItem> {
//        val db = FirebaseFirestore.getInstance()
//        val favoritosRef = db.collection("usuarios").document(userId).collection("favoritos")
//        val favoritos = mutableListOf<MediaItem>()
//
//        // Aquí podemos hacer una consulta para obtener los favoritos del usuario
//        favoritosRef.get().addOnSuccessListener { result ->
//            for (document in result) {
//                val coctel = document.toObject(MediaItem::class.java)
//                favoritos.add(coctel)
//            }
//        }
//
//        return favoritos
//    }


    fun recargarEstadoSync() {
        _syncState.value = SyncState.Loading
    }

    class FirestoreViewModelFactory(
        private val firestoreManager: FirestoreManager
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return FirestoreViewModel(firestoreManager) as T
        }
    }
}