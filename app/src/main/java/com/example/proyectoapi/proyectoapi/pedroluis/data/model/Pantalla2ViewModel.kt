package com.example.proyectoapi.proyectoapi.pedroluis.data.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectoapi.proyectoapi.pedroluis.data.repositories.model.Drink
import com.example.proyectoapi.proyectoapi.pedroluis.data.repositories.repositoryList
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

// ViewModel encargado de gestionar la lista de bebidas
class Pantalla2ViewModel : ViewModel() {

    // Flow mutable para mantener y actualizar la lista de bebidas
    private val _bebidas = MutableStateFlow<List<Drink>>(emptyList()) // Lista vacía por defecto
    val bebidas: StateFlow<List<Drink>> = _bebidas // Flow inmutable para que no se pueda modificar

    private val _producto: MutableLiveData<MediaItem> = MutableLiveData()
    val producto: LiveData<MediaItem> = _producto

    private val _progressBar: MutableLiveData<Boolean> = MutableLiveData(false)
    val progressBar: LiveData<Boolean> = _progressBar



    // Función para obtener las bebidas de la API
    private fun obtenerBebidas() {
        _progressBar.value = true
        viewModelScope.launch() { // Lanzamiento de la corrutina
            val listaBebidas = repositoryList.getListaBebidas()
            _bebidas.value = listaBebidas.map {
                it.toMediaItem()
            }
            _progressBar.value = false
        }
    }
}
