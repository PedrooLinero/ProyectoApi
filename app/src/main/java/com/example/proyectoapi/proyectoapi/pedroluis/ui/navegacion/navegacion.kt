import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.snapshotFlow
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.proyectoapi.proyectoapi.pedroluis.data.firebase.AuthManager
import com.example.proyectoapi.proyectoapi.pedroluis.data.firebase.FirestoreViewModel
import com.example.proyectoapi.proyectoapi.pedroluis.ui.navegacion.Pantalla1
import com.example.proyectoapi.proyectoapi.pedroluis.ui.navegacion.Pantalla2
import com.example.proyectoapi.proyectoapi.pedroluis.ui.navegacion.Pantalla3
import com.example.proyectoapi.proyectoapi.pedroluis.ui.screen.PantallaLoginScreen.Pantalla1Screen
import com.example.proyectoapi.proyectoapi.pedroluis.ui.screen.PantallaListaScreen.Pantalla2ViewModel
import com.example.proyectoapi.proyectoapi.pedroluis.ui.navegacion.Carrito
import com.example.proyectoapi.proyectoapi.pedroluis.ui.navegacion.ForgotPassword
import com.example.proyectoapi.proyectoapi.pedroluis.ui.navegacion.Perfil
import com.example.proyectoapi.proyectoapi.pedroluis.ui.navegacion.SignUp
import com.example.proyectoapi.proyectoapi.pedroluis.ui.screen.CarritoScreen.CarritoScreen
import com.example.proyectoapi.proyectoapi.pedroluis.ui.screen.PantallaLoginScreen.Pantalla1ForgotPasswordScreen
import com.example.proyectoapi.proyectoapi.pedroluis.ui.screen.PantallaLoginScreen.Pantalla1SignUpScreen
import com.example.proyectoapi.proyectoapi.pedroluis.ui.screen.PerfilScreen.PerfilScreen
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.filter


@Composable
fun Navegacion(
    auth: AuthManager,
    viewModelAPI: Pantalla2ViewModel,
    viewModelFirestore: FirestoreViewModel
    ) {

    val listaProductosAPI by viewModelAPI.bebidas.observeAsState(emptyList())
    val progressBar by viewModelFirestore.isLoading.observeAsState(true)

    val navController = rememberNavController()
    val viewModel = Pantalla2ViewModel()
    viewModel.cargarBebidas()

    LaunchedEffect(Unit) {
        coroutineScope {
            val apiDeferred = async { viewModelAPI.cargarBebidas() }
            val firestoreDeferred = async { viewModelFirestore.loadFirestoreProducts() }

            apiDeferred.await()
            firestoreDeferred.await()
        }

        // Observar cuando la API tenga datos
        snapshotFlow { listaProductosAPI }
            .filter { it.isNotEmpty() }
            .collect { bebidas ->
                viewModelFirestore.syncProducts(bebidas)
            }
    }

    NavHost(navController = navController, startDestination = Pantalla1) {
        composable<Pantalla1> {
            Pantalla1Screen(auth,
                {
                    navController.navigate(Pantalla2) {
                        popUpTo(Pantalla1) {
                            inclusive = true
                        }
                    }
                },
                {
                    navController.navigate(SignUp)
                },
                {
                    navController.navigate(ForgotPassword)
                }
            )
        }
        composable<SignUp> {
            Pantalla1SignUpScreen(auth) {
                navController.navigate(Pantalla1) {
                    popUpTo(Pantalla1) { inclusive = true }
                }
            }
        }

        composable<ForgotPassword> {
            Pantalla1ForgotPasswordScreen(auth) {
                navController.navigate(Pantalla1) {
                    popUpTo(Pantalla1) { inclusive = true }
                }
            }
        }

        composable<Pantalla2> {
            Pantalla2Screen(auth, viewModel,
                {
                    navController.navigate(Pantalla1) {
                        popUpTo(Pantalla2) { inclusive = true }
                    }
                },
                navegarAPantalla3 = { id ->
                    navController.navigate(Pantalla3(id))
                },
                {
                    navController.navigate(Carrito)
                },
                {
                    navController.navigate(Perfil)
                }
            )
        }

        composable<Pantalla3> { backStackEntry ->
            val id = backStackEntry.toRoute<Pantalla3>().idDrink
            viewModel.cargarBebidaId(id)
            Pantalla3DetalleScreen(
                 auth, viewModel, viewModelFirestore,
                {
                    navController.navigate(Pantalla2) {
                        popUpTo(Pantalla2) { inclusive = true }
                    }
                },
                {
                  navController.navigate(Carrito)
                },
                {
                    navController.navigate(Perfil)
                }
            )
        }

        // Asegúrate de definir la ruta Carrito
        // Define the route Carrito
        composable<Carrito> {
            CarritoScreen(
                auth,
                viewModelFirestore,
                {
                    navController.navigate(Pantalla2) {
                        popUpTo(Pantalla2) { inclusive = true }
                    }
                },
                {
                    navController.navigate(Perfil)
                },
            )
        }

        composable<Perfil> {
            PerfilScreen(auth) {
                navController.navigate(Pantalla2) {
                    popUpTo(Pantalla2) { inclusive = true }
                }
            }
        }

    }
}