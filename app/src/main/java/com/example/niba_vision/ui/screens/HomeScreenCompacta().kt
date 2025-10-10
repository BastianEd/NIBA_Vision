package com.example.niba_vision.ui.screens
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.niba_vision.R
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.unit.Dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenCompacta() {
    // Implementación de la pantalla compacta
    // Puedes agregar aquí los componentes específicos para la versión compacta
    Scaffold(
        // Configuración del Scaffold para la pantalla compacta
        topBar = {
            // Barra superior para la versión compacta
            TopAppBar(title = { Text(text = "Mi App Kotlin") })
        }
    ) { innerPadding ->
        Column (
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ){
            Text(
                text = "Bienvenido!.",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.headlineMedium
            )
            Button(onClick = {}) {
                Text("Presióname")
            }
            /*
            *             Image(
                painter = painterResource(id = R.drawable.logo_niba_vision),
                contentDescription = "Logo de la App",
                modifier = Modifier
                    .fillMaxSize()
                    .height(150.dp),
                contentScale = ContentScale.Fit
            )
            * */
        }
    }
}