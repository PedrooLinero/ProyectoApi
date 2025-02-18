package com.example.proyectoapi.proyectoapi.pedroluis.ui.screen.FavoritoScreen

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.proyectoapi.proyectoapi.pedroluis.data.firebase.AuthManager
import com.example.proyectoapi.proyectoapi.pedroluis.data.firebase.FirestoreManager
import com.example.proyectoapi.proyectoapi.pedroluis.data.model.MediaItem
import com.example.proyectoapi.proyectoapi.pedroluis.ui.screen.PantallaListaScreen.Pantalla2ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaFavoritosScreen(
    firestoreManager: FirestoreManager,
    navegarAPantalla2: () -> Unit,
) {
    var coctelesFavorites by remember { mutableStateOf<List<MediaItem>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        try {
            coctelesFavorites = firestoreManager.getFavorites()
        } catch (e: Exception) {
            Log.e("Firestore", "Error al obtener favoritos: ${e.message}")
        } finally {
            isLoading = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Favoritos") },
                navigationIcon = {
                    IconButton(onClick = navegarAPantalla2) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { padding ->
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                items(coctelesFavorites) { bebida ->
                    CoctelCard(coctel = bebida, firestoreManager)
                }
            }
        }
    }
}



@Composable
fun CoctelCard(
    coctel: MediaItem,
    firestoreManager: FirestoreManager
) {
    var isFavorite by remember { mutableStateOf(false) }

    LaunchedEffect(coctel) {
        val favorites = firestoreManager.getFavorites()
        isFavorite = favorites.any { it.idDrink == coctel.idDrink }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.elevatedCardElevation()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            AsyncImage(
                model = coctel.strDrinkThumb,
                contentDescription = "Imagen de ${coctel.strDrink}",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(200.dp)
                    .fillMaxWidth()
            )
            Text(
                text = coctel.strDrink,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            // Botón para marcar/desmarcar como favorito
            IconButton(
                onClick = {
                    isFavorite = !isFavorite
                    CoroutineScope(Dispatchers.IO).launch {
                        if (isFavorite) {
                            firestoreManager.addFavorite(coctel)
                        } else {
                            coctel.idDrink?.let { firestoreManager.removeFavorite(it) }
                        }
                    }
                }
            ) {
                Icon(
                    imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "Favorito",
                    tint = if (isFavorite) Color.Red else Color.Gray
                )
            }
        }
    }
}