package com.example.niba_vision.ui.screens.home

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import com.example.niba_vision.ui.utils.obtenerWindowSizeClass
import com.example.niba_vision.viewmodel.AppViewModelFactory

/**
 * Pantalla principal que se adapta al tamaño del dispositivo.
 *
 * Utiliza `WindowSizeClass` para determinar el ancho de la pantalla y elige
 * dinámicamente cuál de las tres implementaciones de la pantalla de inicio mostrar:
 * - `HomeScreenCompacta` para teléfonos.
 * - `HomeScreenMediana` para tablets pequeñas.
 * - `HomeScreenExpandida` para tablets grandes o modo escritorio.
 */
@Composable
fun HomeAdaptiveScreen(viewModelFactory: AppViewModelFactory) { // Recibe la fábrica
    // Obtiene la clase de tamaño de la ventana actual.
    val windowSizeClass = obtenerWindowSizeClass()

    // Elige el diseño a mostrar basado en la clase de ancho,
    // pasando la fábrica a cada pantalla.
    when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> HomeScreenCompacta(viewModelFactory = viewModelFactory)
        WindowWidthSizeClass.Medium -> HomeScreenMediana(viewModelFactory = viewModelFactory)
        WindowWidthSizeClass.Expanded -> HomeScreenExpandida(viewModelFactory = viewModelFactory)
        else -> HomeScreenCompacta(viewModelFactory = viewModelFactory) // Diseño por defecto
    }
}