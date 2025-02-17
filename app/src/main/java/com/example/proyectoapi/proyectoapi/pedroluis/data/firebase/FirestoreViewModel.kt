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


    fun addCarrito(item: MediaItem, userid: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                firestoreManager.addCarrito(item, userid)
                _syncState.value = SyncState.Success("Producto añadido al carrito")
            } catch (e: Exception) {
                _syncState.value = SyncState.Error(e)
            }
        }
        _isLoading.value = false
    }

    fun recargarEstadoSync() {
        _syncState.value = SyncState.Loading
    }


    fun getCarrito(userid: String?, context: Context) {
        _isLoading.value = true
        viewModelScope.launch {
            if (userid != null) {
                firestoreManager.getCarrito(userid).collect { carritoDB ->
                    _carrito.value = carritoDB.mapNotNull { it.bebida as? bebidas }
                }
            } else {
                Toast.makeText(context, "Usuario no encontrado", Toast.LENGTH_SHORT).show()
            }
            _isLoading.value = false
        }
    }


    class FirestoreViewModelFactory(
        private val firestoreManager: FirestoreManager
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return FirestoreViewModel(firestoreManager) as T
        }
    }
}