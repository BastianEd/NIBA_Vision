package com.example.niba_vision.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// ✍️ Aquí definimos la tipografía base de la app NIBA Vision.
// Esto controla cómo se ven los textos: tamaño, grosor, espacio entre líneas, etc.

// 📚 Typography es una clase que agrupa todos los estilos de texto del sistema Material Design 3.
// Podemos personalizar títulos, subtítulos, párrafos, botones, etc.
val Typography = Typography(
    // 🧾 "bodyLarge" es el estilo de texto principal o base del contenido.
    // Se usa en la mayoría de los textos comunes, como párrafos o descripciones.
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,    // 👉 Aquí usamos la fuente predeterminada del sistema.
        fontWeight = FontWeight.Normal,     // 👉 Peso normal (ni negrita ni delgada).
        fontSize = 16.sp,                   // 👉 Tamaño del texto: 16 "scale pixels", ideal para leer cómodo.
        lineHeight = 24.sp,                 // 👉 Altura de línea: deja espacio vertical entre líneas de texto.
        letterSpacing = 0.5.sp              // 👉 Espaciado entre letras, mejora la legibilidad.
    )
)
