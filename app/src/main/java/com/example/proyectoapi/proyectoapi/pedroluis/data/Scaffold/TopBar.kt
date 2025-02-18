package com.example.proyectoapi.proyectoapi.pedroluis.data.Scaffold

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    navegarAPantalla1: () -> Unit,
    navigateToProfile: () -> Unit,
    navigateToCarrito: () -> Unit,
    nombre: String,
    showFavoritesAndLogout: Boolean = true // Nuevo parámetro para controlar la visibilidad
    ) {
    TopAppBar(
        title = {
            Text(
                text = "Nightcap Lounge",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Serif,
                    color = Color(0xFF333333)
                )
            )
        },
        navigationIcon = {
            IconButton(onClick = { navegarAPantalla1() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Volver",
                    tint = Color(0xFF333333)
                )
            }
        },
        actions = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(end = 16.dp)
            ) {
                Text(
                    text = nombre,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = 16.sp,
                        color = Color(0xFF333333)
                    )
                )
                Spacer(modifier = Modifier.width(8.dp))

                if (showFavoritesAndLogout) {
                    // Icono de Usuario
                    IconButton(onClick = { navigateToProfile() }) {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = "Menú de usuario",
                            modifier = Modifier
                                .background(Color.Gray.copy(alpha = 0.2f), shape = CircleShape)
                                .padding(8.dp),
                            tint = Color(0xFF333333)
                        )
                    }

                    // Icono de Carrito
                    IconButton(onClick = { navigateToCarrito() }) {
                        Icon(
                            imageVector = Icons.Default.Favorite ,
                            contentDescription = "Ir al carrito",
                            modifier = Modifier
                                .background(Color.Gray.copy(alpha = 0.2f), shape = CircleShape)
                                .padding(8.dp),
                            tint = Color(0xFF333333)
                        )
                    }
                }

            }
        }

    )
}