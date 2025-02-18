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
import com.example.proyectoapi.proyectoapi.pedroluis.data.firebase.FirestoreManager
import com.example.proyectoapi.proyectoapi.pedroluis.ui.navegacion.Pantalla1
import com.example.proyectoapi.proyectoapi.pedroluis.ui.navegacion.Pantalla2
import com.example.proyectoapi.proyectoapi.pedroluis.ui.navegacion.Pantalla3
import com.example.proyectoapi.proyectoapi.pedroluis.ui.screen.PantallaLoginScreen.Pantalla1Screen
import com.example.proyectoapi.proyectoapi.pedroluis.ui.screen.PantallaListaScreen.Pantalla2ViewModel
import com.example.proyectoapi.proyectoapi.pedroluis.ui.navegacion.Carrito
import com.example.proyectoapi.proyectoapi.pedroluis.ui.navegacion.Crear
import com.example.proyectoapi.proyectoapi.pedroluis.ui.navegacion.ForgotPassword
import com.example.proyectoapi.proyectoapi.pedroluis.ui.navegacion.Perfil
import com.example.proyectoapi.proyectoapi.pedroluis.ui.navegacion.SignUp
import com.example.proyectoapi.proyectoapi.pedroluis.ui.screen.CrearCoctelScreen.CrearCoctelScreen
import com.example.proyectoapi.proyectoapi.pedroluis.ui.screen.FavoritoScreen.PantallaFavoritosScreen
import com.example.proyectoapi.proyectoapi.pedroluis.ui.screen.PantallaLoginScreen.Pantalla1ForgotPasswordScreen
import com.example.proyectoapi.proyectoapi.pedroluis.ui.screen.PantallaLoginScreen.Pantalla1SignUpScreen
import com.example.proyectoapi.proyectoapi.pedroluis.ui.screen.PerfilScreen.PerfilScreen
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.filter


@Composable
fun Navegacion(
    auth: AuthManager,
    firestoreManager: FirestoreManager, // Ahora FirestoreManager se recibe como parámetro
    viewModel: Pantalla2ViewModel


) {
    val listaProductosAPI by viewModel.bebidas.observeAsState(emptyList())
    val progressBar by viewModel.progressBar.observeAsState(true)

    val navController = rememberNavController()

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
            Pantalla2Screen(auth, viewModel, // Usamos el viewModelAPI que ya se pasa
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
                },
                {
                    navController.navigate(Crear)
                }
            )
        }

        composable<Pantalla3> { backStackEntry ->
            val id = backStackEntry.toRoute<Pantalla3>().idDrink
            viewModel.cargarBebidaId(id)
            Pantalla3DetalleScreen(
                auth, viewModel, firestoreManager, // Se pasa firestoreManager correctamente
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

        composable<Carrito> {
            PantallaFavoritosScreen(
                firestoreManager,
                navegarAPantalla2 = {
                    navController.navigate(Pantalla2) {
                        popUpTo(Pantalla2) { inclusive = true }
                    }
                }
            )
        }

        composable<Perfil> {
            PerfilScreen(auth,
                navigateToBack = {
                    navController.navigate(Pantalla2) {
                        popUpTo(Pantalla2) { inclusive = true }
                    }
                },
            ) {
                navController.navigate(Pantalla2) {
                    popUpTo(Pantalla2) { inclusive = true }
                }
            }
        }

        composable<Crear> {
            CrearCoctelScreen(
                auth,
                navController,
                viewModel,
                navegarAPantalla2 = {
                    navController.navigate(Pantalla2) {
                        popUpTo(Pantalla2) { inclusive = true }
                    }
                }
            )
        }

    }
}
