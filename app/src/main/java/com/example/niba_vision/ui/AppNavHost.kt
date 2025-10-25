package com.example.niba_vision.ui

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.niba_vision.data.UserRepository
import com.example.niba_vision.navigation.Route
import com.example.niba_vision.ui.screens.auth.LoginScreen
import com.example.niba_vision.ui.screens.auth.RecoverScreen
import com.example.niba_vision.ui.screens.auth.RegisterScreen
import com.example.niba_vision.ui.screens.home.HomeAdaptiveScreen
import com.example.niba_vision.viewmodel.AppViewModelFactory

/**
 * Composable que configura el NavHost de la aplicación y las rutas principales.
 *
 * - Crea una única instancia de `AppViewModelFactory` usando `userRepository` y la reutiliza
 *   para proporcionar ViewModels que requieren acceso al repositorio.
 * - Define las rutas de navegación y callbacks entre pantallas de autenticación y la pantalla
 *   principal.
 *
 * Rutas y comportamiento:
 * - `Route.Login.route`
 *   - Muestra `LoginScreen` con `loginViewModel` creado por la fábrica.
 *   - `onRegister` -> navega a `Route.Register.route`.
 *   - `onRecover` -> navega a `Route.Recover.route`.
 *   - `onLoggedIn` -> navega a `Route.Home.route` y elimina la pila hasta `Route.Login.route` (inclusive).
 * - `Route.Register.route`
 *   - Muestra `RegisterScreen` con `registerViewModel` creado por la fábrica.
 *   - `onBack` -> vuelve atrás (`popBackStack`).
 *   - `onRegistered` -> navega a `Route.Login.route` y limpia `Route.Login.route` de la pila (inclusive).
 * - `Route.Recover.route`
 *   - Muestra `RecoverScreen` con `onBack` que hace `popBackStack`.
 * - `Route.Home.route`
 *   - Muestra `HomeAdaptiveScreen`.
 *
 * @param nav Controlador de navegación usado para mover entre pantallas.
 * @param userRepository Repositorio de usuario inyectado en los ViewModels mediante la fábrica.
 */
// AppNavHost ahora recibe el UserRepository para poder crear los ViewModels
@Composable
fun AppNavHost(nav: NavHostController, userRepository: UserRepository) {
    // Crea la fábrica una sola vez y pásala a los ViewModels que la necesiten
    val viewModelFactory = AppViewModelFactory(userRepository)
    NavHost(navController = nav, startDestination = Route.Login.route) {
        composable(Route.Login.route) {
            LoginScreen(
                onRegister = { nav.navigate(Route.Register.route) },
                onRecover = { nav.navigate(Route.Recover.route) },
                onLoggedIn = {
                    nav.navigate(Route.Home.route) {
                        popUpTo(Route.Login.route) { inclusive = true }
                    }
                },
                // Usa la fábrica para que el ViewModel reciba el repositorio
                loginViewModel = viewModel(factory = viewModelFactory)
            )
        }
        composable(Route.Register.route) {
            RegisterScreen(
                onBack = { nav.popBackStack() },
                onRegistered = {
                    nav.navigate(Route.Login.route) {
                        popUpTo(Route.Login.route) { inclusive = true }
                    }
                },
                // Usa la fábrica para que el ViewModel reciba el repositorio
                registerViewModel = viewModel(factory = viewModelFactory)
            )
        }
        composable(Route.Recover.route) {
            RecoverScreen(
                onBack = { nav.popBackStack() }
            )
        }
        composable(Route.Home.route) {
            HomeAdaptiveScreen()
        }
    }
}