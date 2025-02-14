package com.example.proyectoapi.proyectoapi.pedroluis.ui.screen.CarritoScreen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bebidas
import com.example.proyectoapi.proyectoapi.pedroluis.data.firebase.AuthManager
import com.example.proyectoapi.proyectoapi.pedroluis.data.firebase.FirestoreViewModel

@OptIn(ExperimentalMaterial3Api::class)
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
        viewModel.getCarrito(user?.uid, context)
    }

    if (user != null) {
        Scaffold(
            topBar = {
                val nombre = user.displayName?.split(" ")?.firstOrNull() ?: "Invitado"
                TopAppBar(
                    title = {
                        Text(
                            text = "Carrito",
                            style = MaterialTheme.typography.titleLarge,
                            fontSize = 24.sp
                        )
                    },
                    actions = {
                        IconButton(
                            onClick = {
                                navigateToProfile()
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Perfil",
                                tint = Color.White
                            )
                        }
                    }
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp)
            ) {
                if (progressBar) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                } else {
                    Text(
                        text = "Carrito de ${user.displayName?.split(" ")?.firstOrNull() ?: "Invitado"}",
                        style = MaterialTheme.typography.displayMedium,
                        fontSize = 40.sp
                    )
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 20.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        items(listaCarrito) { producto ->
                            GenerarLineaPedido(producto)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun GenerarLineaPedido(producto: bebidas) {
    var offsetX by remember { mutableFloatStateOf(0f) }
    val maxOffset = 100f

    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        // Box del botón eliminar
        Box(
            modifier = Modifier
                .width(110.dp)
                .height(150.dp)
                .align(Alignment.CenterStart)
                .background(
                    color = Color.Red,
                    shape = RoundedCornerShape(topStart = 12.dp, bottomStart = 12.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                IconButton(
                    onClick = {
                        Log.e("CarritoScreen", "Eliminar producto")
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Eliminar",
                        tint = Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                }

                Text(
                    text = "Eliminar",
                    color = Color.White,
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }

        // Contenedor del producto
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .offset(x = offsetX.dp)
                .shadow(elevation = 8.dp, shape = RoundedCornerShape(12.dp))
                .background(color = Color.White, shape = RoundedCornerShape(12.dp))
                .padding(horizontal = 8.dp)
                .pointerInput(Unit) {
                    detectHorizontalDragGestures(
                        onDragEnd = {
                            if (offsetX < maxOffset / 2) {
                                offsetX = 0f
                            }
                        },
                        onDragCancel = {
                            offsetX = 0f
                        },
                        onHorizontalDrag = { _, dragAmount ->
                            val newOffset = offsetX + dragAmount
                            offsetX = newOffset.coerceIn(0f, maxOffset)
                        }
                    )
                },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = producto.strDrink, // Usamos el nombre de la bebida
                style = MaterialTheme.typography.labelLarge,
                fontSize = 18.sp
            )
        }
    }
}
