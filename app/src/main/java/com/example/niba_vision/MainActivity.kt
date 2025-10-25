package com.example.niba_vision

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.rememberNavController
import com.example.niba_vision.data.AppDatabase
import com.example.niba_vision.data.UserRepository
import com.example.niba_vision.ui.AppNavHost
import com.example.niba_vision.ui.theme.NIBA_VisionTheme

/**
 * Actividad principal y punto de entrada de la aplicación.
 *
 * Se encarga de configurar el tema de la aplicación y de inicializar el sistema de navegación
 * con Jetpack Compose.
 */
class MainActivity : ComponentActivity() {
    /**
     * Método que se llama cuando se crea la actividad.
     * Aquí se define el contenido de la interfaz de usuario.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        // setContent es el punto de entrada para la UI de Compose.
        super.onCreate(savedInstanceState)
        // Se aplica el tema personalizado de la aplicación.
        setContent {
            NIBA_VisionTheme {
                // Se inicializa el composable principal de la app.
                App()
            }
        }
    }
}

/**
 * Composable raíz de la aplicación.
 *
 * Configura el controlador de navegación (`NavController`) y el `NavHost` que gestionará
 * el cambio entre las diferentes pantallas (composables).
 */
@Composable
fun App() {
    val nav = rememberNavController()
    val context = LocalContext.current

    // Crea una única instancia del repositorio que se recordará durante toda la vida de la app
    val userRepository = remember {
        val db = AppDatabase.getDatabase(context)
        UserRepository(db.userDao())
    }

    // Pasa el repositorio al NavHost, que se encargará de distribuirlo a las pantallas
    AppNavHost(nav = nav, userRepository = userRepository)
}
