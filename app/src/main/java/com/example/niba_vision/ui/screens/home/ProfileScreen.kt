package com.example.niba_vision.ui.screens.home

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable // 👈 Importante para detectar el toque
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Schedule
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.niba_vision.R
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

    // Obtenemos el contexto actual (necesario para procesar la imagen)
    val context = LocalContext.current

    // 👇 Configuración del lanzador de Galería (Photo Picker)
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            // Si el usuario eligió una foto (uri no es null)
            if (uri != null) {
                // Aquí llamamos a la función del ViewModel que estaba "sin usar"
                viewModel.changeProfilePicture(uri, context)
            }
        }
    )

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (uiState.isLoading) {
            CircularProgressIndicator()
        } else if (uiState.currentUser == null) {
            Text("No se pudo cargar el perfil del usuario.")
        } else {
            val user = uiState.currentUser!!
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Spacer(modifier = Modifier.height(32.dp))

                // --- FOTO DE PERFIL ---
                Box(
                    modifier = Modifier
                        .size(150.dp)
                        .clip(CircleShape)
                        .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
                        // 👇 AQUÍ ESTÁ LA CLAVE: Hacemos que al tocar la caja, se abra la galería
                        .clickable {
                            photoPickerLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        }
                ) {
                    AsyncImage(
                        model = user.profilePictureUri, // URI de la foto
                        contentDescription = "Foto de perfil",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        error = painterResource(id = R.drawable.ic_launcher_foreground),
                        placeholder = painterResource(id = R.drawable.ic_launcher_foreground)
                    )

                    // Icono superpuesto para indicar que es editable
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Cambiar foto",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(12.dp)
                            .size(24.dp)
                    )
                }

                Text(
                    text = "Toca la foto para cambiarla",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(top = 8.dp)
                )

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

                // --- INFORMACIÓN DE DESPACHO ---
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

                        InfoRow(
                            icon = Icons.Default.Home,
                            label = "Dirección",
                            value = user.address ?: "No especificada"
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        InfoRow(
                            icon = Icons.Default.DateRange,
                            label = "Próximo despacho",
                            value = "Viernes, 31 de Octubre"
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        InfoRow(
                            icon = Icons.Default.Schedule,
                            label = "Horario programado",
                            value = "14:00 - 18:00 hrs"
                        )
                    }
                }

                Spacer(modifier = Modifier.weight(1f))
                Spacer(modifier = Modifier.height(32.dp))

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

@Composable
private fun InfoRow(icon: ImageVector, label: String, value: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))

        Column {
            Text(label, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(value, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium)
        }

        Spacer(modifier = Modifier.weight(1f))

        IconButton(onClick = { /* Acción decorativa */ }) {
            Icon(Icons.Default.Edit, contentDescription = "Editar", tint = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}