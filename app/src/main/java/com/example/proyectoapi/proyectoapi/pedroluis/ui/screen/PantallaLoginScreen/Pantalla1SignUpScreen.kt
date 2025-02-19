package com.example.proyectoapi.proyectoapi.pedroluis.ui.screen.PantallaLoginScreen

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.proyectoapi.proyectoapi.pedroluis.R
import com.example.proyectoapi.proyectoapi.pedroluis.data.firebase.AuthManager
import kotlinx.coroutines.launch

@Composable
fun Pantalla1SignUpScreen(auth: AuthManager, navigateToLogin: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var usuario by remember { mutableStateOf("") }
    var passwd by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val authState by auth.authState.collectAsState()
    val fotoUrl = "https://media.istockphoto.com/id/1003178096/es/vector/c%C3%B3cteles.jpg?s=612x612&w=0&k=20&c=za-nipZJgIQJM3AqNgDdfx5_wz5oCTu1Lo9EzOo5BEo="

    LaunchedEffect(authState) {
        when (authState) {
            is AuthManager.AuthRes.Success -> {
                Toast.makeText(context, "Usuario registrado", Toast.LENGTH_SHORT).show()
                auth.resetAuthState()
                navigateToLogin()
            }
            is AuthManager.AuthRes.Error -> {
                Toast.makeText(context, (authState as AuthManager.AuthRes.Error).errorMessage, Toast.LENGTH_SHORT).show()
            }
            is AuthManager.AuthRes.Idle -> {}
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = rememberAsyncImagePainter(fotoUrl),
                    contentDescription = "Logo del bar",
                    modifier = Modifier.size(150.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
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
                Spacer(modifier = Modifier.height(24.dp))

                TextField(
                    value = usuario,
                    onValueChange = { usuario = it },
                    label = { Text("Usuario") },
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = "Ícono de usuario") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(10.dp))

                TextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Correo") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    leadingIcon = { Icon(Icons.Default.Mail, contentDescription = "Ícono de email") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(10.dp))

                TextField(
                    value = passwd,
                    onValueChange = { passwd = it },
                    label = { Text("Contraseña") },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    leadingIcon = { Icon(Icons.Default.Lock, contentDescription = "Ícono de password") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF7043)),
                    onClick = {
                        scope.launch {
                            signUp(auth, usuario, email, passwd, context)
                        }
                    },
                ) {
                    val isLoading by auth.progressBar.observeAsState(false)
                    if (isLoading) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(30.dp),
                            strokeWidth = 3.dp
                        )
                    } else {
                        Text("Registrarse")
                    }
                }
            }
        }
    }
}

suspend fun signUp(auth: AuthManager, usuario: String, email: String, passwd: String, context: Context) {
    if (email.isNotEmpty() && usuario.isNotEmpty() && passwd.isNotEmpty()) {
        auth.createUserWithEmailAndPassword(email, passwd, usuario)
    } else {
        Toast.makeText(context, "Rellene todos los campos", Toast.LENGTH_SHORT).show()
    }
}
