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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenCompacta(
    viewModelFactory: AppViewModelFactory,
    onLogout: () -> Unit
) {
    val viewModel: HomeViewModel = viewModel(factory = viewModelFactory)
    val uiState by viewModel.uiState.collectAsState()

    // Lista de destinos para la barra inferior
    val navItems = listOf(HomeRoute.Home, HomeRoute.Cart, HomeRoute.Profile)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "ZONALIBROS") }
                // La cesta ahora está en la barra inferior,
                // pero podrías dejar el icono aquí si quisieras (uiState.cartItemCount)
            )
        },
        bottomBar = {
            NavigationBar {
                navItems.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = screen.title) },
                        label = { Text(screen.title) },
                        selected = (screen == uiState.selectedScreen),
                        onClick = { viewModel.onHomeNavigationSelected(screen) }
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding), // <-- El padding del Scaffold
            contentAlignment = Alignment.Center
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator()
            } else {
                // Muestra el contenido basado en la pestaña seleccionada
                when (uiState.selectedScreen) {
                    HomeRoute.Home -> BookListScreen(
                        books = uiState.books,
                        onAddToCart = { viewModel.addToCart(it) },
                        minSize = 160.dp
                    )
                    HomeRoute.Cart -> CartScreen()
                    HomeRoute.Profile -> ProfileScreen(
                        viewModelFactory = viewModelFactory,
                        onLogout = onLogout
                    )
                }
            }
        }
    }
}

// ... (El Preview se mantiene igual, no es necesario cambiarlo) ...