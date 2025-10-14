package com.example.niba_vision.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.niba_vision.navigation.Route
import com.example.niba_vision.ui.screen.* // Asegúrate de que este import cubra todas tus pantallas
import com.example.niba_vision.ui.screens.*

@Composable
fun AppNavHost(nav: NavHostController) {
    NavHost(navController = nav, startDestination = Route.Login.route) {
        composable(Route.Login.route) {
            LoginScreen(
                onRegister = { nav.navigate(Route.Register.route) },
                onRecover = { nav.navigate(Route.Recover.route) },
                onLoggedIn = {
                    nav.navigate(Route.Home.route) {
                        popUpTo(Route.Login.route) { inclusive = true }
                    }
                }
            )
        }
        composable(Route.Register.route) {
            RegisterScreen(
                onBack = { nav.popBackStack() },
                onRegistered = {
                    nav.navigate(Route.Login.route) {
                        popUpTo(Route.Login.route) { inclusive = true }
                    }
                }
            )
        }
        composable(Route.Recover.route) { RecoverScreen(onBack = { nav.popBackStack() }) }

        // MODIFICACIÓN DE LA GUÍA 9: La ruta Home ahora llama a la pantalla adaptativa
        composable(Route.Home.route) { HomeAdaptiveScreen() }
    }
}