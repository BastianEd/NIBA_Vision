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
 * Representa la pantalla principal para dispositivos medianos (tablets pequeñas).
 * También utiliza una barra de navegación inferior.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenMediana(
    viewModelFactory: AppViewModelFactory,
    onLogout: () -> Unit
) {
    val viewModel: HomeViewModel = viewModel(factory = viewModelFactory)
    val uiState by viewModel.uiState.collectAsState()

    val navItems = listOf(HomeRoute.Home, HomeRoute.Cart, HomeRoute.Profile)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "ZONALIBROS") }
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
                .padding(innerPadding),
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
                        minSize = 180.dp,
                        contentPadding = PaddingValues(24.dp)
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