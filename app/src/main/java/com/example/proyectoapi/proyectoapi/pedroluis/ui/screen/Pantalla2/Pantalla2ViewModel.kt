package com.example.proyectoapi.proyectoapi.pedroluis.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectoapi.proyectoapi.pedroluis.data.model.Drink
import com.example.proyectoapi.proyectoapi.pedroluis.data.repositories.repositoryList
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class Pantalla2ViewModel : ViewModel() {

    // Flow para la lista de bebidas
    private val _bebidas = MutableStateFlow<List<Drink>>(emptyList())
    val bebidas: StateFlow<List<Drink>> = _bebidas

    init {
        // Llamada para obtener las bebidas de la API
        obtenerBebidas()
    }

    private fun obtenerBebidas() {
        viewModelScope.launch {
            val listaBebidas = repositoryList.getListaBebidas()
            _bebidas.value = listaBebidas
        }
    }
}
