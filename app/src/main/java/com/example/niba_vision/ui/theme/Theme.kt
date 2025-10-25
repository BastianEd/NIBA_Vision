package com.example.niba_vision.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// 🌙 Aquí definimos la paleta de colores para el modo oscuro de la app.
// Usamos los tonos que definimos antes en el archivo Colors.kt.
private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

// ☀️ Y aquí está la paleta para el modo claro.
// Son los mismos tonos pero en versiones más brillantes.
private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40

    /* 👇 Si quisiéramos personalizar más colores (fondo, texto, superficie),
       podríamos hacerlo aquí. Se dejaron comentados para mantener el código limpio.
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

// 🎨 Esta función define el "tema visual" general de la app NIBA Vision.
// Es la que decide qué colores, tipografía y estilo se aplican a toda la interfaz.
@Composable
fun NIBA_VisionTheme(
    darkTheme: Boolean = isSystemInDarkTheme(), // 👉 Detecta si el sistema está en modo oscuro.
    dynamicColor: Boolean = true, // 👉 Usa colores dinámicos (solo en Android 12+).
    content: @Composable () -> Unit // 👉 Contenido de la pantalla (lo que se va a mostrar dentro del tema).
) {
    // 🎛️ Aquí decidimos qué esquema de color usar dependiendo de:
    // 1) Si el dispositivo permite colores dinámicos (Android 12 o superior).
    // 2) Si el sistema está en modo oscuro o claro.
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme)
                dynamicDarkColorScheme(context) // 👉 Tema oscuro con colores dinámicos del sistema
            else
                dynamicLightColorScheme(context) // 👉 Tema claro con colores dinámicos
        }

        darkTheme -> DarkColorScheme // 👉 Si no hay colores dinámicos, usamos nuestra paleta oscura.
        else -> LightColorScheme     // 👉 O la clara.
    }

    // 💡 Obtenemos la vista actual para poder cambiar detalles visuales del sistema,
    // como el color o brillo de los íconos en la barra de estado.
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            // ⚙️ Aquí ajustamos la barra de estado (hora, batería, señal, etc.)
            // para que combine con el tema actual (íconos blancos o negros).
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    // 🧩 Finalmente, aplicamos el tema a toda la app:
    // - colorScheme: define los colores (fondos, botones, textos, etc.)
    // - typography: define el estilo de las letras
    // - content: es el contenido visual (pantallas, botones, etc.)
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
