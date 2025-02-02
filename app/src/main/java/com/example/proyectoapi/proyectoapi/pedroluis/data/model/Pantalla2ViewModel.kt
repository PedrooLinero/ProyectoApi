package com.example.proyectoapi.proyectoapi.pedroluis.data.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectoapi.proyectoapi.pedroluis.data.repositories.RemoteConnection
import com.example.proyectoapi.proyectoapi.pedroluis.data.repositories.model.toMediaItem
import kotlinx.coroutines.launch

// ViewModel encargado de gestionar la lista de bebidas
class Pantalla2ViewModel : ViewModel() {
    private val _bebidas: MutableLiveData<List<MediaItem>> = MutableLiveData()
    val bebidas: LiveData<List<MediaItem>> = _bebidas

    private val _producto: MutableLiveData<MediaItem> = MutableLiveData()
    val producto: LiveData<MediaItem> = _producto

    private val _progressBar: MutableLiveData<Boolean> = MutableLiveData(false)
    val progressBar: LiveData<Boolean> = _progressBar

    // Función para obtener las bebidas de la API
    fun cargarBebidas() {
        _progressBar.value = true
        viewModelScope.launch() { // Lanzamiento de la corrutina
            val listaBebidas = RemoteConnection.remoteService.getDrinks()
            _bebidas.value = listOf(listaBebidas.toMediaItem())
            _progressBar.value = false
        }
    }

    fun cargarBebidaId(id: String) {
        _progressBar.value = true
        viewModelScope.launch() {
            val bebidaPorId = RemoteConnection.remoteService.getDrinks()
            _producto.value = bebidaPorId.toMediaItem()
            _progressBar.value = false
        }

    }
}
