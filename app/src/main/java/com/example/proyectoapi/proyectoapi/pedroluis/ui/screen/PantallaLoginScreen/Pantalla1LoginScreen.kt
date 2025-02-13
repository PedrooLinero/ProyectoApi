package com.example.proyectoapi.proyectoapi.pedroluis.ui.screen.PantallaLoginScreen

import android.content.Context
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.proyectoapi.proyectoapi.pedroluis.data.firebase.AuthManager
import com.example.proyectoapi.proyectoapi.pedroluis.ui.theme.Purple40
import com.google.android.gms.auth.api.signin.GoogleSignIn
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.example.proyectoapi.proyectoapi.pedroluis.R


@Composable
fun Pantalla1Screen(
    auth: AuthManager,
    navegarAPantalla2: () -> Unit,
    navigateToSignUp: () -> Unit,
    navigateToForgotPassword: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val authState by auth.authState.collectAsState()
    val googleSignLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        CoroutineScope(Dispatchers.Main).launch {
            auth.handleGoogleSignInResult(task)
        }
    }

    LaunchedEffect(authState) {
        when (authState) {
            is AuthManager.AuthRes.Success -> {
                Toast.makeText(context, "Inicio de sesion exitoso", Toast.LENGTH_SHORT).show()
                auth.resetAuthState()
                navegarAPantalla2()
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

    // URL de la imagen
    val fotoUrl =
        "https://media.istockphoto.com/id/1003178096/es/vector/c%C3%B3cteles.jpg?s=612x612&w=0&k=20&c=za-nipZJgIQJM3AqNgDdfx5_wz5oCTu1Lo9EzOo5BEo="  // URL de la foto

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        // Pantalla de inicio de sesión
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .background(Color.White)
                .padding(innerPadding)
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
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    placeholder = { Text("Introduce tu usuario") },
                    singleLine = true

                )

                // Campo de texto para la contraseña
                Text("Contraseña:")
                TextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Contraseña") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    placeholder = { Text("Introduce tu contraseña") },
                    visualTransformation = PasswordVisualTransformation(), // Mostrar los caracteres como puntos
                    singleLine = true
                )

                // Botón de inicio de sesión
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        // Si el usuario y la contraseña no están vacíos, navegar a la pantalla 2
                        scope.launch {
                            signIn(auth, email, password, context)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFF7043)
                    )
                ) {
                    if (auth.progressBar.observeAsState().value == true) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(30.dp),
                            strokeWidth = 3.dp
                        )
                    } else {
                        Text("Iniciar sesión")
                    }
                }

                // Mensaje debajo del botón (opcional)
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "¿Olvidaste tu contraseña?",
                    modifier = Modifier.clickable { navigateToForgotPassword() },
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontFamily = FontFamily.Default,
                        textDecoration = TextDecoration.Underline,
                        color = Purple40
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "¿No tienes una cuenta?",
                    style = TextStyle(color = Color.Gray)
                )

                Spacer(modifier = Modifier.height(16.dp))

                BotonesGoogle(
                    onClick = {
                        scope.launch {
                            signAnonimously(auth)
                        }
                    },
                    text = "Continuar como invitado",
                    icon = R.drawable.ic_incognito,
                    color = Color(0xFF363636),
                    cargando = auth.progressBarAnonimous.observeAsState().value ?: false
                )

                Spacer(modifier = Modifier.height(16.dp))

                BotonesGoogle(
                    onClick = {
                        googleSignLauncher.launch(auth.getGoogleSignInIntent())
                    },
                    text = "Continuar con Google",
                    icon = R.drawable.ic_google,
                    color = Color(0xFFF1F1F1),
                    cargando = auth.progressBarGoogle.observeAsState().value ?: false
                )
            }
        }
    }
}

// Función BotonesGoogle
@Composable
fun BotonesGoogle(
    onClick: () -> Unit,
    text: String,
    icon: Int,
    color: Color,
    cargando: Boolean
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = androidx.compose.material3.ButtonDefaults.buttonColors(
            containerColor = color
        )
    ) {
        if (cargando) {
            if (icon == R.drawable.ic_incognito) {
                CircularProgressIndicator(
                    color = Color.White,
                    modifier = Modifier.size(30.dp),
                    strokeWidth = 3.dp
                )
            } else {
                CircularProgressIndicator(
                    color = Color.Black,
                    modifier = Modifier.size(30.dp),
                    strokeWidth = 3.dp
                )
            }
        } else {
            Row(
                modifier = Modifier
                    .padding(start = 6.dp, end = 8.dp, top = 6.dp, bottom = 6.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    painter = painterResource(id = icon),
                    modifier = Modifier.size(24.dp),
                    contentDescription = text,
                    tint = Color.Unspecified
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = text,
                    color = if (icon == R.drawable.ic_incognito) Color.White else Color.Black
                )
            }
        }
    }
}

suspend fun signIn(auth: AuthManager, email: String, passwd: String, context: Context) {
    if (email.isNotEmpty() && passwd.isNotEmpty()) {
        auth.signInWithEmailAndPassword(email, passwd)
    } else {
        Toast.makeText(context, "Complete los campos", Toast.LENGTH_SHORT).show()
    }
}

suspend fun signAnonimously(auth: AuthManager) {
    // Cerramos la sesion antes de iniciar como anonimos
    // Para evitar posibles intervenciones de la cache del sistema
    auth.signOut()
    auth.signAnonimously()
}



