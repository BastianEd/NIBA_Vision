package com.example.niba_vision.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.niba_vision.navigation.Route
import com.example.niba_vision.ui.screens.*

/**
 * Define el grafo de navegación de la aplicación.
 *
 * Este Composable se encarga de asociar cada ruta definida en [Route] con su
 * pantalla (Composable) correspondiente. Gestiona la lógica de navegación,
 * como la pantalla de inicio y las transiciones entre pantallas.
 *
 * @param nav El [NavHostController] que gestiona la pila de navegación.
 */
@Composable
fun AppNavHost(nav: NavHostController) {
    // Define el NavHost, especificando el NavController y la ruta de inicio.
    NavHost(navController = nav, startDestination = Route.Login.route) {
        // Define la pantalla para la ruta de Login.
        composable(Route.Login.route) {
            LoginScreen(
                onRegister = { nav.navigate(Route.Register.route) },
                onRecover = { nav.navigate(Route.Recover.route) },
                onLoggedIn = {
                    // Navega a Home y limpia la pila de navegación hasta Login.
                    nav.navigate(Route.Home.route) {
                        popUpTo(Route.Login.route) { inclusive = true }
                    }
                }
            )
        }
        // Define la pantalla para la ruta de Registro.
        composable(Route.Register.route) {
            RegisterScreen(
                onBack = { nav.popBackStack() }, // Vuelve a la pantalla anterior.
                onRegistered = {
                    // Navega a Login después del registro exitoso.
                    nav.navigate(Route.Login.route) {
                        popUpTo(Route.Login.route) { inclusive = true }
                    }
                }
            )
        }
        // Define la pantalla para la ruta de Recuperación.
        composable(Route.Recover.route) { RecoverScreen(onBack = { nav.popBackStack() }) }

        // Define la pantalla para la ruta de Inicio (Home).
        composable(Route.Home.route) { HomeAdaptiveScreen() }
    }
}