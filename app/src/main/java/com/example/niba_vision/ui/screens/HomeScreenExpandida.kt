package com.example.niba_vision.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import com.example.niba_vision.view.theme.NIBA_VisionTheme

/**
 * Representa la pantalla de inicio para dispositivos con ancho expandido (ej. tablets grandes).
 *
 * Utiliza un diseño de tipo "Master-Detail" con un panel de navegación lateral fijo
 * y un área de contenido principal, ideal para pantallas anchas.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenExpandida() {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = "ZONALIBROS - Panel de Control") })
        }
    ) { innerPadding ->
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Panel lateral (Master Pane)
            Column(
                modifier = Modifier
                    .width(250.dp)
                    .fillMaxHeight()
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_foreground),
                    contentDescription = "Logo App",
                    modifier = Modifier.size(100.dp).padding(bottom = 16.dp)
                )
                Text("Menú Lateral", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(8.dp))
                Button(onClick = {}) { Text("Opción 1") }
                Spacer(Modifier.height(8.dp))
                Button(onClick = {}) { Text("Opción 2") }
            }

            // Contenido principal (Detail Pane)
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Modo Expandido (Tablet Grande/Escritorio)",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier.padding(bottom = 24.dp)
                )
                Text(
                    text = "Esta vista utiliza un diseño de panel lateral (Master/Detail) para maximizar el uso de grandes pantallas.",
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

/**
 * Previsualización para el diseño expandido en Android Studio.
 */
@Preview(name = "Expanded (1000dp)", widthDp = 1000, heightDp = 900)
@Composable
fun PreviewExpandida() {
    NIBA_VisionTheme {
        HomeScreenExpandida()
    }
}