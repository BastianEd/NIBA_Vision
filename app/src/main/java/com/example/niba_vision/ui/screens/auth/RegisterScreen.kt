package com.example.niba_vision.ui.screens.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.niba_vision.data.Genre
import com.example.niba_vision.viewmodel.RegisterViewModel
import androidx.compose.animation.*
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween

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
                verticalArrangement = Arrangement.spacedBy(12.dp) // 👉 Espacio uniforme entre elementos.
            ) {
                // 🏷️ Título de la pantalla.
                Text("Registro", style = MaterialTheme.typography.headlineMedium)

                // 👤 Campo: Nombre completo del usuario.
                OutlinedTextField(
                    value = uiState.fullName,
                    onValueChange = { registerViewModel.onFullNameChange(it) },
                    label = { Text("Nombre completo") },
                    isError = uiState.nameError != null,
                    modifier = Modifier.fillMaxWidth()
                )
                if (uiState.nameError != null)
                    Text(uiState.nameError!!, color = MaterialTheme.colorScheme.error)

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
                    Text(uiState.emailError!!, color = MaterialTheme.colorScheme.error)

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
                    Text(uiState.passError!!, color = MaterialTheme.colorScheme.error)

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
                    Text(uiState.confirmPassError!!, color = MaterialTheme.colorScheme.error)

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
                    Text(uiState.phoneError!!, color = MaterialTheme.colorScheme.error)

                // 🎶 Sección: Géneros favoritos del usuario.
                Text(
                    "Géneros favoritos (selecciona al menos uno):",
                    style = MaterialTheme.typography.titleMedium
                )

                // ✅ Por cada género, mostramos una fila con checkbox + texto.
                genreOptions.forEachIndexed { index, genre ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
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
                if (uiState.genresError != null)
                    Text(uiState.genresError!!, color = MaterialTheme.colorScheme.error)

                // ⚠️ Si hay error general de envío, lo mostramos aquí.
                if (uiState.submitError != null)
                    Text(uiState.submitError!!, color = MaterialTheme.colorScheme.error)

                // 🚀 Botón de “Crear cuenta”. Solo se habilita si todo está validado.
                Button(
                    onClick = { registerViewModel.register() },
                    enabled = uiState.allValid, // 👉 El ViewModel decide si todos los campos son correctos.
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Crear cuenta")
                }

                // 🔙 Botón “Volver” centrado, para regresar al login.
                TextButton(
                    onClick = onBack,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text("Volver")
                }
            }
        }
    }
}