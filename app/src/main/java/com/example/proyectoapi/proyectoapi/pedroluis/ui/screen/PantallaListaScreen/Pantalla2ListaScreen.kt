import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.proyectoapi.proyectoapi.pedroluis.ui.screen.PantallaListaScreen.Pantalla2ViewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BackHand
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.style.TextAlign
import com.example.proyectoapi.proyectoapi.pedroluis.data.firebase.AuthManager
import com.example.proyectoapi.proyectoapi.pedroluis.data.model.MediaItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Pantalla2Screen(
    authManager: AuthManager,
    viewModelPantalla: Pantalla2ViewModel,
    navegarAPantalla1: () -> Unit,
    navegarAPantalla3: (String) -> Unit,
    navigateToCarrito: () -> Unit,
    navigateToCrearCoctel: () -> Unit
) {
    val lista by viewModelPantalla.bebidas.observeAsState(emptyList())
    val progressBar by viewModelPantalla.progressBar.observeAsState(false)
    val user = authManager.getCurrentUser()

    var coctelesfavorites by remember { mutableStateOf<List<MediaItem>>(emptyList()) }

    // URL del logo de la aplicación
    val fotoUrl = "https://media.istockphoto.com/id/1003178096/es/vector/c%C3%B3cteles.jpg?s=612x612&w=0&k=20&c=za-nipZJgIQJM3AqNgDdfx5_wz5oCTu1Lo9EzOo5BEo="

    // Cargar bebidas al entrar a la pantalla
    LaunchedEffect(Unit) {
        viewModelPantalla.cargarBebidas()
    }

    val logout = {
        authManager.signOut()
        navegarAPantalla1()
    }

    Scaffold(
        topBar = {
            val nombre = if (user?.email == null) "Invitado"
            else user.displayName?.split(" ")?.firstOrNull() ?: "Usuario"

            TopAppBar(
                title = {
                    // Muestra el logo de la aplicación en pequeño
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter(fotoUrl),
                            contentDescription = "Logo de la aplicación",
                            modifier = Modifier.size(30.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp)) // Espacio entre el logo y el texto

                        Text(
                            text = "Nightcap Lounge",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontSize = 22.sp, // Tamaño más pequeño
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Serif,
                                color = Color(0xFF333333)
                            )
                        )
                    }

                },
                actions = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(end = 16.dp)
                    ) {
                        // Muestra el nombre del usuario
                        Text(
                            text = nombre,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontSize = 16.sp,
                                color = Color(0xFF333333)
                            )
                        )
                        Spacer(modifier = Modifier.width(4.dp))

                        IconButton(onClick = logout) {
                            Icon(
                                imageVector = Icons.Default.BackHand,
                                contentDescription = "Logout",
                                modifier = Modifier
                                    .background(Color.Gray.copy(alpha = 0.2f), shape = CircleShape)
                                    .padding(8.dp),
                                tint = Color(0xFF333333)
                            )
                        }

                        IconButton(onClick = { navigateToCarrito() }) {
                            Icon(
                                imageVector = Icons.Default.Favorite,
                                contentDescription = "Ir al carrito",
                                modifier = Modifier
                                    .background(Color.Gray.copy(alpha = 0.2f), shape = CircleShape)
                                    .padding(8.dp),
                                tint = Color(0xFF333333)
                            )
                        }
                    }
                }
            )
        },
        floatingActionButton = { // FAB para crear un cóctel
            FloatingActionButton(
                onClick = { navigateToCrearCoctel() },
                modifier = Modifier.padding(16.dp),
                containerColor = Color(0xFFFF7043)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Crear Cóctel",
                    tint = Color.White
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Text(
                text = "Déjate seducir por el sabor de la creatividad en cada trago. ¡Prueba nuestros cócteles y descubre tu favorito!",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    fontFamily = FontFamily.Serif,
                    color = Color(0xFFFF7043),
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            if (progressBar) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val coctelesToDisplay = if (coctelesfavorites.isEmpty()) lista else coctelesfavorites
                    items(coctelesToDisplay) { item ->
                        CocktailCard(
                            item = item,
                            idUsuario = user?.uid ?: "",
                            navegarAPantalla3 = navegarAPantalla3
                        )
                    }
                }
            }
        }
    }
}


// Card para mostrar la información de una bebida
@Composable
fun CocktailCard(
    item: MediaItem,
    idUsuario: String,
    navegarAPantalla3: (String) -> Unit
) {
    // Fondo blanco para los cards
    val cardBackgroundColor = Color.White

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable { navegarAPantalla3(item.idDrink) }
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = cardBackgroundColor)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
            ) {
                Image(
                    painter = rememberAsyncImagePainter(item.strDrinkThumb),
                    contentDescription = "Imagen de ${item.strDrink}",
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 200.dp),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = item.strDrink,
                modifier = Modifier.padding(horizontal = 12.dp),
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    fontFamily = FontFamily.Serif
                ),
                color = Color(0xFF333333)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Categoría: ${item.strCategory}",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        fontFamily = FontFamily.Serif
                    ),
                    color = Color(0xFFFF7043)
                )

                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    text = "Alcohol: ${if (item.strAlcoholic == "Alcoholic") "Sí" else "No"}",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        fontFamily = FontFamily.Serif
                    ),
                    color = Color(0xFFFF7043)
                )
            }
        }
    }
}
