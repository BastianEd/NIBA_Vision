package com.example.niba_vision.ui.utils

import android.app.Activity
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

/**
 * Composable de utilidad para obtener la [WindowSizeClass] actual.
 *
 * Esta función reutilizable permite a cualquier Composable determinar la clase de tamaño
 * de la ventana (Compact, Medium, Expanded) en la que se está ejecutando, facilitando
 * la creación de interfaces de usuario adaptables a diferentes dimensiones de pantalla.
 *
 * @throws IllegalStateException si el contexto local no puede ser convertido a una Activity.
 * @return El objeto [WindowSizeClass] que describe el tamaño de la ventana actual.
 */
@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun obtenerWindowSizeClass(): WindowSizeClass {
    // Obtiene la actividad actual desde el contexto local de manera segura.
    val context = LocalContext.current
    val activity = context as? Activity
        ?: throw IllegalStateException("LocalContext no puede convertirse a Activity. Asegúrate de que este Composable se ejecute en un contexto de Activity.")

    // Calcula y retorna la clase de tamaño de la ventana para esa actividad.
    return calculateWindowSizeClass(activity)
}