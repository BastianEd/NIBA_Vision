package com.example.niba_vision.ui.screens.home.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartIconWithBadge(count: Int) {
    IconButton(onClick = { /* TODO: Navegar a la pantalla de la cesta */ }) {
        // BadgedBox apila la insignia encima del icono
        BadgedBox(
            badge = {
                // Solo muestra la insignia si hay artículos
                if (count > 0) {
                    Badge {
                        Text(text = "$count")
                    }
                }
            }
        ) {
            Icon(
                imageVector = Icons.Default.ShoppingCart,
                contentDescription = "Cesta de compra"
            )
        }
    }
}