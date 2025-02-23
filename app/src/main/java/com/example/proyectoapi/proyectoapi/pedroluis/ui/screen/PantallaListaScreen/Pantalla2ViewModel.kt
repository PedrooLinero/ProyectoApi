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
                // Obtener cócteles desde la API
                val response = RemoteConnection.remoteService.getDrinks()
                val bebidasApi = response.drinks.map { it.toMediaItem() }

                // Obtener cócteles actualizados desde Firestore
                val bebidasFirestore = firestoreManager.getCocktails()

                // Creamos un mapa con los cócteles actualizados, usando el id como clave
                val updatedMap = bebidasFirestore.associateBy { it.idDrink }

                // Fusionamos: para cada cóctel de la API, si existe una versión actualizada en Firestore, la usamos; sino, usamos la versión de la API
                val mergedList = bebidasApi.map { apiItem ->
                    updatedMap[apiItem.idDrink] ?: apiItem
                }

                // Si existen cócteles en Firestore que no estén en la lista de la API (por ejemplo, creados manualmente), se añaden
                val additionalItems = bebidasFirestore.filter { firestoreItem ->
                    mergedList.none { it.idDrink == firestoreItem.idDrink }
                }

                // Combinar ambas listas
                _bebidas.value = mergedList + additionalItems

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
                // Primero, se consulta Firestore para ver si hay una versión modificada
                val bebidaFirestore = firestoreManager.getCocktailById(id)
                if (bebidaFirestore != null) {
                    _producto.value = bebidaFirestore
                    Log.d("Pantalla2ViewModel", "Cóctel cargado desde Firestore: ${_producto.value?.strDrink}")
                } else {
                    // Si no existe en Firestore, se consulta la API
                    val response = RemoteConnection.remoteService.getDrinkById(id)
                    if (response.drinks != null && response.drinks.isNotEmpty()) {
                        _producto.value = response.drinks.first().toMediaItem()
                        Log.d("Pantalla2ViewModel", "Cóctel cargado desde la API: ${_producto.value?.strDrink}")
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

