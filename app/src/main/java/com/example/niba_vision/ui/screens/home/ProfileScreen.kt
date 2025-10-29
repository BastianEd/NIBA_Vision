package com.example.niba_vision.ui.screens.home

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState // Para desplazamiento vertical
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll // Para desplazamiento vertical
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange // Icono de fecha
import androidx.compose.material.icons.filled.Edit // Icono de editar
import androidx.compose.material.icons.filled.Home // Icono de casa
import androidx.compose.material.icons.filled.Schedule // Icono de reloj
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector // Para los iconos
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.niba_vision.R // Para el 'drawable' de placeholder
import com.example.niba_vision.viewmodel.AppViewModelFactory
import com.example.niba_vision.viewmodel.ProfileViewModel

/**
 * Pantalla que muestra el perfil del usuario logueado.
 */
@Composable
fun ProfileScreen(
    viewModelFactory: AppViewModelFactory,
    onLogout: () -> Unit // Callback para cerrar sesión
) {
    // Obtiene la instancia del ProfileViewModel
    val viewModel: ProfileViewModel = viewModel(factory = viewModelFactory)
    // Observa el estado del perfil
    val uiState by viewModel.uiState.collectAsState()

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (uiState.isLoading) {
            // Muestra un indicador de carga mientras se obtiene el usuario desde SessionManager
            CircularProgressIndicator()
        } else if (uiState.currentUser == null) {
            // Muestra un error si no se pudo cargar el usuario
            Text("No se pudo cargar el perfil del usuario.")
        } else {
            // Si el usuario se cargó correctamente, se muestra el perfil
            val user = uiState.currentUser!!
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()) // Permite desplazar si el contenido es largo
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top // Alinea el contenido principal arriba
            ) {
                Spacer(modifier = Modifier.height(32.dp)) // Espacio superior

                // FOTO DE PERFIL
                Box(
                    modifier = Modifier
                        .size(150.dp)
                        .clip(CircleShape)
                        .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
                ) {
                    AsyncImage(
                        model = user.profilePictureUri, // URI de la foto
                        contentDescription = "Foto de perfil",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        // Imágenes de fallback en caso de error o carga
                        error = painterResource(id = R.drawable.ic_launcher_foreground),
                        placeholder = painterResource(id = R.drawable.ic_launcher_foreground)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // NOMBRE DE USUARIO
                Text(
                    text = user.fullName,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                // EMAIL
                Text(
                    text = user.email,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(32.dp))

                // --- CAMBIO: INFORMACIÓN DE DESPACHO ---
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(2.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text(
                            "Información de Despacho",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                        // Fila para la Dirección (Dato Real)
                        InfoRow(
                            icon = Icons.Default.Home,
                            label = "Dirección",
                            // Muestra la dirección guardada o "No especificada"
                            value = user.address ?: "No especificada"
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Fila para la Fecha (Dato Ficticio)
                        InfoRow(
                            icon = Icons.Default.DateRange,
                            label = "Próximo despacho",
                            value = "Viernes, 31 de Octubre" // Dato ficticio
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Fila para el Horario (Dato Ficticio)
                        InfoRow(
                            icon = Icons.Default.Schedule,
                            label = "Horario programado",
                            value = "14:00 - 18:00 hrs" // Dato ficticio
                        )
                    }
                }
                // ----------------------------------------

                // Spacer con 'weight' empuja el botón de logout al fondo de la pantalla
                Spacer(modifier = Modifier.weight(1f))
                Spacer(modifier = Modifier.height(32.dp)) // Espacio mínimo antes del botón

                // BOTÓN PARA CERRAR SESIÓN
                Button(
                    onClick = onLogout,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("CERRAR SESIÓN")
                }
            }
        }
    }
}

/**
 * Composable de ayuda reutilizable para mostrar una fila de información
 * (Icono, Etiqueta, Valor) dentro del perfil.
 */
@Composable
private fun InfoRow(icon: ImageVector, label: String, value: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        // 1. Icono
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))

        // 2. Columna de texto (Etiqueta y Valor)
        Column {
            Text(label, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(value, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium)
        }

        // 3. Spacer flexible que empuja el icono de editar al final
        Spacer(modifier = Modifier.weight(1f))

        // 4. Icono de "editar" (decorativo/ficticio)
        IconButton(onClick = { /* Acción de editar (ficticia) */ }) {
            Icon(Icons.Default.Edit, contentDescription = "Editar", tint = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}