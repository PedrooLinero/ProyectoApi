package com.example.proyectoapi.proyectoapi.pedroluis

import Navegacion
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import com.example.proyectoapi.proyectoapi.pedroluis.ui.theme.ProyectoApiTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProyectoApiTheme {
                // Llamamos a la función Navegacion aquí
                Navegacion()
            }
        }
    }
}
