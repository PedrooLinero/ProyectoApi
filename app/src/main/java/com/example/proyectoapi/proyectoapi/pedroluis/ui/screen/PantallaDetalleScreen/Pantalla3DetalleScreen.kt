import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.example.proyectoapi.proyectoapi.pedroluis.data.model.Drink
import com.example.proyectoapi.proyectoapi.pedroluis.data.repositories.repositoryList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Pantalla3DetalleScreen(
    idDrink: String,
    usuario: String,
    navigateToBack: (String) -> Unit
) {
    val bebidaState = remember { mutableStateOf<Drink?>(null) }

    LaunchedEffect(idDrink) {
        val bebida = repositoryList.getBebidaPorId(idDrink)
        bebidaState.value = bebida
    }

    bebidaState.value?.let { bebida ->
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(
                        "Detalles del Cóctel",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Serif,

                    ) },
                    navigationIcon = {
                        IconButton(onClick = { navigateToBack(usuario) }) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
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
                                text = usuario,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontSize = 16.sp, // Tamaño reducido
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
                    },
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
                // Tarjeta con la imagen
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 30.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.LightGray)
                ) {
                    AsyncImage(
                        model = bebida.strDrinkThumb,
                        contentDescription = "Imagen de ${bebida.strDrink}",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                    )
                }

                // Nombre del cóctel en el centro
                Text(
                    text = bebida.strDrink,
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
                        text = "Categoría: ${bebida.strCategory}",
                        fontSize = 17.sp,
                        color = Color(0xFFFF5722),
                        modifier = Modifier.padding(horizontal = 16.dp),
                        fontFamily = FontFamily.Serif
                    )
                    Text(
                        text = "Alcohol: ${bebida.strAlcoholic}",
                        fontSize = 17.sp,
                        color = Color(0xFFFF5722),
                        modifier = Modifier.padding(horizontal = 16.dp),
                        fontFamily = FontFamily.Serif
                    )
                }

                // Detalle del Vaso centrado en la fila debajo
                Text(
                    text = "Vaso: ${bebida.strGlass}",
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
                    bebida.strIngredient1,
                    bebida.strIngredient2,
                    bebida.strIngredient3,
                    bebida.strIngredient4,
                    bebida.strIngredient5,
                    bebida.strIngredient6,
                    bebida.strIngredient7,
                    bebida.strIngredient8,
                    bebida.strIngredient9,
                    bebida.strIngredient10,
                    bebida.strIngredient11,
                    bebida.strIngredient12,
                    bebida.strIngredient13,
                    bebida.strIngredient14,
                    bebida.strIngredient15
                ).filterNotNull()

                ListaIngredientes(strIngredientes)

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

                // Instrucciones del cóctel centradas
                Text(
                    text = bebida.strInstructionsES,
                    fontSize = 17.sp,
                    color = Color(0xFFFF5722),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    textAlign = TextAlign.Center
                )

                // Botón para pedir el cóctel
                Spacer(modifier = Modifier.weight(1f)) // Para empujar el botón al final de la pantalla
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


@Composable
fun ListaIngredientes(ingredientes: List<Any>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 20.dp)
            .padding(horizontal = 16.dp) // Esto asegura que tenga espacio a los lados
    ) {
        // Título "Ingredientes" centrado
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            contentAlignment = Alignment.Center // Centrado dentro del Box
        ) {
            Text(
                text = "Ingredientes",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF333333)
            )
        }

        // Lista de ingredientes en filas de 3
        val chunkedIngredientes = ingredientes.chunked(2) // Divide la lista en sublistas de 2 elementos

        chunkedIngredientes.forEach { filaIngredientes ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                //Espacio entre el titulo y los ingredientes
                Spacer(modifier = Modifier.height(8.dp))

                filaIngredientes.forEach { ingrediente ->
                    Text(
                        text = "• $ingrediente",
                        fontSize = 18.sp,
                        color = Color(0xFFFF5722), // Ingredientes en naranja
                        modifier = Modifier.padding(end = 16.dp)
                    )
                }
            }
        }
    }
}
