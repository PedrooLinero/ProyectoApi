package com.example.proyectoapi.proyectoapi.pedroluis.ui.screen.PantallaListaScreen

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectoapi.proyectoapi.pedroluis.data.firebase.FirestoreManager
import com.example.proyectoapi.proyectoapi.pedroluis.data.model.MediaItem
import com.example.proyectoapi.proyectoapi.pedroluis.data.repositories.RemoteConnection
import kotlinx.coroutines.launch
import toMediaItem

// ViewModel encargado de gestionar la lista de bebidas
class Pantalla2ViewModel(
    private val firestoreManager: FirestoreManager
) : ViewModel() {

    private val _bebidas: MutableLiveData<List<MediaItem>> = MutableLiveData()
    val bebidas: LiveData<List<MediaItem>> = _bebidas

    private val _producto: MutableLiveData<MediaItem?> = MutableLiveData()
    val producto: MutableLiveData<MediaItem?> = _producto

    private val _progressBar: MutableLiveData<Boolean> = MutableLiveData(false)
    val progressBar: LiveData<Boolean> = _progressBar

    fun cargarBebidas() {
        _progressBar.value = true
        viewModelScope.launch {
            try {
                // Cargar bebidas de la API
                val response = RemoteConnection.remoteService.getDrinks()
                val bebidasApi = response.drinks.map { it.toMediaItem() }

                // Cargar bebidas de Firestore
                val bebidasFirestore = firestoreManager.getCocktails()

                // Combinar ambas listas
                val todasLasBebidas = bebidasApi + bebidasFirestore

                _bebidas.value = todasLasBebidas
                Log.d("Pantalla2ViewModel", "Bebidas cargadas: ${_bebidas.value?.size}")
            } catch (e: Exception) {
                Log.e("Pantalla2ViewModel", "Error al cargar bebidas: ${e.message}")
            } finally {
                _progressBar.value = false
            }
        }
    }

    fun cargarBebidaId(id: String) {
        _progressBar.value = true
        viewModelScope.launch {
            try {
                Log.d("Pantalla2ViewModel", "Intentando cargar cóctel con ID: $id")
                val response = RemoteConnection.remoteService.getDrinkById(id)
                Log.d("Pantalla2ViewModel", "Respuesta de la API: ${response.drinks}")

                if (response.drinks != null && response.drinks.isNotEmpty()) {
                    _producto.value = response.drinks.first().toMediaItem()
                    Log.d("Pantalla2ViewModel", "Cóctel cargado desde la API: ${_producto.value?.strDrink}")
                } else {
                    Log.d("Pantalla2ViewModel", "Cóctel no encontrado en la API. Intentando cargar desde Firestore...")
                    val bebidaFirestore = firestoreManager.getCocktailById(id)
                    if (bebidaFirestore != null) {
                        _producto.value = bebidaFirestore
                        Log.d("Pantalla2ViewModel", "Cóctel cargado desde Firestore: ${_producto.value?.strDrink}")
                    } else {
                        Log.e("Pantalla2ViewModel", "Error: No se encontró el cóctel en Firestore")
                    }
                }
            } catch (e: Exception) {
                Log.e("Pantalla2ViewModel", "Error al cargar bebida por ID: ${e.message}")
            } finally {
                _progressBar.value = false
            }
        }
    }
}

