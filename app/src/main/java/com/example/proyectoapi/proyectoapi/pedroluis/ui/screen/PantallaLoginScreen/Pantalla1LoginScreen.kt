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
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter

@Composable
fun Pantalla1Screen(navegarAPantalla2: (String) -> Unit) {
    var usuario = remember { androidx.compose.runtime.mutableStateOf("") }
    var contrasena = remember { androidx.compose.runtime.mutableStateOf("") }

    // URL de la imagen
    val fotoUrl = "https://media.istockphoto.com/id/1003178096/es/vector/c%C3%B3cteles.jpg?s=612x612&w=0&k=20&c=za-nipZJgIQJM3AqNgDdfx5_wz5oCTu1Lo9EzOo5BEo="  // URL de la foto

    // Pantalla de inicio de sesión
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Cargar la imagen desde la URL y hacerla más grande
            androidx.compose.foundation.Image(
                painter = rememberAsyncImagePainter(fotoUrl), // Cargar la imagen desde la URL
                contentDescription = "Logo del bar",
                modifier = Modifier
                    .size(200.dp)
                    .padding(bottom = 32.dp)
            )

            Text(
                text = "NIGHTCAP LOUNGE",
                fontFamily = androidx.compose.ui.text.font.FontFamily.Serif,
                fontSize = 32.sp,
                color = Color(0xFFFF7043),
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
                onValueChange = { usuario.value = it }, // Actualizar el valor del campo de texto
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                placeholder = { Text("Introduce tu usuario") }
            )

            // Campo de texto para la contraseña
            Text("Contraseña:")
            TextField(
                value = contrasena.value,
                onValueChange = { contrasena.value = it }, // Actualizar el valor del campo de texto
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                placeholder = { Text("Introduce tu contraseña") },
                visualTransformation = PasswordVisualTransformation() // Mostrar los caracteres como puntos
            )

            // Botón de inicio de sesión
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    // Si el usuario y la contraseña no están vacíos, navegar a la pantalla 2
                    if (usuario.value.isNotEmpty() && contrasena.value.isNotEmpty()) {
                        navegarAPantalla2(usuario.value)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFF7043)
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
