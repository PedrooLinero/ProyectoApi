package com.example.proyectoapi.proyectoapi.pedroluis.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.proyectoapi.proyectoapi.pedroluis.data.firebase.FirestoreManager
import com.example.proyectoapi.proyectoapi.pedroluis.data.model.MediaItem
import com.example.proyectoapi.proyectoapi.pedroluis.data.repositories.RemoteConnection
import kotlinx.coroutines.launch
import toMediaItem

class FirestoreViewModel(
    val firestoreManager: FirestoreManager
) : ViewModel() {

    private val _bebidas: MutableLiveData<List<MediaItem>> = MutableLiveData()
    val bebidas: LiveData<List<MediaItem>> = _bebidas

    private val _producto: MutableLiveData<MediaItem?> = MutableLiveData()
    val producto: MutableLiveData<MediaItem?> = _producto

    private val _progressBar: MutableLiveData<Boolean> = MutableLiveData(false)
    val progressBar: LiveData<Boolean> = _progressBar

    private val _favorites = MutableLiveData<List<MediaItem>>()
    val favorites: LiveData<List<MediaItem>> get()= _favorites

    private val _syncState = MutableLiveData<SyncState?>()

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    sealed class SyncState {
        object Loading : SyncState()
        data class Success(val message: String) : SyncState()
        data class Error(val exception: Throwable) : SyncState()
    }

    // Añadir cóctel
    fun addCocktail(mediaItem: MediaItem) {
        viewModelScope.launch {
            _isLoading.value = true
            _syncState.value = SyncState.Loading
            try {
                firestoreManager.addCocktail(mediaItem)
                _syncState.value = SyncState.Success("Cóctel añadido")
            } catch (e: Exception) {
                _syncState.value = SyncState.Error(e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Actualizar cóctel
    fun updateCocktail(mediaItem: MediaItem) {
        viewModelScope.launch {
            _isLoading.value = true
            _syncState.value = SyncState.Loading
            try {
                firestoreManager.updateCocktail(mediaItem)
                _syncState.value = SyncState.Success("Cóctel actualizado")
            } catch (e: Exception) {
                _syncState.value = SyncState.Error(e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Cargar favoritos
    fun loadFavorites() {
        viewModelScope.launch {
            _isLoading.value = true
            _syncState.value = SyncState.Loading
            try {
                val favorites = firestoreManager.getFavorites()
                _favorites.value = favorites
                _syncState.value = SyncState.Success("Favoritos cargados")
            } catch (e: Exception) {
                _syncState.value = SyncState.Error(e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Añadir favorito
    fun addFavorite(mediaItem: MediaItem) {
        viewModelScope.launch {
            _isLoading.value = true
            _syncState.value = SyncState.Loading
            try {
                firestoreManager.addFavorite(mediaItem)
                _syncState.value = SyncState.Success("Favorito añadido")
            } catch (e: Exception) {
                _syncState.value = SyncState.Error(e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Eliminar favorito
    fun removeFavorite(idDrink: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _syncState.value = SyncState.Loading
            try {
                firestoreManager.removeFavorite(idDrink)
                _syncState.value = SyncState.Success("Favorito eliminado")
            } catch (e: Exception) {
                _syncState.value = SyncState.Error(e)
            } finally {
                _isLoading.value = false
            }
        }
    }
    fun cargarBebidaId(id: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // Intenta obtener el cóctel desde Firestore
                val bebidaFirestore = firestoreManager.getCocktailById(id)
                if (bebidaFirestore != null) {
                    _producto.value = bebidaFirestore
                } else {
                    // Si no existe en Firestore, puedes optar por obtenerlo desde la API (opcional)
                    val response = RemoteConnection.remoteService.getDrinkById(id)
                    if (response.drinks != null && response.drinks.isNotEmpty()) {
                        _producto.value = response.drinks.first().toMediaItem()
                    }
                }
            } catch (e: Exception) {
                _syncState.value = SyncState.Error(e)
            } finally {
                _isLoading.value = false
            }
        }
    }



    // Factory para ViewModel
    class FirestoreViewModelFactory(
        private val firestoreManager: FirestoreManager
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(FirestoreViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return FirestoreViewModel(firestoreManager) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}