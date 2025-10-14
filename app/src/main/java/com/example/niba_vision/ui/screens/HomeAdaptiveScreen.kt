package com.example.niba_vision.ui.screens

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import com.example.niba_vision.ui.utils.obtenerWindowSizeClass

/**
 * Pantalla principal que se encarga de determinar qué vista de Home mostrar
 * según el ancho de la ventana del dispositivo. (Parte 3, Paso 4 de la guía)
 */
@Composable
fun HomeAdaptiveScreen() {
    val windowSizeClass = obtenerWindowSizeClass()

    // Se verifica la clase de ancho de la ventana (WindowWidthSizeClass)
    when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> HomeScreenCompacta()
        WindowWidthSizeClass.Medium -> HomeScreenMediana()
        WindowWidthSizeClass.Expanded -> HomeScreenExpandida()
        else -> HomeScreenCompacta() // Valor por defecto
    }
}