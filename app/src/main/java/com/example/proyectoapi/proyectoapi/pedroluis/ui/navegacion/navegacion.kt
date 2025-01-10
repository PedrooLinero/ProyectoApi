import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.proyectoapi.proyectoapi.pedroluis.ui.screen.Pantalla1.Pantalla1Screen
import com.example.proyectoapi.proyectoapi.pedroluis.ui.viewmodel.Pantalla2ViewModel

@Composable
fun Navegacion() {
    val navController = rememberNavController()

    // Inicializamos el ViewModel aquí
    val pantalla2ViewModel: Pantalla2ViewModel = viewModel()

    NavHost(navController = navController, startDestination = "pantalla1") {
        composable("pantalla1") {
            Pantalla1Screen(navegarAPantalla2 = { usuario ->
                navController.navigate("pantalla2/$usuario")
            })
        }
        composable("pantalla2/{usuario}") { backStackEntry ->
            val usuario = backStackEntry.arguments?.getString("usuario") ?: "Usuario"
            Pantalla2Screen(
                navegarAPantalla1 = { navController.popBackStack() },
                usuario = usuario,  // Pasamos el parámetro usuario
                pantalla2ViewModel = pantalla2ViewModel // Ahora se pasa el ViewModel correctamente
            )
        }
    }
}
