package com.example.proyectoapi.proyectoapi.pedroluis.ui.screen.PantallaLoginScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter

@Composable
fun Pantalla1Screen(navegarAPantalla2: (String) -> Unit) {
    var usuario = remember { androidx.compose.runtime.mutableStateOf("") }
    var contrasena = remember { androidx.compose.runtime.mutableStateOf("") }

    // URL de la imagen
    val fotoUrl = "https://media.istockphoto.com/id/1003178096/es/vector/c%C3%B3cteles.jpg?s=612x612&w=0&k=20&c=za-nipZJgIQJM3AqNgDdfx5_wz5oCTu1Lo9EzOo5BEo="  // URL de la foto

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp) // Padding para que no quede pegado al borde
            .background(Color.White) // Fondo blanco
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.Center) // Centra todo dentro de la pantalla
                .padding(16.dp), // Padding general
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Cargar la imagen desde la URL y hacerla más grande
            androidx.compose.foundation.Image(
                painter = rememberImagePainter(fotoUrl),
                contentDescription = "Logo del bar",
                modifier = Modifier
                    .size(200.dp)  // Tamaño de la imagen (ajustado a 200.dp)
                    .padding(bottom = 32.dp)  // Espaciado hacia abajo
            )

            // Título con estilo
            Text(
                text = "NIGHTCAP LOUNGE",
                fontFamily = androidx.compose.ui.text.font.FontFamily.Serif,
                fontSize = 32.sp,
                color = Color(0xFFFF7043),  // Naranja
                modifier = Modifier
                    .padding(bottom = 32.dp),
                style = androidx.compose.ui.text.TextStyle(
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                )
            )

            // Campo de texto para el usuario
            Text("Usuario:")
            TextField(
                value = usuario.value,
                onValueChange = { usuario.value = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                placeholder = { Text("Introduce tu usuario") }
            )

            // Campo de texto para la contraseña
            Text("Contraseña:")
            TextField(
                value = contrasena.value,
                onValueChange = { contrasena.value = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                placeholder = { Text("Introduce tu contraseña") },
                visualTransformation = PasswordVisualTransformation()
            )

            // Botón de inicio de sesión
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    if (usuario.value.isNotEmpty() && contrasena.value.isNotEmpty()) {
                        navegarAPantalla2(usuario.value)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFF7043)  // Botón naranja
                )
            ) {
                Text(text = "Iniciar sesión", color = Color.White)
            }

            // Mensaje debajo del botón (opcional)
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "¿No tienes cuenta? Regístrate",
                color = Color.Gray,
                fontSize = 14.sp
            )
        }
    }
}
