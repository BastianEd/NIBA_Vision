package com.example.niba_vision.ui.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.niba_vision.viewmodel.AppViewModelFactory
import com.example.niba_vision.viewmodel.HomeViewModel

/**
 * Representa la pantalla principal para dispositivos compactos (teléfonos).
 * Utiliza una barra de navegación inferior (NavigationBar).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenCompacta(
    viewModelFactory: AppViewModelFactory,
    onLogout: () -> Unit
) {
    // Obtiene la instancia del HomeViewModel usando la fábrica
    val viewModel: HomeViewModel = viewModel(factory = viewModelFactory)
    // Observa el estado de la UI
    val uiState by viewModel.uiState.collectAsState()

    // Lista de destinos para la barra inferior
    val navItems = listOf(HomeRoute.Home, HomeRoute.Cart, HomeRoute.Profile)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "ZONALIBROS") }
            )
        },
        bottomBar = {
            // Barra de navegación inferior
            NavigationBar {
                navItems.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = screen.title) },
                        label = { Text(screen.title) },
                        selected = (screen == uiState.selectedScreen), // Marca el ítem seleccionado
                        onClick = { viewModel.onHomeNavigationSelected(screen) } // Cambia de pestaña
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding), // Aplica el padding del Scaffold
            contentAlignment = Alignment.Center
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator()
            } else {
                // Navegación interna: Muestra el contenido basado en la pestaña seleccionada
                when (uiState.selectedScreen) {
                    HomeRoute.Home -> BookListScreen(
                        books = uiState.books,
                        onAddToCart = { viewModel.addToCart(it) },
                        minSize = 160.dp
                    )
                    // --- CAMBIO AQUÍ ---
                    // Se pasa la instancia del viewModel a la CartScreen
                    HomeRoute.Cart -> CartScreen(viewModel = viewModel)
                    // --------------------
                    HomeRoute.Profile -> ProfileScreen(
                        viewModelFactory = viewModelFactory,
                        onLogout = onLogout
                    )
                }
            }
        }
    }
}