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
        composable(Route.Recover.route) {
            RecoverScreen(onBack = { nav.popBackStack() })
        }
        composable(Route.Home.route) {
            HomeAdaptiveScreen()
        }
    }
}