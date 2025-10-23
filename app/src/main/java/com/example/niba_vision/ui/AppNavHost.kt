package com.example.niba_vision.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.niba_vision.navigation.Route
import com.example.niba_vision.ui.screens.auth.LoginScreen
import com.example.niba_vision.ui.screens.auth.RecoverScreen
import com.example.niba_vision.ui.screens.auth.RegisterScreen
import com.example.niba_vision.ui.screens.home.HomeAdaptiveScreen

// 🚀 Esta función define toda la navegación de la app NIBA Vision.
// Aquí se controlan las pantallas (rutas) y cómo se mueve el usuario entre ellas.
@Composable
fun AppNavHost(nav: NavHostController) {

    // 🗺️ NavHost es el "contenedor" principal de navegación.
    // Define la primera pantalla (startDestination) y todas las rutas disponibles.
    NavHost(navController = nav, startDestination = Route.Login.route) {

        // 🔑 Pantalla de inicio de sesión (Login)
        composable(Route.Login.route) {
            LoginScreen(
                // 👉 Si el usuario toca "Crear cuenta", lo llevamos a la pantalla de registro.
                onRegister = { nav.navigate(Route.Register.route) },

                // 👉 Si toca "¿Olvidaste tu contraseña?", va a la pantalla de recuperación.
                onRecover = { nav.navigate(Route.Recover.route) },

                // 👉 Si el login fue exitoso, lo llevamos a Home y
                // quitamos la pantalla de Login del historial para no volver atrás.
                onLoggedIn = {
                    nav.navigate(Route.Home.route) {
                        popUpTo(Route.Login.route) { inclusive = true }
                    }
                }
            )
        }

        // 📝 Pantalla de registro de nuevo usuario.
        composable(Route.Register.route) {
            RegisterScreen(
                // 👉 Vuelve atrás si el usuario presiona “volver”.
                onBack = { nav.popBackStack() },

                // 👉 Una vez registrado, lo llevamos de vuelta al Login.
                onRegistered = {
                    nav.navigate(Route.Login.route) {
                        popUpTo(Route.Login.route) { inclusive = true }
                    }
                }
            )
        }

        // 🔄 Pantalla de recuperación de contraseña.
        composable(Route.Recover.route) {
            RecoverScreen(
                // 👉 Solo tiene opción de volver atrás.
                onBack = { nav.popBackStack() }
            )
        }

        // 🏠 Pantalla principal de la app (Home)
        composable(Route.Home.route) {
            HomeAdaptiveScreen() // 👉 Aquí entra el usuario una vez logueado correctamente.
        }
    }
}
