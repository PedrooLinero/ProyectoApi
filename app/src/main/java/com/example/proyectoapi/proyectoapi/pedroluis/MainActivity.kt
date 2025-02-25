package com.example.proyectoapi.proyectoapi.pedroluis

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.google.firebase.auth.FirebaseAuth
import com.example.proyectoapi.proyectoapi.pedroluis.ui.theme.ProyectoApiTheme
import Navegacion
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.example.proyectoapi.proyectoapi.pedroluis.data.firebase.AuthManager
import com.example.proyectoapi.proyectoapi.pedroluis.data.firebase.FirestoreManager
import com.example.proyectoapi.proyectoapi.pedroluis.ui.screen.PantallaListaScreen.Pantalla2ViewModel
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ProyectoApiTheme {
                // Inicializar FirebaseAuth
                val auth = AuthManager()
                auth.resetAuthState()
                auth.initializeGoogleSignIn(this)
                auth.signOut()

                // Inicializar Firestore
                val firestoreManager = remember { FirestoreManager() }
                // Llamamos a la función Navegacion y le pasamos el parámetro 'auth'
                Navegacion(auth, firestoreManager, Pantalla2ViewModel(firestoreManager))
            }
        }
    }
}
