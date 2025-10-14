package com.example.niba_vision.ui.screens

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
import com.example.niba_vision.view.theme.NIBA_VisionTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenMediana() {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = "ZONALIBROS") })
        }
    ) { innerPadding ->
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = "Logo App",
                modifier = Modifier.size(200.dp)
            )
            Column(
                modifier = Modifier.widthIn(max = 300.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "Modo Mediano (Tablet Pequeña)",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                Text(
                    text = "En pantallas medianas, el diseño empieza a aprovechar el espacio horizontal para presentar la imagen y el texto lado a lado.",
                    textAlign = TextAlign.Start
                )
            }
        }
    }
}

@Preview(name = "Medium (600dp)", widthDp = 600, heightDp = 900)
@Composable
fun PreviewMediana() {
    NIBA_VisionTheme {
        HomeScreenMediana()
    }
}