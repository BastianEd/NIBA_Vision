package com.example.niba_vision.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.niba_vision.data.UserRepository
import com.example.niba_vision.util.Validators

/**
 * Composable que representa la pantalla de inicio de sesión.
 *
 * Permite a los usuarios ingresar sus credenciales (correo y contraseña) para acceder a la app.
 * También ofrece opciones para navegar a las pantallas de registro y recuperación de contraseña.
 *
 * @param onRegister Lambda que se invoca cuando el usuario presiona "Crear cuenta".
 * @param onRecover Lambda que se invoca cuando el usuario presiona "¿Olvidaste tu contraseña?".
 * @param onLoggedIn Lambda que se invoca tras una autenticación exitosa.
 */
@Composable
fun LoginScreen(
    onRegister: () -> Unit,
    onRecover: () -> Unit,
    onLoggedIn: () -> Unit
) {
    // Estados para almacenar el email, la contraseña y los mensajes de error.
    var email by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }

    // Validación en tiempo real de los campos.
    val emailError = Validators.validateEmail(email)
    val passError = if (pass.isBlank()) "La contraseña no puede estar vacía." else null
    val valid = email.isNotBlank() && pass.isNotBlank() && emailError == null

    // Contenedor principal centrado.
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            Modifier
                .fillMaxWidth(0.9f)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("ZONALIBROS", style = MaterialTheme.typography.headlineMedium)
            Text("Inicio de sesión", style = MaterialTheme.typography.titleMedium)

            // Campo de texto para el correo.
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Correo @duoc.cl") },
                singleLine = true,
                isError = emailError != null && email.isNotBlank(),
                modifier = Modifier.fillMaxWidth()
            )
            if (emailError != null && email.isNotBlank()) Text(emailError, color = MaterialTheme.colorScheme.error)

            // Campo de texto para la contraseña.
            OutlinedTextField(
                value = pass,
                onValueChange = { pass = it },
                label = { Text("Contraseña") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                isError = pass.isBlank() && email.isNotBlank(), // Muestra error en pass si está vacío y ya se empezó a escribir en email
                modifier = Modifier.fillMaxWidth()
            )
            if (passError != null && pass.isBlank() && email.isNotBlank()) Text(passError, color = MaterialTheme.colorScheme.error)

            // Muestra el error de inicio de sesión.
            if (error != null) Text(error!!, color = MaterialTheme.colorScheme.error)

            // Botón para intentar el inicio de sesión.
            Button(
                onClick = {
                    val res = UserRepository.login(email.trim(), pass)
                    if (res.isSuccess) onLoggedIn()
                    else error = res.exceptionOrNull()?.message
                },
                enabled = valid,
                modifier = Modifier.fillMaxWidth(),
                // --- ¡AQUÍ ESTÁ LA SOLUCIÓN! ---
                colors = ButtonDefaults.buttonColors(
                    // Color de fondo cuando el botón está HABILITADO
                    containerColor = MaterialTheme.colorScheme.primary,
                    // Color de fondo cuando el botón está DESHABILITADO
                    disabledContainerColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f),
                    // Color del texto cuando el botón está HABILITADO
                    contentColor = Color.White,
                    // Color del texto cuando el botón está DESHABILITADO
                    disabledContentColor = Color.White.copy(alpha = 0.7f)
                )
            ) { Text("Ingresar") }

            // Botones para navegar a registro y recuperación.
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                TextButton(onClick = onRegister) { Text("Crear cuenta") }
                TextButton(onClick = onRecover) { Text("¿Olvidaste tu contraseña?") }
            }
        }
    }
}