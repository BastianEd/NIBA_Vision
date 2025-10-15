package com.example.niba_vision.ui.screens

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import com.example.niba_vision.ui.utils.obtenerWindowSizeClass

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
fun HomeAdaptiveScreen() {
    // Obtiene la clase de tamaño de la ventana actual.
    val windowSizeClass = obtenerWindowSizeClass()

    // Elige el diseño a mostrar basado en la clase de ancho.
    when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> HomeScreenCompacta()
        WindowWidthSizeClass.Medium -> HomeScreenMediana()
        WindowWidthSizeClass.Expanded -> HomeScreenExpandida()
        else -> HomeScreenCompacta() // Diseño por defecto en caso de un tamaño no esperado.
    }
}