package com.example.niba_vision

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.rememberNavController
import com.example.niba_vision.data.AppDatabase
import com.example.niba_vision.data.BookRepository
import com.example.niba_vision.data.CartRepository
import com.example.niba_vision.data.UserRepository
import com.example.niba_vision.ui.AppNavHost
import com.example.niba_vision.ui.theme.NIBA_VisionTheme

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
    val context = LocalContext.current

    val userRepository = remember {
        val db = AppDatabase.getDatabase(context)
        UserRepository(db.userDao())
    }

    val bookRepository = remember { BookRepository() }

    // El CartRepository es un 'object' (singleton), así que lo pasamos directamente
    val cartRepository = CartRepository

    // Pasa todos los repositorios al NavHost
    AppNavHost(
        nav = nav,
        userRepository = userRepository,
        bookRepository = bookRepository,
        cartRepository = cartRepository // <-- AÑADIDO
    )
}