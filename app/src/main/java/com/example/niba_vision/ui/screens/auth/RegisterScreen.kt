package com.example.niba_vision.ui.screens.auth

import android.Manifest // Para el permiso de la cámara
import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider // Para crear la URI de la cámara
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage // Para cargar la imagen de perfil
import com.example.niba_vision.data.Genre
import com.example.niba_vision.viewmodel.RegisterViewModel
import androidx.compose.animation.*
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

// Constante para la duración de la animación de entrada
const val ANIMATION_DURATION_MS_REGISTER = 600

/**
 * Composable que representa la pantalla de Registro de usuarios.
 * Recolecta todos los datos del nuevo usuario, incluyendo la foto de perfil y la dirección.
 */
@Composable
fun RegisterScreen(
    onBack: () -> Unit,                 // Callback para navegar hacia atrás (a Login)
    onRegistered: () -> Unit,           // Callback para navegar a Login después de un registro exitoso
    registerViewModel: RegisterViewModel = viewModel() // Inyección del ViewModel
) {
    // 📡 Observamos el estado de la pantalla (UiState) desde el ViewModel
    val uiState by registerViewModel.uiState.collectAsState()
    // Se obtiene la lista de géneros disponibles desde el Enum
    val genreOptions = Genre.entries
    // Se obtiene el contexto actual, necesario para la cámara y el FileProvider
    val context = LocalContext.current

    // Estado de transición para controlar la animación de entrada
    val transitionState = remember { MutableTransitionState(false) }

    // Efecto que se ejecuta una sola vez al cargar el Composable
    LaunchedEffect(Unit) {
        transitionState.targetState = true // Inicia la animación de entrada
    }

    // Efecto que observa si el registro fue exitoso
    LaunchedEffect(uiState.isRegistrationSuccess) {
        if (uiState.isRegistrationSuccess) {
            onRegistered() // Navega a la siguiente pantalla
        }
    }

    // --- Lógica y Launchers para la Cámara ---

    // 1. Crea un archivo temporal en el caché para guardar la foto
    val file = context.createImageFile()
    // 2. Obtiene una URI segura para ese archivo usando un FileProvider
    val uri = FileProvider.getUriForFile(
        context,
        context.packageName + ".provider", // La autoridad debe coincidir con AndroidManifest.xml
        file
    )

    // 3. Launcher que abre la cámara y espera el resultado (un booleano 'success')
    val cameraLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) {
                // Si la foto se tomó con éxito, se actualiza el ViewModel con la URI
                registerViewModel.onProfilePictureChange(uri)
            }
        }

    // 4. Launcher que solicita el permiso para usar la cámara
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            // Si el usuario concede el permiso, se abre la cámara
            cameraLauncher.launch(uri)
        }
        // (Opcional: mostrar un mensaje si el permiso es denegado)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // 💫 Contenedor de animación: aplica una animación de entrada al formulario
        AnimatedVisibility(
            visibleState = transitionState,
            enter = slideInVertically(
                initialOffsetY = { it * 2 }, // Desliza desde abajo
                animationSpec = tween(ANIMATION_DURATION_MS_REGISTER)
            ) + fadeIn(tween(ANIMATION_DURATION_MS_REGISTER * 3 / 4, delayMillis = ANIMATION_DURATION_MS_REGISTER / 4)), // Aparece gradualmente
            exit = fadeOut(tween(300))
        ) {
            // 🧱 Columna principal que contienetodo el formulario
            Column(
                Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()) // Permite desplazarse si el contenido no cabe
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp) // Espacio uniforme entre elementos
            ) {

                Text("Registro", style = MaterialTheme.typography.headlineMedium)

                // 📸 --- Sección para la Foto de Perfil ---
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape) // Forma circular
                        .clickable {
                            // Al hacer clic, se pide el permiso para la cámara
                            permissionLauncher.launch(Manifest.permission.CAMERA)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    if (uiState.profilePictureUri != null) {
                        // Si ya hay una foto, se muestra usando Coil (AsyncImage)
                        AsyncImage(
                            model = uiState.profilePictureUri,
                            contentDescription = "Foto de perfil",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop // Rellena el círculo
                        )
                    } else {
                        // Si no, se muestra un ícono placeholder de cámara
                        Icon(
                            painter = painterResource(id = android.R.drawable.ic_menu_camera),
                            contentDescription = "Tomar foto",
                            modifier = Modifier.size(60.dp),
                            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                        )
                    }
                }
                Text("Toca el círculo para añadir una foto", style = MaterialTheme.typography.bodySmall)

                // 👤 Campo: Nombre completo
                OutlinedTextField(
                    value = uiState.fullName,
                    onValueChange = { registerViewModel.onFullNameChange(it) },
                    label = { Text("Nombre completo") },
                    isError = uiState.nameError != null,
                    modifier = Modifier.fillMaxWidth()
                )
                if (uiState.nameError != null)
                    Text(uiState.nameError!!, color = MaterialTheme.colorScheme.error, modifier = Modifier.fillMaxWidth())

                // 📧 Campo: Correo
                OutlinedTextField(
                    value = uiState.email,
                    onValueChange = { registerViewModel.onEmailChange(it) },
                    label = { Text("Correo @duoc.cl") },
                    isError = uiState.emailError != null,
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                if (uiState.emailError != null)
                    Text(uiState.emailError!!, color = MaterialTheme.colorScheme.error, modifier = Modifier.fillMaxWidth())

                // 🔒 Campo: Contraseña
                OutlinedTextField(
                    value = uiState.pass,
                    onValueChange = { registerViewModel.onPasswordChange(it) },
                    label = { Text("Contraseña") },
                    visualTransformation = PasswordVisualTransformation(), // Oculta los caracteres
                    isError = uiState.passError != null,
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                if (uiState.passError != null)
                    Text(uiState.passError!!, color = MaterialTheme.colorScheme.error, modifier = Modifier.fillMaxWidth())

                // ✅ Campo: Confirmar contraseña
                OutlinedTextField(
                    value = uiState.confirmPass,
                    onValueChange = { registerViewModel.onConfirmPasswordChange(it) },
                    label = { Text("Confirmar contraseña") },
                    visualTransformation = PasswordVisualTransformation(),
                    isError = uiState.confirmPassError != null,
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                if (uiState.confirmPassError != null)
                    Text(uiState.confirmPassError!!, color = MaterialTheme.colorScheme.error, modifier = Modifier.fillMaxWidth())

                // ☎️ Campo: Teléfono
                OutlinedTextField(
                    value = uiState.phone,
                    onValueChange = { registerViewModel.onPhoneChange(it) },
                    label = { Text("Teléfono (opcional, 8 dígitos)") },
                    isError = uiState.phoneError != null,
                    singleLine = true,
                    leadingIcon = { Text("+569") }, // Prefijo visual
                    modifier = Modifier.fillMaxWidth()
                )
                if (uiState.phoneError != null)
                    Text(uiState.phoneError!!, color = MaterialTheme.colorScheme.error, modifier = Modifier.fillMaxWidth())

                // 🏠 --- CAMBIO: Campo Dirección ---
                OutlinedTextField(
                    value = uiState.address,
                    onValueChange = { registerViewModel.onAddressChange(it) },
                    label = { Text("Dirección de despacho") },
                    isError = uiState.addressError != null, // Muestra error si está vacío
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                if (uiState.addressError != null)
                    Text(uiState.addressError!!, color = MaterialTheme.colorScheme.error, modifier = Modifier.fillMaxWidth())
                // ---------------------------------

                // 🎶 Sección: Géneros favoritos
                Text(
                    "Géneros favoritos (selecciona al menos uno):",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.fillMaxWidth()
                )

                Column(Modifier.fillMaxWidth()) {
                    // Itera sobre todos los géneros y crea un Checkbox para cada uno
                    genreOptions.forEachIndexed { index, genre ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Checkbox(
                                checked = uiState.checkedGenres[index], // Estado de marcado
                                onCheckedChange = { isChecked ->
                                    registerViewModel.onGenreCheckedChange(index, isChecked) // Notifica al ViewModel
                                }
                            )
                            Text(genre.name.replace("_", " ")) // Reemplaza "NO_FICCION" por "NO FICCION"
                        }
                    }
                }
                if (uiState.genresError != null)
                    Text(uiState.genresError!!, color = MaterialTheme.colorScheme.error, modifier = Modifier.fillMaxWidth())

                // ⚠️ Muestra un error general si el envío falla (ej: email duplicado)
                if (uiState.submitError != null)
                    Text(uiState.submitError!!, color = MaterialTheme.colorScheme.error)

                // 🚀 Botón de “Crear cuenta”
                Button(
                    onClick = { registerViewModel.register() },
                    // Se habilita solo si 'allValid' (en el UiState) es verdadero
                    enabled = uiState.allValid,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = Color.White,
                        disabledContainerColor = Color.Gray, // Color cuando está deshabilitado
                        disabledContentColor = Color.White
                    )
                ) {
                    Text("Crear cuenta")
                }

                // 🔙 Botón “Volver” para regresar al login
                TextButton(
                    onClick = onBack,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = Color.White,
                    )
                ) {
                    Text("Volver")
                }
            }
        }
    }
}

/**
 * Función de utilidad (mEtodo de extensión de Context) para crear un archivo de imagen temporal.
 */
private fun Context.createImageFile(): File {
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
    val imageFileName = "JPEG_" + timeStamp + "_"
    // Se usa 'cacheDir' (almacenamiento interno en caché)
    return File.createTempFile(
        imageFileName,
        ".jpg",
        cacheDir
    )
}