package com.example.niba_vision.navigation

/**
 * Define las rutas de navegación de la aplicación de forma segura (type-safe).
 *
 * Esta sealed class contiene un objeto por cada pantalla de la aplicación,
 * asegurando que solo se puedan usar rutas predefinidas y evitando errores de tipeo.
 *
 * @property route El identificador de la ruta como una cadena de texto.
 */
sealed class Route(val route: String) {
    data object Login : Route("login")
    data object Register : Route("register")
    data object Recover : Route("recover")
    data object Home : Route("home")
}