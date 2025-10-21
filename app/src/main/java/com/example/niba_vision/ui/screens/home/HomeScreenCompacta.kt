package com.example.niba_vision.ui.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.niba_vision.R
import com.example.niba_vision.ui.theme.NIBA_VisionTheme

/**
 * Representa la pantalla de inicio para dispositivos con ancho compacto (ej. teléfonos).
 *
 * El diseño es vertical, apilando los elementos uno encima del otro para
 * un mejor aprovechamiento del espacio en pantallas estrechas.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenCompacta() {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = "ZONALIBROS") })
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Modo Compacto (Móvil)",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = "Logo App",
                modifier = Modifier
                    .size(150.dp)
                    .padding(bottom = 24.dp)
            )
            Text(
                text = "Vista de inicio optimizada para pantallas pequeñas. El contenido está apilado verticalmente.",
                textAlign = TextAlign.Center
            )
        }
    }
}

/**
 * Previsualización para el diseño compacto en Android Studio.
 */
@Preview(name = "Compact (360dp)", widthDp = 360, heightDp = 800)
@Composable
fun PreviewCompact() {
    NIBA_VisionTheme {
        HomeScreenCompacta()
    }
}