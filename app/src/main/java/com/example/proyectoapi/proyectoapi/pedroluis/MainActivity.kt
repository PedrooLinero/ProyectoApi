package com.example.proyectoapi.proyectoapi.pedroluis

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.proyectoapi.proyectoapi.pedroluis.ui.theme.ProyectoApiTheme
import Navegacion
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.remember
import com.example.proyectoapi.proyectoapi.pedroluis.data.firebase.AuthManager
import com.example.proyectoapi.proyectoapi.pedroluis.data.firebase.FirestoreManager
import com.example.proyectoapi.proyectoapi.pedroluis.ui.screen.PantallaListaScreen.Pantalla2ViewModel
import com.example.proyectoapi.proyectoapi.pedroluis.ui.viewmodel.FirestoreViewModel

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
                val factory = FirestoreViewModel.FirestoreViewModelFactory(firestoreManager)
                val viewModelFirestore = factory.create(FirestoreViewModel::class.java)
                Navegacion(auth,  Pantalla2ViewModel(firestoreManager), viewModelFirestore)
            }
        }
    }
}
