package com.example.niba_vision.ui.screens.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.niba_vision.viewmodel.LoginViewModel
import androidx.compose.ui.graphics.Color

@Composable
fun LoginScreen(
    onRegister: () -> Unit,
    onRecover: () -> Unit,
    onLoggedIn: () -> Unit,
    loginViewModel: LoginViewModel = viewModel()
) {
    val uiState by loginViewModel.uiState.collectAsState()

    // Efecto para navegar cuando el login es exitoso
    LaunchedEffect(uiState.isLoginSuccess) {
        if (uiState.isLoginSuccess) {
            onLoggedIn()
        }
    }

    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center, ) {
        Column(
            Modifier
                .fillMaxWidth(0.9f)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("ZONALIBROS", style = MaterialTheme.typography.headlineMedium)
            Text("Inicio de sesión", style = MaterialTheme.typography.titleMedium)

            OutlinedTextField(
                value = uiState.email,
                onValueChange = { loginViewModel.onEmailChange(it) },
                label = { Text("Correo @duoc.cl") },
                singleLine = true,
                isError = uiState.emailError != null,
                modifier = Modifier.fillMaxWidth()
            )
            if (uiState.emailError != null) Text(uiState.emailError!!, color = MaterialTheme.colorScheme.error)

            OutlinedTextField(
                value = uiState.pass,
                onValueChange = { loginViewModel.onPasswordChange(it) },
                label = { Text("Contraseña") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                isError = uiState.passError != null,
                modifier = Modifier.fillMaxWidth()
            )
            if (uiState.passError != null) Text(uiState.passError!!, color = MaterialTheme.colorScheme.error)

            if (uiState.loginError != null) Text(uiState.loginError!!, color = MaterialTheme.colorScheme.error)

            Button(
                onClick = { loginViewModel.login() },
                enabled = uiState.emailError == null && uiState.passError == null && uiState.email.isNotBlank() && uiState.pass.isNotBlank(),
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    // Color de fondo cuando el botón está HABILITADO
                    containerColor = MaterialTheme.colorScheme.primary,
                    // Color del texto cuando el botón está HABILITADO
                    contentColor = Color.White,
                    // Color de fondo cuando el botón está DESHABILITADO
                    disabledContainerColor = Color.DarkGray,
                    // Color del texto cuando el botón está DESHABILITADO
                    disabledContentColor = Color.White
                )
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