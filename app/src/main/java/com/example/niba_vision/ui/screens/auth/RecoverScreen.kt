package com.example.niba_vision.ui.screens.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.niba_vision.viewmodel.RecoverViewModel

// 🧩 Esta pantalla permite al usuario recuperar su contraseña.
// Aquí se ingresa el correo y se envían instrucciones para restablecer la cuenta.
@Composable
fun RecoverScreen(
    onBack: () -> Unit,                      // 👉 Acción para volver a la pantalla anterior.
    recoverViewModel: RecoverViewModel = viewModel() // 👉 Conectamos la vista con el ViewModel que maneja la lógica.
) {
    // 📡 Nos “suscribimos” al estado de la vista (uiState) para que Compose actualice automáticamente la pantalla.
    val uiState by recoverViewModel.uiState.collectAsState()

    // 📦 Box centra el contenido dentro de toda la pantalla.
    Box(
        Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        // 🧱 Columna principal que organiza todos los elementos de arriba hacia abajo.
        Column(
            Modifier
                .fillMaxWidth(0.9f)  // 👉 Dejamos márgenes laterales (para que no quede pegado al borde).
                .padding(16.dp),     // 👉 Agregamos espacio interno alrededor del contenido.
            verticalArrangement = Arrangement.spacedBy(12.dp) // 👉 Separa los elementos con 12dp de espacio.
        ) {
            // 🏷️ Título principal de la pantalla.
            Text("Recuperar contraseña", style = MaterialTheme.typography.headlineSmall)

            // ✉️ Campo para ingresar el correo electrónico institucional.
            OutlinedTextField(
                value = uiState.email,                            // 👉 Mostramos el texto actual.
                onValueChange = { recoverViewModel.onEmailChange(it) }, // 👉 Notificamos cambios al ViewModel.
                label = { Text("Correo @duoc.cl") },               // 👉 Etiqueta que se muestra sobre el campo.
                isError = uiState.emailError != null,              // 👉 Muestra el campo en rojo si hay error.
                singleLine = true,                                 // 👉 Evita saltos de línea en el texto.
                modifier = Modifier.fillMaxWidth()                 /*// 👉 Hace que el campo ocupetodo el ancho*/
            )

            // ⚠️ Si el correo no es válido, mostramos el mensaje de error en rojo.
            if (uiState.emailError != null)
                Text(uiState.emailError!!, color = MaterialTheme.colorScheme.error)

            // 💬 Si hay un mensaje de éxito o estado (por ejemplo, “Se enviaron las instrucciones”), se muestra aquí.
            if (uiState.message != null)
                Text(uiState.message!!)

            // 📤 Botón que envía las instrucciones de recuperación.
            Button(
                onClick = { recoverViewModel.sendRecoveryInstructions() }, // 👉 Llama la función del ViewModel.
                enabled = uiState.emailError == null && uiState.email.isNotBlank(), // 👉 Solo se habilita si hay correo válido.
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Enviar instrucciones")
            }

            // 🔙 Botón de texto centrado que permite volver atrás.
            TextButton(
                onClick = onBack,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Volver")
            }
        }
    }
}
