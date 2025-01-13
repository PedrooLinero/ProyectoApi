package com.example.proyectoapi.proyectoapi.pedroluis.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectoapi.proyectoapi.pedroluis.data.model.Drink
import com.example.proyectoapi.proyectoapi.pedroluis.data.repositories.repositoryList
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

// ViewModel encargado de gestionar la lista de bebidas
class Pantalla2ViewModel : ViewModel() {

    // Flow mutable para mantener y actualizar la lista de bebidas
    private val _bebidas = MutableStateFlow<List<Drink>>(emptyList()) // Lista vacía por defecto
    val bebidas: StateFlow<List<Drink>> = _bebidas // Flow inmutable para que no se pueda modificar

    // Inicialización de la lista de bebidas
    init {
        // Llamada para obtener las bebidas de la API
        obtenerBebidas()
    }

    // Función para obtener las bebidas de la API
    private fun obtenerBebidas() {
        viewModelScope.launch { // Lanzamiento de la corrutina
            val listaBebidas = repositoryList.getListaBebidas() // Llamada a la función de la API
            _bebidas.value = listaBebidas // Actualización de la lista de bebidas
        }
    }
}
