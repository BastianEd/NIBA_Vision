package com.example.niba_vision.ui.screens.auth

import android.Manifest
import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
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
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.niba_vision.data.Genre
import com.example.niba_vision.viewmodel.RegisterViewModel
import androidx.compose.animation.*
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

// Usamos la misma constante de duración para la consistencia
const val ANIMATION_DURATION_MS_REGISTER = 600

// 🧾 Pantalla de registro de nuevos usuarios.
// Aquí el usuario ingresa su nombre, correo, contraseña, teléfono y géneros favoritos.
@Composable
fun RegisterScreen(
    onBack: () -> Unit,                 // 👉 Acción para volver al login.
    onRegistered: () -> Unit,           // 👉 Acción que se ejecuta cuando el registro fue exitoso.
    registerViewModel: RegisterViewModel = viewModel() // 👉 ViewModel que maneja los datos y validaciones.
) {
    // 📡 Observamos el estado de la pantalla (nombre, correo, errores, etc.)
    val uiState by registerViewModel.uiState.collectAsState()
    // 🎵 Lista de géneros (tomada del enum Genre).
    val genreOptions = Genre.entries
    // 📍 Obtenemos el contexto actual, necesario para la cámara y el FileProvider.
    val context = LocalContext.current

    // 💡 Estado de transición para controlar la animación de entrada, se inicializa en 'false'
    val transitionState = remember { MutableTransitionState(false) }

    // 🚀 Al cargar la pantalla, iniciamos la animación del formulario (solo una vez)
    LaunchedEffect(Unit) { transitionState.targetState = true }

    // 🚀 Si el registro fue exitoso, navegamos automáticamente de vuelta al login.
    LaunchedEffect(uiState.isRegistrationSuccess) {
        if (uiState.isRegistrationSuccess) {
            onRegistered()
        }
    }

    // 📸 --- Lógica y Launchers para la Cámara ---
    // Creamos un archivo temporal para guardar la foto
    // NOTE: usamos el cache interno (context.cacheDir) para que coincida con res/xml/file_paths.xml
    val file = context.createImageFile()
    // Obtenemos una URI segura para ese archivo usando un FileProvider
    val uri = FileProvider.getUriForFile(
        context,
        context.packageName + ".provider",
        file
    )

    // Launcher que abre la cámara y espera el resultado (una foto)
    val cameraLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) {
                // Si la foto se tomó con éxito, actualizamos el ViewModel
                registerViewModel.onProfilePictureChange(uri)
            }
        }

    // Launcher que solicita el permiso para usar la cámara
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            // Si el usuario concede el permiso, abrimos la cámara
            cameraLauncher.launch(uri)
        }
        // Opcional: podrías mostrar un mensaje si el permiso es denegado
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // 💫 Aplicamos la animación de entrada más "vívida"
        AnimatedVisibility(
            visibleState = transitionState,
            // Animación de entrada: desliza desde el fondo y se desvanece
            enter = slideInVertically(
                initialOffsetY = { it * 2 },
                animationSpec = tween(ANIMATION_DURATION_MS_REGISTER)
            ) + fadeIn(tween(ANIMATION_DURATION_MS_REGISTER * 3 / 4, delayMillis = ANIMATION_DURATION_MS_REGISTER / 4)),
            exit = fadeOut(tween(300))
        ) {
            // 🧱 Columna principal que contiene todo el formulario.
            Column(
                Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()) // 👉 Permite desplazarse si hay muchos campos.
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally, // Centramos los elementos
                verticalArrangement = Arrangement.spacedBy(12.dp) // 👉 Espacio uniforme entre elementos.
            ) {
                // 🏷️ Título de la pantalla.
                Text("Registro", style = MaterialTheme.typography.headlineMedium)

                // 📸 --- Sección para la Foto de Perfil ---
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .clickable {
                            // Al hacer clic, pedimos el permiso para la cámara
                            permissionLauncher.launch(Manifest.permission.CAMERA)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    if (uiState.profilePictureUri != null) {
                        // Si ya hay una foto, la mostramos
                        AsyncImage(
                            model = uiState.profilePictureUri,
                            contentDescription = "Foto de perfil",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        // Si no, mostramos un ícono placeholder
                        Icon(
                            painter = painterResource(id = android.R.drawable.ic_menu_camera),
                            contentDescription = "Tomar foto",
                            modifier = Modifier.size(60.dp),
                            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                        )
                    }
                }
                Text("Toca el círculo para añadir una foto", style = MaterialTheme.typography.bodySmall)

                // 👤 Campo: Nombre completo del usuario.
                OutlinedTextField(
                    value = uiState.fullName,
                    onValueChange = { registerViewModel.onFullNameChange(it) },
                    label = { Text("Nombre completo") },
                    isError = uiState.nameError != null,
                    modifier = Modifier.fillMaxWidth()
                )
                if (uiState.nameError != null)
                    Text(uiState.nameError!!, color = MaterialTheme.colorScheme.error, modifier = Modifier.fillMaxWidth())

                // 📧 Campo: Correo institucional.
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

                // 🔒 Campo: Contraseña (oculta con puntitos).
                OutlinedTextField(
                    value = uiState.pass,
                    onValueChange = { registerViewModel.onPasswordChange(it) },
                    label = { Text("Contraseña") },
                    visualTransformation = PasswordVisualTransformation(), // 👉 Oculta los caracteres.
                    isError = uiState.passError != null,
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                if (uiState.passError != null)
                    Text(uiState.passError!!, color = MaterialTheme.colorScheme.error, modifier = Modifier.fillMaxWidth())

                // ✅ Campo: Confirmar contraseña (también oculta).
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

                // ☎️ Campo: Teléfono (opcional).
                OutlinedTextField(
                    value = uiState.phone,
                    onValueChange = { registerViewModel.onPhoneChange(it) },
                    // 💡 Indicamos al usuario que solo necesita los últimos 8 dígitos.
                    label = { Text("Teléfono (opcional, 8 dígitos)") },
                    isError = uiState.phoneError != null,
                    singleLine = true,
                    // 💡 Agregamos un prefijo visible para una mejor UX.
                    leadingIcon = { Text("+569") },
                    modifier = Modifier.fillMaxWidth()
                )
                if (uiState.phoneError != null)
                    Text(uiState.phoneError!!, color = MaterialTheme.colorScheme.error, modifier = Modifier.fillMaxWidth())

                // 🎶 Sección: Géneros favoritos del usuario.
                Text(
                    "Géneros favoritos (selecciona al menos uno):",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.fillMaxWidth() // Alinea el texto a la izquierda
                )

                Column(Modifier.fillMaxWidth()) {
                    // ✅ Por cada género, mostramos una fila con checkbox + texto.
                    genreOptions.forEachIndexed { index, genre ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Checkbox(
                                checked = uiState.checkedGenres[index], // 👉 Muestra si está marcado.
                                onCheckedChange = { isChecked ->
                                    registerViewModel.onGenreCheckedChange(index, isChecked) // 👉 Actualiza el ViewModel.
                                }
                            )
                            // 👉 Mostramos el nombre del género (reemplazando "_" por espacio).
                            Text(genre.name.replace("_", " "))
                        }
                    }
                }
                if (uiState.genresError != null)
                    Text(uiState.genresError!!, color = MaterialTheme.colorScheme.error, modifier = Modifier.fillMaxWidth())

                // ⚠️ Si hay error general de envío, lo mostramos aquí.
                if (uiState.submitError != null)
                    Text(uiState.submitError!!, color = MaterialTheme.colorScheme.error)

                // 🚀 Botón de “Crear cuenta”. Solo se habilita si todo está validado.
                Button(
                    onClick = { registerViewModel.register() },
                    enabled = uiState.allValid, // 👉 El ViewModel decide si todos los campos son correctos.
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        // Color de fondo cuando el botón está HABILITADO
                        containerColor = MaterialTheme.colorScheme.primary,
                        // Color del texto cuando el botón está HABILITADO
                        contentColor = Color.White,
                        // Color de fondo cuando el botón está DESHABILITADO (¡Esta es la clave!)
                        disabledContainerColor = Color.Gray,
                        // Color del texto cuando el botón está DESHABILITADO
                        disabledContentColor = Color.White
                    )
                ) {
                    Text("Crear cuenta")
                }

                // 🔙 Botón “Volver” centrado, para regresar al login.
                TextButton(
                    onClick = onBack,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    colors = ButtonDefaults.buttonColors(
                        // Color de fondo cuando el botón está HABILITADO
                        containerColor = MaterialTheme.colorScheme.primary,
                        // Color del texto cuando el botón está HABILITADO
                        contentColor = Color.White,
                    )
                ) {
                    Text("Volver")
                }
            }
        }
    }
}

// Función de utilidad para crear el archivo de imagen temporal
private fun Context.createImageFile(): File {
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
    val imageFileName = "JPEG_" + timeStamp + "_"
    // ...usar cacheDir (interno) en vez de externalCacheDir para que FileProvider lo encuentre según res/xml/file_paths.xml
    return File.createTempFile(
        imageFileName,
        ".jpg",
        cacheDir
    )
}