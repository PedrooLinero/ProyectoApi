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
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.proyectoapi.proyectoapi.pedroluis.data.repositories.model.Drink
import com.example.proyectoapi.proyectoapi.pedroluis.data.model.Pantalla2ViewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.ui.text.style.TextAlign

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Pantalla2Screen(
    navegarAPantalla1: () -> Unit, // Función para navegar a la pantalla 1
    navegarAPantalla3: (String) -> Unit, // Función para navegar a la pantalla 3
    usuario: String, // Nombre del usuario
    pantalla2ViewModel: Pantalla2ViewModel // ViewModel de la pantalla 2
) {

    // Obtener el estado de las bebidas desde el ViewModel
    val bebidasState: State<List<Drink>> =
        pantalla2ViewModel.bebidas.collectAsState(initial = emptyList())

    // Scaffold con la AppBar y el contenido de la pantalla
    Scaffold(
        // TopBar para mostrar el título y el menú de usuario
        topBar = {
            TopAppBar(
                title = {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "Nightcap Lounge",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Serif,
                                color = Color(0xFF333333)
                            ),
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                },

                // Navegar a la pantalla 1 al hacer clic en el botón de retroceso
                navigationIcon = {
                    IconButton(onClick = { navegarAPantalla1() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = Color(0xFF333333)
                        )
                    }
                },

                // Menú de usuario con el nombre del usuario
                actions = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(end = 16.dp)
                    ) {
                        Text(
                            text = usuario,
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
                },
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Contenido de la pantalla
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color.White)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Déjate seducir por el sabor de la creatividad en cada trago. ¡Prueba nuestros cócteles y descubre tu favorito!",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 21.sp,
                    fontFamily = FontFamily.Serif,
                    color = Color(0xFFFF7043),
                    letterSpacing = 0.5.sp,
                    lineHeight = 28.sp,
                    textAlign = TextAlign.Center
                ),
                color = Color(0xFFFF7043),
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 16.dp)
            )

            // Mostrar los cards de bebidas
            if (bebidasState.value.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    // Mostrar un CircularProgressIndicator mientras se cargan las bebidas
                    CircularProgressIndicator()
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Iterar sobre la lista de bebidas y mostrar un card para cada una de ellas
                    items(bebidasState.value) { bebida ->
                        CocktailCard(bebida, navegarAPantalla3 = navegarAPantalla3)
                    }
                }
            }
        }
    }
}

// Card para mostrar la información de una bebida
@Composable
fun CocktailCard(bebida: Drink, navegarAPantalla3: (String) -> Unit) {
    // Fondo blanco para los cards
    val cardBackgroundColor = Color.White

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable { navegarAPantalla3(bebida.idDrink) }
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
                    painter = rememberAsyncImagePainter(bebida.strDrinkThumb),
                    contentDescription = "Imagen de ${bebida.strDrink}",
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 200.dp),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = bebida.strDrink,
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
                    text = "Categoría: ${bebida.strCategory}",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        fontFamily = FontFamily.Serif
                    ),
                    color = Color(0xFFFF7043)
                )

                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    text = "Alcohol: ${if (bebida.strAlcoholic == "Alcoholic") "Sí" else "No"}",
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
