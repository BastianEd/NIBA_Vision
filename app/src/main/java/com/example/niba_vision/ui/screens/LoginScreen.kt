package com.example.niba_vision.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
    var email by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }

    val emailError = Validators.validateEmail(email)
    val passError = if (pass.isBlank()) "La contraseña no puede estar vacía." else null
    val valid = emailError == null && passError == null

    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            Modifier
                .fillMaxWidth(0.9f)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("ZONALIBROS", style = MaterialTheme.typography.headlineMedium)
            Text("Inicio de sesión", style = MaterialTheme.typography.titleMedium)

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Correo @duoc.cl") },
                singleLine = true,
                isError = emailError != null,
                modifier = Modifier.fillMaxWidth()
            )
            if (emailError != null) Text(emailError, color = MaterialTheme.colorScheme.error)

            OutlinedTextField(
                value = pass,
                onValueChange = { pass = it },
                label = { Text("Contraseña") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                isError = passError != null,
                modifier = Modifier.fillMaxWidth()
            )
            if (passError != null) Text(passError, color = MaterialTheme.colorScheme.error)

            if (error != null) Text(error!!, color = MaterialTheme.colorScheme.error)

            Button(
                onClick = {
                    val res = UserRepository.login(email.trim(), pass)
                    if (res.isSuccess) onLoggedIn()
                    else error = res.exceptionOrNull()?.message
                },
                enabled = valid,
                modifier = Modifier.fillMaxWidth()
            ) { Text("Ingresar") }

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
