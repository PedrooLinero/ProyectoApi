package com.example.proyectoapi.proyectoapi.pedroluis.ui.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proyectoapi.proyectoapi.pedroluis.data.firebase.FirestoreManager
import com.example.proyectoapi.proyectoapi.pedroluis.data.model.MediaItem
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditarCoctelScreen(
    mediaItem: MediaItem,
    onBack: () -> Unit,
    firestoreManager: FirestoreManager
) {
    // Usamos String en lugar de TextFieldValue para evitar problemas de visualización
    var nombre by remember { mutableStateOf(mediaItem.strDrink) }
    var categoria by remember { mutableStateOf(mediaItem.strCategory) }
    var instrucciones by remember { mutableStateOf(mediaItem.strInstructionsES) }

    // Combinar ingredientes existentes (filtrando los nulos) en un único string separado por comas
    val initialIngredientes = listOfNotNull(
        mediaItem.strIngredient1,
        mediaItem.strIngredient2,
        mediaItem.strIngredient3,
        mediaItem.strIngredient4,
        mediaItem.strIngredient5,
        mediaItem.strIngredient6
    ).joinToString(", ")

    var ingredientes by remember { mutableStateOf(initialIngredientes) }

    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Editar cóctel",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Serif
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = categoria,
                    onValueChange = { categoria = it },
                    label = { Text("Categoría") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = instrucciones,
                    onValueChange = { instrucciones = it },
                    label = { Text("Instrucciones") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = ingredientes,
                    onValueChange = { ingredientes = it },
                    label = { Text("Ingredientes (separados por comas)") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))

                Button(

                    onClick = {
                        // Separamos los ingredientes ingresados en una lista
                        val listIngredientes = ingredientes
                            .split(",")
                            .map { it.trim() }
                            .filter { it.isNotEmpty() }

                        // Actualizamos el MediaItem asignando cada ingrediente a su campo correspondiente
                        val nuevoCoctel = mediaItem.copy(
                            strDrink = nombre,
                            strCategory = categoria,
                            strInstructionsES = instrucciones,
                            strIngredient1 = listIngredientes.getOrNull(0),
                            strIngredient2 = listIngredientes.getOrNull(1),
                            strIngredient3 = listIngredientes.getOrNull(2),
                            strIngredient4 = listIngredientes.getOrNull(3),
                            strIngredient5 = listIngredientes.getOrNull(4),
                            strIngredient6 = listIngredientes.getOrNull(5)
                        )
                        scope.launch {
                            try {
                                firestoreManager.updateCocktail(nuevoCoctel)
                                println("Cóctel actualizado exitosamente.")
                                onBack()
                            } catch (e: Exception) {
                                println("Error al actualizar cóctel: ${e.message}")
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFF7043)
                    )
                ) {
                    Text("Actualizar cóctel")
                }
            }
        }
    }
}
