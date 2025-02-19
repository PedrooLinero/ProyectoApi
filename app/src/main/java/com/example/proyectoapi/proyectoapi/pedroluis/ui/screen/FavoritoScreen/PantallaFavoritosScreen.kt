package com.example.proyectoapi.proyectoapi.pedroluis.ui.screen.FavoritoScreen

import android.util.Log
import androidx.compose.foundation.background
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
import androidx.compose.ui.text.font.FontFamily
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
                title = {
                    Text(
                        "Favoritos",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Serif
                    )
                },
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
                items(coctelesFavorites, key = { it.idDrink ?: "" }) { bebida ->
                    CoctelCard(
                        coctel = bebida,
                        firestoreManager = firestoreManager,
                        onRemoveFavorite = { idDrink ->
                            coctelesFavorites =
                                coctelesFavorites.filterNot { it.idDrink == idDrink }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun CoctelCard(
    coctel: MediaItem,
    firestoreManager: FirestoreManager,
    onRemoveFavorite: (String) -> Unit
) {
    var isFavorite by remember { mutableStateOf(true) }

    LaunchedEffect(coctel.idDrink) {
        val favorites = firestoreManager.getFavorites()
        isFavorite = favorites.any { it.idDrink == coctel.idDrink }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.elevatedCardElevation(6.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Box {
            AsyncImage(
                model = coctel.strDrinkThumb,
                contentDescription = "Imagen de ${coctel.strDrink}",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(250.dp)
                    .fillMaxWidth()
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .background(Color.Black.copy(alpha = 0.6f))
                    .padding(8.dp)
            ) {
                Text(
                    text = coctel.strDrink,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = Color.White,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            IconButton(
                onClick = {
                    isFavorite = !isFavorite
                    coctel.idDrink?.let { idDrink ->
                        CoroutineScope(Dispatchers.IO).launch {
                            if (!isFavorite) {
                                firestoreManager.removeFavorite(idDrink)
                                onRemoveFavorite(idDrink)
                            } else {
                                firestoreManager.addFavorite(coctel)
                            }
                        }
                    }
                },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
                    .size(36.dp)
                    .background(Color.White.copy(alpha = 0.8f), shape = RoundedCornerShape(50))
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

