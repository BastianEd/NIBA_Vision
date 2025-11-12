package com.example.niba_vision.ui.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage // Para cargar imágenes desde URL
import com.example.niba_vision.R // Para acceder a los 'drawables' (placeholder)
import com.example.niba_vision.data.CartItem
import com.example.niba_vision.viewmodel.HomeViewModel
import java.text.NumberFormat // Para formatear moneda

/*
 * Pantalla que muestra los items del carrito y gestiona el pago.
 * Recibe el HomeViewModel para acceder al estado del carrito (items) y
 * los eventos de pago.
*/
@Composable
fun CartScreen(viewModel: HomeViewModel) {
    // 📡 Observa el estado (HomeUiState) desde el ViewModel.
    // Compose se redibujará automáticamente cuando 'uiState' cambie.
    val uiState by viewModel.uiState.collectAsState()

    // Convierte el mapa de items (Map<Int, CartItem>) a una lista (List<CartItem>)
    // para que 'LazyColumn' pueda usarla.
    val cartItems = uiState.cartItems.values.toList()

    // Se crea un formateador de moneda para pesos chilenos (CLP)
    // Locale("es", "CL") especifica español de Chile.
    val currencyFormat = NumberFormat.getCurrencyInstance()
    currencyFormat.maximumFractionDigits = 0 // No se muestran decimales (ej: $22.900)

    // Se calcula el precio total sumando (precio * cantidad) de cada item en el carrito.
    val total = cartItems.sumOf { (it.book.priceValue ?: 0.0) * it.quantity }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center // Centra el texto "carrito vacío"
    ) {
        // Comprueba si el carrito está vacío
        if (cartItems.isEmpty()) {
            Text(text = "Tu cesta de compra está vacía")
        } else {
            // Si hay items, muestra la lista, el total y el botón de pago
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Mi Cesta",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Lista perezosa (LazyColumn) que solo renderiza los items visibles
                LazyColumn(
                    modifier = Modifier.weight(1f), // Ocupatodo el espacio vertical disponible
                    verticalArrangement = Arrangement.spacedBy(12.dp), // Espacio entre cada item
                    contentPadding = PaddingValues(bottom = 16.dp) // Padding al final de la lista
                ) {
                    // Itera sobre la lista de 'cartItems'
                    items(cartItems) { item ->
                        // Llama al Composable que dibuja cada fila del carrito
                        CartItemRow(item = item, currencyFormat = currencyFormat)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Muestra el total calculado
                Text(
                    text = "Total: ${currencyFormat.format(total)}",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Botón Pagar
                Button(
                    onClick = { viewModel.onPaymentClicked() }, // Llama al evento del ViewModel
                    modifier = Modifier
                        .fillMaxWidth() // Ocupatodo el ancho
                        .height(50.dp) // Altura fija
                ) {
                    Text("PAGAR", fontSize = 18.sp)
                }
            }
        }

        // --- POP-UP DE PAGO EXITOSO (AlertDialog) ---

        // Se muestra solo si 'showPaymentSuccessDialog' es verdadero en el UiState
        if (uiState.showPaymentSuccessDialog) {
            AlertDialog(
                // Se llama cuando el usuario toca fuera del diálogo o el botón "Aceptar"
                onDismissRequest = { viewModel.dismissPaymentDialog() },
                title = { Text("¡Éxito!") },
                text = { Text("Compra realizada con éxito.") },
                confirmButton = {
                    TextButton(onClick = { viewModel.dismissPaymentDialog() }) {
                        Text("Aceptar")
                    }
                }
            )
        }
    }
}

/**
 * Composable interno (privado a este archivo) que define cómo se ve
 * CADA fila individual en la lista del carrito.
 */
@Composable
fun CartItemRow(item: CartItem, currencyFormat: NumberFormat) {
    // Calcula el total para esta fila específica (Precio del libro * cantidad)
    val itemTotal = (item.book.priceValue ?: 0.0) * item.quantity

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically // Centra los elementos verticalmente
        ) {
            // 1. Imagen del libro
            AsyncImage(
                model = item.book.coverImageUrl,
                contentDescription = item.book.title,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp)), // Bordes redondeados
                contentScale = ContentScale.Crop,
                placeholder = painterResource(id = R.drawable.ic_launcher_foreground)
            )

            Spacer(modifier = Modifier.width(16.dp))

            // 2. Columna de Información (Nombre, Cantidad, Precio Unitario)
            Column(modifier = Modifier.weight(1f)) { // 'weight(1f)' hace que ocupe el espacio sobrante
                Text(item.book.title ?: "Sin título", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(
                    "Cantidad: ${item.quantity}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    item.book.price ?: "N/A", // Precio unitario (ej: "$22.900")
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            // 3. Valor Total del Item (Precio * Cantidad)
            Text(
                text = currencyFormat.format(itemTotal), // Total formateado (ej: "$45.800")
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
            )
        }
    }
}