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
import com.example.proyectoapi.proyectoapi.pedroluis.data.model.Drink
import com.example.proyectoapi.proyectoapi.pedroluis.ui.viewmodel.Pantalla2ViewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Pantalla2Screen(
    navegarAPantalla1: () -> Unit,
    usuario: String,
    pantalla2ViewModel: Pantalla2ViewModel
) {
    val bebidasState: State<List<Drink>> =
        pantalla2ViewModel.bebidas.collectAsState(initial = emptyList())

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "Nightcap Lounge",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontSize = 25.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Serif,
                                color = Color.White // Usar blanco para que contraste con el fondo oscuro
                            ),
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navegarAPantalla1() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = Color.White // Los iconos también en blanco para contrastar
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
                                fontSize = 18.sp,
                                color = Color.White // Texto en blanco
                            )
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = "Menú de usuario",
                            modifier = Modifier
                                .background(Color.Gray.copy(alpha = 0.2f), shape = CircleShape)
                                .padding(8.dp),
                            tint = Color.White // El icono de cuenta también en blanco
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = Color(0xFF333333) // Usar containerColor en lugar de backgroundColor
                )
            )
        }
    ) { innerPadding ->
        // Fondo degradado aplicado aquí en el Column
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            Color(0xFFFF5722), // Naranja
                            Color(0xFFF44336), // Rojo
                            Color(0xFFFFEB3B)  // Amarillo
                        )
                    )
                )
        ) {
            Spacer(modifier = Modifier.height(30.dp)) // Espacio adicional entre el nombre y los cards

            // Mostrar los cards de bebidas
            if (bebidasState.value.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(bebidasState.value) { bebida ->
                        CocktailCard(bebida)
                    }
                }
            }
        }
    }
}

@Composable
fun CocktailCard(bebida: Drink) {
    // Establecer el mismo color para el fondo que el del TopAppBar
    val cardBackgroundColor = Color(0xFF333333)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable { /* Acción al hacer clic en un cóctel */ }
            .padding(vertical = 8.dp), // Padding adicional entre cards
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = cardBackgroundColor) // Fondo del card en gris oscuro
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(12.dp) // Padding dentro del card
        ) {
            // Imagen sin redondeo en las esquinas
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

            Spacer(modifier = Modifier.height(8.dp)) // Espacio entre la imagen y el texto

            // Nombre del cóctel
            Text(
                text = bebida.strDrink,
                modifier = Modifier.padding(horizontal = 12.dp),
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold,  // Usar un peso negrita
                    fontSize = 24.sp,  // Tamaño más grande
                    fontFamily = FontFamily.Serif
                ),
                color = Color.White // Texto blanco
            )

            // Espacio entre nombre y la información
            Spacer(modifier = Modifier.height(8.dp))

            // Información en una sola línea centrada
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Categoría
                Text(
                    text = "Categoría: ${bebida.strCategory}",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold,  // Letras en negrita
                        fontSize = 16.sp,
                        fontFamily = FontFamily.Serif
                    ),
                    color = Color.White // Texto blanco
                )

                Spacer(modifier = Modifier.width(16.dp)) // Espacio entre los textos

                // Alcohol
                Text(
                    text = "Alcohol: ${if (bebida.strAlcoholic == "Alcoholic") "Sí" else "No"}",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold,  // Letras en negrita
                        fontSize = 16.sp,
                        fontFamily = FontFamily.Serif
                    ),
                    color = Color.White // Texto blanco
                )
            }
        }
    }
}

