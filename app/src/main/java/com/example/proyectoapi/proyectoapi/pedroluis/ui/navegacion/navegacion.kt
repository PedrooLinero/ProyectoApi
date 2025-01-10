import androidx.compose.runtime.Composable
import androidx.compose.runtime.internal.composableLambda
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.toRoute
import com.example.proyectoapi.proyectoapi.pedroluis.ui.navegacion.Pantalla1
import com.example.proyectoapi.proyectoapi.pedroluis.ui.navegacion.Pantalla2
import com.example.proyectoapi.proyectoapi.pedroluis.ui.navegacion.Pantalla3
import com.example.proyectoapi.proyectoapi.pedroluis.ui.screen.PantallaLoginScreen.Pantalla1Screen
import com.example.proyectoapi.proyectoapi.pedroluis.ui.viewmodel.Pantalla2ViewModel

@Composable
fun Navegacion() {
    val navController = rememberNavController()

    // Inicializamos el ViewModel aquí
    val pantalla2ViewModel: Pantalla2ViewModel = viewModel()

    NavHost(navController = navController, startDestination = Pantalla1) {
        composable<Pantalla1> {
            Pantalla1Screen(navegarAPantalla2 = { usuario ->
                navController.navigate(Pantalla2(usuario))
            })
        }
        composable<Pantalla2> { backStackEntry ->
            val usuario = backStackEntry.toRoute<Pantalla2>().usuario
            Pantalla2Screen(
                navegarAPantalla1 = { navController.popBackStack() },
                usuario = usuario,  // Pasamos el parámetro usuario
                pantalla2ViewModel = pantalla2ViewModel // Ahora se pasa el ViewModel correctamente
            )
        }
    }
}
