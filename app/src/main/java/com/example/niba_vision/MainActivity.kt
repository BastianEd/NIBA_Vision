package com.example.niba_vision

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.compose.rememberNavController
import com.example.niba_vision.data.BookRepository
import com.example.niba_vision.data.CartRepository
import com.example.niba_vision.data.UserRepository
import com.example.niba_vision.ui.AppNavHost
import com.example.niba_vision.ui.theme.NIBA_VisionTheme
import com.example.niba_vision.data.ApiClient

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
    // val context = LocalContext.current // Ya no es necesario para el UserRepository

    // --- INICIO DE CAMBIOS ---

    // 1. Obtenemos la instancia de nuestro cliente de API (Retrofit)
    val apiService = ApiClient.instance

    // 2. Creamos el UserRepository con el ApiService
    val userRepository = remember {
        UserRepository(apiService)
    }

    // (AppDatabase y UserDao ya no son necesarios para el login/registro)

    // --- FIN DE CAMBIOS ---

    val bookRepository = remember { BookRepository() }
    val cartRepository = CartRepository

    // Pasa todos los repositorios al NavHost (esto no cambia)
    AppNavHost(
        nav = nav,
        userRepository = userRepository,
        bookRepository = bookRepository,
        cartRepository = cartRepository
    )
}