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
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import com.example.proyectoapi.proyectoapi.pedroluis.data.firebase.AuthManager
import com.example.proyectoapi.proyectoapi.pedroluis.data.firebase.FirestoreViewModel
import com.example.proyectoapi.proyectoapi.pedroluis.data.model.MediaItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun Pantalla2Screen(
    authManager: AuthManager,
    viewModelPantalla: Pantalla2ViewModel,
    viewModel: FirestoreViewModel,
    navegarAPantalla1: () -> Unit,
    navegarAPantalla3: (String) -> Unit,
    navigateToCarrito: () -> Unit,
    navigateToProfile: () -> Unit
) {
    val lista by viewModelPantalla.bebidas.observeAsState(emptyList())
    val syncState by viewModel.syncState.observeAsState()
    val progressBar by viewModelPantalla.progressBar.observeAsState(false)
    val context = LocalContext.current
    val user = authManager.getCurrentUser()

    LaunchedEffect(Unit) {
        viewModelPantalla.cargarBebidas()
    }

    Scaffold(
        topBar = {
            val nombre =
                if (user?.email == null) "Invitado" else user.displayName?.split(" ")?.firstOrNull()
                    ?: "Usuario"

            TopAppBar(
                title = {
                    Text(
                        text = "Nightcap Lounge",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Serif,
                            color = Color(0xFF333333)
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navegarAPantalla1() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = Color(0xFF333333)
                        )
                    }
                },
                actions = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(end = 16.dp)
                    ) {
                        Text(
                            text = nombre,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontSize = 16.sp,
                                color = Color(0xFF333333)
                            )
                        )
                        Spacer(modifier = Modifier.width(8.dp))

                        // Icono de Usuario
                        IconButton(onClick = { navigateToProfile() }) {
                            Icon(
                                imageVector = Icons.Default.AccountCircle,
                                contentDescription = "Menú de usuario",
                                modifier = Modifier
                                    .background(Color.Gray.copy(alpha = 0.2f), shape = CircleShape)
                                    .padding(8.dp),
                                tint = Color(0xFF333333)
                            )
                        }

                        // Icono de Carrito
                        IconButton(onClick = { navigateToCarrito() }) {
                            Icon(
                                imageVector = Icons.Default.ShoppingCart,
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
                    fontSize = 21.sp,
                    fontFamily = FontFamily.Serif,
                    color = Color(0xFFFF7043),
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier
                    .padding(horizontal = 16.dp)
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
                    items(lista) { mediaItem ->
                        if (user != null) {
                            CocktailCard(mediaItem,viewModel, user.uid, navegarAPantalla3)
                        }
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
    viewModelFirestore: FirestoreViewModel,
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
