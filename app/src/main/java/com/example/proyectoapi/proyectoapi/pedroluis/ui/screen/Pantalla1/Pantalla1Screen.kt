package com.example.proyectoapi.proyectoapi.pedroluis.ui.screen.Pantalla1

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

@Composable
fun Pantalla1Screen(navegarAPantalla2: (String) -> Unit) {
    var usuario = remember { androidx.compose.runtime.mutableStateOf("") }
    var contrasena = remember { androidx.compose.runtime.mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Título
        Text(
            text = "Bienvenido",
            fontSize = 32.sp,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 32.dp)
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
                .padding(vertical = 8.dp)
        ) {
            Text(text = "Iniciar sesión")
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
