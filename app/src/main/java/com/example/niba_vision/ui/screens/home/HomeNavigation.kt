package com.example.niba_vision.ui.screens.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Define las tres pantallas principales dentro de la sección Home de la app.
 */
sealed class HomeRoute(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    data object Home : HomeRoute("home_feed", "Home", Icons.Default.Home)
    data object Cart : HomeRoute("home_cart", "Carrito", Icons.Default.ShoppingCart)
    data object Profile : HomeRoute("home_profile", "Perfil", Icons.Default.Person)
}