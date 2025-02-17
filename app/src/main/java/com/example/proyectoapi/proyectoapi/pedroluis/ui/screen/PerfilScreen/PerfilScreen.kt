package com.example.proyectoapi.proyectoapi.pedroluis.ui.screen.PerfilScreen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proyectoapi.proyectoapi.pedroluis.R
import com.example.proyectoapi.proyectoapi.pedroluis.data.firebase.AuthManager
import kotlinx.coroutines.launch
import java.util.Locale

@Composable
fun PerfilScreen(
    auth: AuthManager,
    navigateToBack: () -> Unit
) {
    val authState by auth.authState.collectAsState()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    LaunchedEffect(authState) {
        when (authState) {
            is AuthManager.AuthRes.Success -> {
                Toast.makeText(context, "Usuario actualizado correctamente", Toast.LENGTH_SHORT)
                    .show()
                auth.resetAuthState()
            }

            is AuthManager.AuthRes.Error -> {
                Toast.makeText(
                    context,
                    (authState as AuthManager.AuthRes.Error).errorMessage,
                    Toast.LENGTH_SHORT
                ).show()
            }

            is AuthManager.AuthRes.Idle -> {}
        }
    }

    val user = auth.getCurrentUser()
    val nombre: String = if (user?.email == null) {
        "Invitado"
    } else {
        user.displayName ?: "Usuario"
    }
    val nombrePerfil = nombre.toCharArray()[0].toString().uppercase(Locale.getDefault())

    var nombreNuevo by remember { mutableStateOf(nombre) }

    Scaffold(
        containerColor = Color(0xFFF5F5F5) // Fondo claro
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header con botón de retroceso
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Atras",
                    tint = Color(0xFF1A1A1A),
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color.Transparent, CircleShape)
                        .clickable { navigateToBack() }
                        .padding(8.dp)
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    "Perfil",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1A1A1A)
                    ),
                    fontSize = 28.sp
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Avatar circular
            Box(
                modifier = Modifier
                    .size(150.dp)
                    .background(Color(0xFFFF7043), CircleShape) // Color morado
                    .border(4.dp, Color.White, CircleShape)
                    .shadow(8.dp, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    nombrePerfil,
                    style = MaterialTheme.typography.displayLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    ),
                    fontSize = 64.sp
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Campo de texto para cambiar el nombre
            Text(
                "Cambiar nombre",
                style = MaterialTheme.typography.titleMedium.copy(
                    color = Color(0xFF1A1A1A)
                ),
                fontSize = 18.sp,
                modifier = Modifier.align(Alignment.Start)
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = nombreNuevo,
                onValueChange = { nombreNuevo = it },
                placeholder = { Text("Introduce tu nuevo nombre") },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, MaterialTheme.shapes.medium)
                    .border(1.dp, Color(0xFFE0E0E0), MaterialTheme.shapes.medium),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                shape = MaterialTheme.shapes.medium
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Botón para guardar cambios
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                onClick = {
                    if (nombreNuevo != nombre) {
                        scope.launch {
                            auth.updateDisplayName(nombreNuevo)
                        }
                    } else {
                        Toast.makeText(
                            context,
                            "El nombre de usuario es el mismo",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFF7043),
                ),
                shape = MaterialTheme.shapes.medium
            ) {
                if (auth.progressBar.observeAsState().value == true) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 3.dp
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Save,
                        contentDescription = "Guardar",
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Guardar cambios", fontSize = 16.sp)
                }
            }
        }
    }
}