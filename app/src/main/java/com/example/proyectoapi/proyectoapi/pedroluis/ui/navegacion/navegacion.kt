import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.proyectoapi.proyectoapi.pedroluis.data.firebase.AuthManager
import com.example.proyectoapi.proyectoapi.pedroluis.ui.navegacion.Pantalla1
import com.example.proyectoapi.proyectoapi.pedroluis.ui.navegacion.Pantalla2
import com.example.proyectoapi.proyectoapi.pedroluis.ui.navegacion.Pantalla3
import com.example.proyectoapi.proyectoapi.pedroluis.ui.screen.PantallaLoginScreen.Pantalla1Screen
import com.example.proyectoapi.proyectoapi.pedroluis.data.model.Pantalla2ViewModel
import com.example.proyectoapi.proyectoapi.pedroluis.ui.navegacion.ForgotPassword
import com.example.proyectoapi.proyectoapi.pedroluis.ui.navegacion.SignUp
import com.example.proyectoapi.proyectoapi.pedroluis.ui.screen.PantallaLoginScreen.Pantalla1ForgotPasswordScreen
import com.example.proyectoapi.proyectoapi.pedroluis.ui.screen.PantallaLoginScreen.Pantalla1SignUpScreen

@Composable
fun Navegacion(auth : AuthManager) {
    val navController = rememberNavController()
    val viewModel = Pantalla2ViewModel()
    viewModel.cargarBebidas()

    NavHost(navController = navController, startDestination = Pantalla1) {
        composable<Pantalla1> {
            Pantalla1Screen(auth,
                {
                    navController.navigate(Pantalla2){
                        popUpTo(Pantalla1){
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
                {id ->
                    navController.navigate(Pantalla3(id.toString()))
                },
                {
                    navController.navigate(Pantalla1) {
                        popUpTo(Pantalla2) { inclusive = true }
                    }
                }
            )
        }

        composable<Pantalla3> { backStackEntry ->
            val id = backStackEntry.toRoute<Pantalla3>().idDrink
            viewModel.cargarBebidaId(id)
            Pantalla3DetalleScreen(auth, viewModel,
                {
                    navController.navigate(Pantalla2) {
                        popUpTo(Pantalla3) { inclusive = true }
                    }
                },
                {
                    navController.navigate(Pantalla1) {
                        popUpTo(Pantalla3) { inclusive = true }
                    }
                }
                )
        }
   }
}

