package com.example.proyectoapi.proyectoapi.pedroluis.ui.screen.CrearCoctelScreen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
    // Estados para los campos del formulario
    var nombreCoctel by remember { mutableStateOf(TextFieldValue()) }
    var categoriaCoctel by remember { mutableStateOf(TextFieldValue()) }
    var tipoAlcohol by remember { mutableStateOf(TextFieldValue()) }
    var imagenUrl by remember { mutableStateOf(TextFieldValue()) }
    var vaso by remember { mutableStateOf(TextFieldValue()) }
    var instrucciones by remember { mutableStateOf(TextFieldValue()) }

    // Lista de ingredientes con su estado de selección
    val ingredientes = listOf("Vodka", "Ron", "Tequila", "Ginebra", "Jugo de limón", "Azúcar", "Menta", "Soda")
    val ingredientesSeleccionados = remember { mutableStateMapOf<String, Boolean>().apply {
        ingredientes.forEach { this[it] = false }
    }}

    val firestoreManager = FirestoreManager()
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Crear Cóctel",
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
                            vaso.text,
                            ingredientesSeleccionados.filterValues { it }.keys.toList(),
                            instrucciones.text,
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
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()), // Permite hacer scroll
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Campo: Nombre del cóctel
                TextField(
                    value = nombreCoctel,
                    onValueChange = { nombreCoctel = it },
                    label = { Text("Nombre del Cóctel") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Campo: Categoría
                TextField(
                    value = categoriaCoctel,
                    onValueChange = { categoriaCoctel = it },
                    label = { Text("Categoría") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Campo: Tipo de alcohol
                TextField(
                    value = tipoAlcohol,
                    onValueChange = { tipoAlcohol = it },
                    label = { Text("Tipo de Alcohol") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Campo: URL de la imagen
                TextField(
                    value = imagenUrl,
                    onValueChange = { imagenUrl = it },
                    label = { Text("URL de la Imagen") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Campo: Vaso
                TextField(
                    value = vaso,
                    onValueChange = { vaso = it },
                    label = { Text("Vaso") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Lista de ingredientes con checkboxes
                Text(
                    text = "Ingredientes",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    ),
                    modifier = Modifier.align(Alignment.Start)
                )
                ingredientes.forEach { ingrediente ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Checkbox(
                            checked = ingredientesSeleccionados[ingrediente] == true,
                            onCheckedChange = { isChecked ->
                                ingredientesSeleccionados[ingrediente] = isChecked
                            }
                        )
                        Text(text = ingrediente)
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))

                // Campo: Instrucciones
                TextField(
                    value = instrucciones,
                    onValueChange = { instrucciones = it },
                    label = { Text("Instrucciones del Cóctel") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    maxLines = 5
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Botón para guardar
                Button(
                    onClick = {
                        scope.launch {
                            guardarCoctel(
                                nombreCoctel.text,
                                categoriaCoctel.text,
                                tipoAlcohol.text,
                                imagenUrl.text,
                                vaso.text,
                                ingredientesSeleccionados.filterValues { it }.keys.toList(),
                                instrucciones.text,
                                firestoreManager,
                                navController,
                                scope
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
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
    vaso: String,
    ingredientes: List<String>,
    instrucciones: String,
    firestoreManager: FirestoreManager,
    navController: NavController,
    scope: CoroutineScope
) {
    if (nombreCoctel.isEmpty() || categoriaCoctel.isEmpty() || tipoAlcohol.isEmpty() || imagenUrl.isEmpty() || vaso.isEmpty() || instrucciones.isEmpty()) {
        Toast.makeText(navController.context, "Completa todos los campos", Toast.LENGTH_SHORT).show()
        return
    }

    val newMediaItem = MediaItem(
        idDrink = generateId(),
        strDrink = nombreCoctel,
        strCategory = categoriaCoctel,
        strAlcoholic = tipoAlcohol,
        strDrinkThumb = imagenUrl,
        strGlass = vaso,
        strInstructionsES = instrucciones,
        strIngredient1 = ingredientes.getOrNull(0) ?: "",
        strIngredient2 = ingredientes.getOrNull(1) ?: "",
        strIngredient3 = ingredientes.getOrNull(2) ?: "",
        strIngredient4 = ingredientes.getOrNull(3) ?: "",
        strIngredient5 = ingredientes.getOrNull(4) ?: "",
        strIngredient6 = ingredientes.getOrNull(5) ?: ""

    )

    scope.launch {
        firestoreManager.addCocktail(newMediaItem)
        Toast.makeText(navController.context, "Cóctel guardado con éxito", Toast.LENGTH_SHORT).show()
        navController.popBackStack() // Regresa a la pantalla anterior
    }
}

// Generar un ID único
fun generateId(): String {
    return (10000..99999).random().toString()
}