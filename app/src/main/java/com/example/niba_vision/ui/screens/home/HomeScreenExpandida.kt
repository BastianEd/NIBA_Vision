package com.example.niba_vision.ui.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.niba_vision.data.AppDatabase
import com.example.niba_vision.data.BookRepository
import com.example.niba_vision.data.CartRepository
import com.example.niba_vision.data.UserRepository
import com.example.niba_vision.ui.theme.NIBA_VisionTheme
import com.example.niba_vision.viewmodel.AppViewModelFactory
import com.example.niba_vision.viewmodel.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenExpandida(viewModelFactory: AppViewModelFactory) {
    val viewModel: HomeViewModel = viewModel(factory = viewModelFactory)
    val uiState by viewModel.uiState.collectAsState()

    val navItems = listOf(HomeRoute.Home, HomeRoute.Cart, HomeRoute.Profile)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "ZONALIBROS - Panel de Control") }
            )
        }
    ) { innerPadding ->
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // *** CAMBIO AQUÍ ***
            // Usamos un NavigationRail (barra lateral) en lugar de NavigationBar
            NavigationRail {
                navItems.forEach { screen ->
                    NavigationRailItem(
                        icon = { Icon(screen.icon, contentDescription = screen.title) },
                        label = { Text(screen.title) },
                        selected = (screen == uiState.selectedScreen),
                        onClick = { viewModel.onHomeNavigationSelected(screen) }
                    )
                }
            }

            // Contenido principal (Detail Pane)
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
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
                            contentPadding = PaddingValues(32.dp)
                        )
                        HomeRoute.Cart -> CartScreen()
                        HomeRoute.Profile -> ProfileScreen()
                    }
                }
            }
        }
    }
}
