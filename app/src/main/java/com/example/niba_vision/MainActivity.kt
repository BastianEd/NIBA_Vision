package com.example.niba_vision

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.example.niba_vision.ui.AppNavHost
import com.example.niba_vision.view.theme.NIBA_VisionTheme

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
    // Crea y recuerda el NavController para manejar la pila de navegación.
    val nav = rememberNavController()
    // Define el grafo de navegación, conectando rutas con sus respectivos composables.
    AppNavHost(nav)
}
