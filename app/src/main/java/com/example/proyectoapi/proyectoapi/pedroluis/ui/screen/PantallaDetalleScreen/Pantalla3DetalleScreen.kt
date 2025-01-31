import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.proyectoapi.proyectoapi.pedroluis.data.firebase.AuthManager
import com.example.proyectoapi.proyectoapi.pedroluis.data.model.Pantalla2ViewModel
import com.example.proyectoapi.proyectoapi.pedroluis.data.repositories.model.Drink

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Pantalla3DetalleScreen(
    authManager: AuthManager,
    viewModel: Pantalla2ViewModel,
    navigateToBack: () -> Unit,
    navegarAPantalla1: () -> Unit,
) {

    val bebida by viewModel.producto.observeAsState(null) // Usar null como valor inicial
    val progressBar by viewModel.progressBar.observeAsState(false)
    val usuario = authManager.getCurrentUser()

    if (progressBar) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        Scaffold(
            topBar = {
                val nombre = if (usuario?.email == null) {
                    "Invitado"
                } else {
                    usuario.displayName?.split(" ")?.firstOrNull() ?: "Usuario"
                }
                TopAppBar(
                    title = {
                        Text(
                            "Detalles del Cóctel",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Serif
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { navigateToBack() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Volver"
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
                            Icon(
                                imageVector = Icons.Default.AccountCircle,
                                contentDescription = "Menú de usuario",
                                modifier = Modifier
                                    .background(Color.Gray.copy(alpha = 0.2f), shape = CircleShape)
                                    .padding(8.dp),
                                tint = Color(0xFF333333)
                            )
                        }
                    }
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Card con la imagen
                bebida?.let {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 30.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.LightGray)
                    ) {
                        // Imagen del cóctel
                        AsyncImage(
                            model = it.strDrinkThumb,
                            contentDescription = "Imagen de ${it.strDrink}",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(300.dp)
                        )
                    }

                    Text(
                        text = it.strDrink,
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp),
                        textAlign = TextAlign.Center,
                        fontFamily = FontFamily.Serif
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 20.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Categoría: ${it.strCategory}",
                            fontSize = 17.sp,
                            color = Color(0xFFFF5722),
                            modifier = Modifier.padding(horizontal = 16.dp),
                            fontFamily = FontFamily.Serif
                        )
                        Text(
                            text = "Alcohol: ${it.strAlcoholic}",
                            fontSize = 17.sp,
                            color = Color(0xFFFF5722),
                            modifier = Modifier.padding(horizontal = 16.dp),
                            fontFamily = FontFamily.Serif
                        )
                    }

                    Text(
                        text = "Vaso: ${it.strGlass}",
                        fontSize = 17.sp,
                        color = Color(0xFFFF5722),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        textAlign = TextAlign.Center,
                        fontFamily = FontFamily.Serif
                    )

                    // Ingredientes
                    val strIngredientes: List<Any> = listOf(
                        it.strIngredient1,
                        it.strIngredient2,
                        it.strIngredient3,
                        it.strIngredient4,
                        it.strIngredient5,
                        it.strIngredient6,
                        it.strIngredient7,
                        it.strIngredient8,
                        it.strIngredient9,
                        it.strIngredient10,
                        it.strIngredient11,
                        it.strIngredient12,
                        it.strIngredient13,
                        it.strIngredient14,
                        it.strIngredient15
                    ).filterNotNull() // Filtra los ingredientes nulos

                    ListaIngredientes(strIngredientes) // Muestra la lista de ingredientes

                    // Instrucciones
                    Text(
                        text = "Instrucciones del Cóctel",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF333333),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 24.dp, bottom = 12.dp),
                        textAlign = TextAlign.Center,
                        fontFamily = FontFamily.Serif
                    )

                    Text(
                        text = it.strInstructionsES,
                        fontSize = 17.sp,
                        color = Color(0xFFFF5722),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        textAlign = TextAlign.Center
                    )

                    // Botón para pedir el cóctel
                    Spacer(modifier = Modifier.weight(1f))
                    Button(
                        onClick = { /* Acción para pedir el cóctel */ },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF7043))
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ShoppingCart,
                            contentDescription = "Pedir Cóctel",
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        Text(
                            text = "Pedir el Cóctel",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

// Función para mostrar la lista de ingredientes
@Composable
fun ListaIngredientes(ingredientes: List<Any>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 20.dp)
            .padding(horizontal = 16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Ingredientes",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF333333)
            )
        }

        // Lista de ingredientes en filas de 2
        val chunkedIngredientes =
            ingredientes.chunked(2) // Divide la lista en sublistas de 2 elementos

        chunkedIngredientes.forEach { filaIngredientes ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Spacer(modifier = Modifier.height(8.dp))

                filaIngredientes.forEach { ingrediente ->
                    Text(
                        text = "• $ingrediente",
                        fontSize = 18.sp,
                        color = Color(0xFFFF5722),
                        modifier = Modifier.padding(end = 16.dp)
                    )
                }
            }
        }
    }
}
