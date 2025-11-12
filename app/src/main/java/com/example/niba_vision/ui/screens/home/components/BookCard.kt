package com.example.niba_vision.ui.screens.home.components

// --- Imports para Animación ---
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
// ------------------------------
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember // Para 'remember'
import androidx.compose.runtime.rememberCoroutineScope // Para lanzar animaciones
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate // Para la animación de vibración
import androidx.compose.ui.draw.scale // Para la animación de salto
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage // Para cargar imágenes desde URL
import com.example.niba_vision.data.Book
import kotlinx.coroutines.launch // Para lanzar la corutina
import com.example.niba_vision.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookCard(
    book: Book,
    onAddToCart: () -> Unit,
    modifier: Modifier = Modifier
) {
    // --- Lógica de Animación ---

    // Se crea un CoroutineScope que este Composable puede usar para lanzar animaciones
    val scope = rememberCoroutineScope()

    // Se crea un estado 'Animatable' para la escala (salto) de la tarjeta.
    // 'remember' asegura que el estado persista entre recomposiciones.
    val scale = remember { Animatable(1f) } // 1f = 100% (tamaño normal)

    // Se crea un estado 'Animatable' para la rotación (vibración) del icono del carrito.
    val iconRotation = remember { Animatable(0f) } // 0f = 0 grados (sin rotación)
    // ---------------------------

    Card(
        // Se aplica la animación de escala al modificador de la tarjeta
        modifier = modifier
            .width(180.dp)
            .scale(scale.value), // El valor 'scale.value' cambiará durante la animación
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .height(200.dp)
                    .fillMaxWidth()
            ) {
                // Se usa AsyncImage de Coil para cargar la imagen desde la URL
                AsyncImage(
                    model = book.coverImageUrl, // La URL de la portada del libro
                    contentDescription = book.title ?: "Portada", // (Usamos el título)
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,

                    // --- INICIO DE CAMBIOS ---

                    // 1. Cambia 'drawable' a 'mipmap' para usar tu nuevo logo
                    placeholder = painterResource(id = R.mipmap.ic_launcher_foreground),

                    // 2. AÑADE ESTA LÍNEA:
                    // Esto mostrará el logo si Coil falla (ej: 404, sin internet)
                    error = painterResource(id = R.mipmap.ic_launcher_foreground)

                    // --- FIN DE CAMBIOS ---
                )

                // Muestra la insignia "N" si el libro es nuevo
                if (book.isNew) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd) // Alinea la insignia arriba a la derecha
                            .padding(8.dp)
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF4CAF50)), // Color verde
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "N",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Información del libro
            Text(
                text = book.title ?: "Sin título",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis // Pone "..." si el texto es muy largo
            )
            Text(
                text = book.author ?: "Sin autor",
                style = MaterialTheme.typography.bodySmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = book.price ?: "Precio no disponible",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            // Fila de botones
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Botón "AGREGAR"
                OutlinedButton(
                    onClick = {
                        // Al hacer clic, se lanza una corutina para ejecutar las animaciones
                        scope.launch {
                            // 1. Animación de "salto" de la tarjeta
                            scale.animateTo(1.05f, tween(100)) // Crece a 105%
                            scale.animateTo(1f, tween(100))   // Vuelve a 100%

                            // 2. Animación de "vibración" del icono
                            iconRotation.animateTo(15f, tween(100))  // Gira a 15 grados
                            iconRotation.animateTo(-15f, tween(100)) // Gira a -15 grados
                            iconRotation.animateTo(0f, tween(100))   // Vuelve a 0 grados

                            // 3. Llama a la lógica del ViewModel (acción principal)
                            // Esto se hace DESPUÉS de que las animaciones se disparen
                            onAddToCart()
                        }
                    },
                    modifier = Modifier.weight(1f), // Ocupa el espacio disponible
                    contentPadding = PaddingValues(horizontal = 8.dp),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                ) {
                    Icon(
                        Icons.Outlined.ShoppingCart,
                        contentDescription = "Agregar",
                        modifier = Modifier
                            .size(18.dp)
                            .rotate(iconRotation.value) // Aplica el valor de rotación animado
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("AGREGAR", fontSize = 12.sp)
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Botón de Favoritos (sin acción por ahora)
                IconButton(onClick = { /* TODO: Lógica de favoritos */ }) {
                    Icon(
                        Icons.Outlined.FavoriteBorder,
                        contentDescription = "Favorito"
                    )
                }
            }
        }
    }
}