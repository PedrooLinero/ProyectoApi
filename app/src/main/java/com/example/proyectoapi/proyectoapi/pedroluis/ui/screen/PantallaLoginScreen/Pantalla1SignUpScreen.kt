package com.example.proyectoapi.proyectoapi.pedroluis.ui.screen.PantallaLoginScreen

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

    LaunchedEffect(authState) {
        when(authState) {
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
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)) {
            Column(
                modifier = Modifier.fillMaxWidth()
                    .padding(top = 40.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Nightcap Lounge",
                    style = MaterialTheme.typography.titleLarge,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    fontFamily = FontFamily.Monospace
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 40.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(75.dp))

                TextField(
                    value = usuario,
                    onValueChange = { usuario = it },
                    label = { Text("Usuario") },
                    modifier = Modifier.width(335.dp),
                    leadingIcon = {
                        Icon(Icons.Default.Person, contentDescription = "Ícono de usuario")
                    },
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(10.dp))

                TextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Correo") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    modifier = Modifier.width(335.dp),
                    leadingIcon = {
                        Icon(Icons.Default.Mail, contentDescription = "Ícono de email")
                    },
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(10.dp))

                TextField(
                    value = passwd,
                    onValueChange = { passwd = it },
                    label = { Text("Contraseña") },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    modifier = Modifier.width(335.dp),
                    leadingIcon = {
                        Icon(Icons.Default.Lock, contentDescription = "Ícono de password")
                    },
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    modifier = Modifier.width(335.dp)
                        .height(50.dp),
                    onClick = {
                        scope.launch {
                            signUp(auth, usuario, email, passwd, context)
                        }
                    },
                ) {
                    if (auth.progressBar.observeAsState().value == true) {
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