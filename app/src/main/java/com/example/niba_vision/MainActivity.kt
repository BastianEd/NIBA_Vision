package com.example.niba_vision

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.compose.rememberNavController
import com.example.niba_vision.data.ApiClient
import com.example.niba_vision.data.BookRepository
import com.example.niba_vision.data.CartRepository
import com.example.niba_vision.data.SessionManager
import com.example.niba_vision.data.UserRepository
import com.example.niba_vision.ui.AppNavHost
import com.example.niba_vision.view.theme.NIBA_VisionTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // --- INICIALIZACIÓN DE PERSISTENCIA ---
        // Inicializamos la sesión y el carrito para que carguen datos guardados
        SessionManager.init(this)
        CartRepository.init(this)
        // --------------------------------------

        setContent {
            NIBA_VisionTheme {
                App()
            }
        }
    }
}

@Composable
fun App() {
    val nav = rememberNavController()

    // Obtenemos la instancia de la API
    val apiService = ApiClient.instance

    // Inyección de dependencias manual
    val userRepository = remember { UserRepository(apiService) }
    val bookRepository = remember { BookRepository(apiService) }

    // CartRepository y SessionManager son Objects (Singletons),
    // no necesitan instanciarse, pero los pasamos para mantener la estructura.
    val cartRepository = CartRepository

    AppNavHost(
        nav = nav,
        userRepository = userRepository,
        bookRepository = bookRepository,
        cartRepository = cartRepository
    )
}