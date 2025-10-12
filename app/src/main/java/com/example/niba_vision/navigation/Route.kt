package com.example.niba_vision.navigation

sealed class Route(val route: String) {
    data object Login : Route("login")
    data object Register : Route("register")
    data object Recover : Route("recover")
    data object Home : Route("home")
}
