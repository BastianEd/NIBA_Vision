package com.example.niba_vision.ui

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.niba_vision.data.BookRepository
import com.example.niba_vision.data.CartRepository
import com.example.niba_vision.data.UserRepository
import com.example.niba_vision.navigation.Route
import com.example.niba_vision.ui.screens.auth.LoginScreen
import com.example.niba_vision.ui.screens.auth.RecoverScreen
import com.example.niba_vision.ui.screens.auth.RegisterScreen
import com.example.niba_vision.ui.screens.home.HomeAdaptiveScreen
import com.example.niba_vision.viewmodel.AppViewModelFactory


@Composable
fun AppNavHost(
    nav: NavHostController,
    userRepository: UserRepository,
    bookRepository: BookRepository,
    cartRepository: CartRepository // <-- AÑADIDO
) {
    // Crea la fábrica una sola vez con todos los repositorios
    val viewModelFactory = AppViewModelFactory(
        userRepository = userRepository,
        bookRepository = bookRepository,
        cartRepository = cartRepository // <-- AÑADIDO
    )

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
                registerViewModel = viewModel(factory = viewModelFactory)
            )
        }
        composable(Route.Recover.route) {
            RecoverScreen(
                onBack = { nav.popBackStack() }
            )
        }
        composable(Route.Home.route) {
            HomeAdaptiveScreen(
                viewModelFactory = viewModelFactory,
                onLogout = {
                    nav.navigate(Route.Login.route) {
                        popUpTo(Route.Home.route) { inclusive = true }
                    }
                }
            )
        }
    }
}