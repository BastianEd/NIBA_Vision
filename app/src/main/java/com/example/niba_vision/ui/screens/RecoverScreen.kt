package com.example.niba_vision.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.niba_vision.data.UserRepository
import com.example.niba_vision.util.Validators

@Composable
fun RecoverScreen(onBack: () -> Unit) {
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

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Correo @duoc.cl") },
                isError = emailErr != null,
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            if (emailErr != null) Text(emailErr, color = MaterialTheme.colorScheme.error)

            if (message != null) Text(message!!)

            Button(
                onClick = {
                    // Simulación: siempre mostramos el mismo mensaje por privacidad
                    val exist = UserRepository.exists(email.trim())
                    message = "Si el correo existe, enviaremos instrucciones a $email."
                },
                enabled = emailErr == null,
                modifier = Modifier.fillMaxWidth()
            ) { Text("Enviar instrucciones") }

            TextButton(onClick = onBack, modifier = Modifier.align(Alignment.CenterHorizontally)) {
                Text("Volver")
            }
        }
    }
}
