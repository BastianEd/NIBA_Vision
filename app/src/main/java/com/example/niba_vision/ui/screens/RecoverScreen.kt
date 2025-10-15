package com.example.niba_vision.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.niba_vision.data.UserRepository
import com.example.niba_vision.util.Validators

/**
 * Pantalla para la recuperación de contraseña.
 *
 * El usuario ingresa su correo electrónico para simular el envío de instrucciones
 * de recuperación. Por razones de seguridad y privacidad, siempre muestra un mensaje
 * genérico, sin confirmar si el correo existe o no en el sistema.
 *
 * @param onBack Lambda para volver a la pantalla anterior.
 */
@Composable
fun RecoverScreen(onBack: () -> Unit) {
    // Estado para el correo y el mensaje de confirmación.
    var email by remember { mutableStateOf("") }
    val emailErr = Validators.validateEmail(email)
    var message by remember { mutableStateOf<String?>(null) }

    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            Modifier
                .fillMaxWidth(0.9f)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Recuperar contraseña", style = MaterialTheme.typography.headlineSmall)

            // Campo de texto para el correo.
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Correo @duoc.cl") },
                isError = emailErr != null,
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            if (emailErr != null) Text(emailErr, color = MaterialTheme.colorScheme.error)

            // Muestra el mensaje de simulación.
            if (message != null) Text(message!!)

            // Botón para solicitar la recuperación.
            Button(
                onClick = {
                    // Simulación: No se confirma si el correo existe.
                    message = "Si el correo existe, enviaremos instrucciones a $email."
                },
                enabled = emailErr == null,
                modifier = Modifier.fillMaxWidth()
            ) { Text("Enviar instrucciones") }

            // Botón para volver.
            TextButton(onClick = onBack, modifier = Modifier.align(Alignment.CenterHorizontally)) {
                Text("Volver")
            }
        }
    }
}