package com.example.proyectoapi.proyectoapi.pedroluis

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.google.firebase.auth.FirebaseAuth
import com.example.proyectoapi.proyectoapi.pedroluis.ui.theme.ProyectoApiTheme
import Navegacion
import com.example.proyectoapi.proyectoapi.pedroluis.data.firebase.AuthManager

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializar FirebaseAuth
        val auth = AuthManager()
        auth.resetAuthState()
        auth.initializeGoogleSignIn(this)
        auth.signOut()

        setContent {
            ProyectoApiTheme {
                // Llamamos a la función Navegacion y le pasamos el parámetro 'auth'
                Navegacion(auth)
            }
        }
    }
}
