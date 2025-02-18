package com.example.proyectoapi.proyectoapi.pedroluis.ui.screen.CrearCoctelScreen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.proyectoapi.proyectoapi.pedroluis.data.firebase.AuthManager
import com.example.proyectoapi.proyectoapi.pedroluis.data.firebase.FirestoreManager
import com.example.proyectoapi.proyectoapi.pedroluis.data.model.MediaItem
import com.example.proyectoapi.proyectoapi.pedroluis.ui.screen.PantallaListaScreen.Pantalla2ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrearCoctelScreen(
    authManager: AuthManager,
    navController: NavController,
    viewModelPantalla: Pantalla2ViewModel,
    navegarAPantalla2: () -> Unit,
) {
    var nombreCoctel by remember { mutableStateOf(TextFieldValue()) }
    var categoriaCoctel by remember { mutableStateOf(TextFieldValue()) }
    var tipoAlcohol by remember { mutableStateOf(TextFieldValue()) }
    var imagenUrl by remember { mutableStateOf(TextFieldValue()) }

    val firestoreManager = FirestoreManager()

    val lista by viewModelPantalla.bebidas.observeAsState(emptyList())
    val progressBar by viewModelPantalla.progressBar.observeAsState(false)
    val user = authManager.getCurrentUser()

    val scope = rememberCoroutineScope()  // Aquí creas el contexto de la corrutina

    Scaffold(
        topBar = {
            val nombre =
                if (user?.email == null) "Invitado" else user.displayName?.split(" ")?.firstOrNull()
                    ?: "Usuario"

            TopAppBar(
                title = {
                    Text(
                        text = "Crear Cocktail",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Serif,
                            color = Color(0xFF333333)
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navegarAPantalla2() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = Color(0xFF333333)
                        )
                    }
                },
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    scope.launch {
                        guardarCoctel(
                            nombreCoctel.text,
                            categoriaCoctel.text,
                            tipoAlcohol.text,
                            imagenUrl.text,
                            firestoreManager,
                            navController,
                            scope
                        )
                    }
                }
            ) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = "Agregar Cóctel")
            }
        },
        content = { contentPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TextField(
                    value = nombreCoctel,
                    onValueChange = { nombreCoctel = it },
                    label = { Text("Nombre del Cóctel") }
                )
                Spacer(modifier = Modifier.height(8.dp))

                TextField(
                    value = categoriaCoctel,
                    onValueChange = { categoriaCoctel = it },
                    label = { Text("Categoría") }
                )
                Spacer(modifier = Modifier.height(8.dp))

                TextField(
                    value = tipoAlcohol,
                    onValueChange = { tipoAlcohol = it },
                    label = { Text("Tipo de Alcohol") }
                )
                Spacer(modifier = Modifier.height(8.dp))

                TextField(
                    value = imagenUrl,
                    onValueChange = { imagenUrl = it },
                    label = { Text("URL de la Imagen") }
                )
                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        // Llamada a la función guardarCoctel
                        guardarCoctel(
                            nombreCoctel.text,
                            categoriaCoctel.text,
                            tipoAlcohol.text,
                            imagenUrl.text,
                            firestoreManager,
                            navController,
                            scope // Pasamos el scope de la corrutina
                        )
                    }
                ) {
                    Text("Guardar Cóctel")
                }
            }
        }
    )
}

// Función para guardar el cóctel
fun guardarCoctel(
    nombreCoctel: String,
    categoriaCoctel: String,
    tipoAlcohol: String,
    imagenUrl: String,
    firestoreManager: FirestoreManager,
    navController: NavController,
    scope: CoroutineScope // Ahora pasamos el scope como argumento
) {
    val newMediaItem = MediaItem(
        idDrink = generateId(),
        strDrink = nombreCoctel,
        strCategory = categoriaCoctel,
        strAlcoholic = tipoAlcohol,
        strDrinkThumb = imagenUrl
    )

    // Usamos el scope para lanzar una corrutina
    scope.launch {
        firestoreManager.addCocktail(newMediaItem)
        Toast.makeText(navController.context, "Cóctel guardado con éxito", Toast.LENGTH_SHORT).show()
        navController.popBackStack() // Regresa a la pantalla anterior
    }
}

fun generateId(): String {
    return java.util.UUID.randomUUID().toString()
}