import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.toRoute
import com.example.proyectoapi.proyectoapi.pedroluis.ui.navegacion.Pantalla1
import com.example.proyectoapi.proyectoapi.pedroluis.ui.navegacion.Pantalla2
import com.example.proyectoapi.proyectoapi.pedroluis.ui.navegacion.Pantalla3
import com.example.proyectoapi.proyectoapi.pedroluis.ui.screen.PantallaLoginScreen.Pantalla1Screen
import com.example.proyectoapi.proyectoapi.pedroluis.data.model.Pantalla2ViewModel

@Composable
fun Navegacion() {
    // Inicializa el controlador de navegación
    val navController = rememberNavController()

    // Inicializamos el ViewModel aquí
    val pantalla2ViewModel: Pantalla2ViewModel = viewModel()

    // Definimos las pantallas de la aplicación
    NavHost(navController = navController, startDestination = Pantalla1) {
        composable<Pantalla1> {
            Pantalla1Screen(navegarAPantalla2 = { usuario ->
                navController.navigate(Pantalla2(usuario))
            })
        }
        composable<Pantalla2> { backStackEntry ->
            val usuario = backStackEntry.toRoute<Pantalla2>().usuario
            Pantalla2Screen(
                // Pasamos las funciones de navegación
                navegarAPantalla1 = { navController.popBackStack(Pantalla1, inclusive = false ) },
                usuario = usuario,
                pantalla2ViewModel = pantalla2ViewModel,
                navegarAPantalla3 = { idDrink ->
                    navController.navigate(Pantalla3(idDrink, usuario))
                }
            )
        }

        composable<Pantalla3> {backStackEntry ->
            val idDrink = backStackEntry.toRoute<Pantalla3>().idDrink
            val usuario = backStackEntry.toRoute<Pantalla3>().usuario
            Pantalla3DetalleScreen(
                idDrink = idDrink,
                usuario = usuario,
            ) {
                navController.navigate(Pantalla2(backStackEntry.toRoute<Pantalla3>().usuario)) {
                    popUpTo(Pantalla2(backStackEntry.toRoute<Pantalla3>().usuario)) {
                        inclusive = true
                    }
                }
            }
        }
    }
}

