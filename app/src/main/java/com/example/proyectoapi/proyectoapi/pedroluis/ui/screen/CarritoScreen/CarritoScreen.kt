package com.example.proyectoapi.proyectoapi.pedroluis.ui.screen.CarritoScreen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
import com.example.proyectoapi.proyectoapi.pedroluis.data.firebase.AuthManager
import com.example.proyectoapi.proyectoapi.pedroluis.data.firebase.FirestoreViewModel

@Composable
fun CarritoScreen(
    auth: AuthManager,
    viewModel: FirestoreViewModel,
    navigateToBack: () -> Unit,
    navigateToLogin: () -> Unit,
    navigateToProfile: () -> Unit
) {
    val listaCarrito by viewModel.carrito.observeAsState(emptyList())
    val progressBar by viewModel.isLoading.observeAsState(false)
    val user = auth.getCurrentUser()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.getCarrito(auth.getCurrentUser()?.uid, context)
    }


}