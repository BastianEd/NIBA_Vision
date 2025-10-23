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
    onRegister: () -> Unit,   // 👉 Aquí recibimos el callback para ir a "Crear cuenta"
    onRecover: () -> Unit,    // 👉 Aquí el callback para "Recuperar contraseña"
    onLoggedIn: () -> Unit,   // 👉 Aquí lo que pasa cuando el login resulta OK (navegación)
    loginViewModel: LoginViewModel = viewModel() // 👉 Obtenemos el ViewModel (estado y lógica del login)
) {
    // 👉 Nos “suscribimos” al estado expuesto por el ViewModel para que la UI se reactive sola
    val uiState by loginViewModel.uiState.collectAsState()

    // 👉 Aquí reaccionamos: si el login fue exitoso, disparamos la navegación
    LaunchedEffect(uiState.isLoginSuccess) {
        if (uiState.isLoginSuccess) {
            onLoggedIn() // 👉 “Listo, estás dentro”
        }
    }

    // 👉 Contenedor a pantalla completa, centrando el contenido
    Box(
        Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        // 👉 Columna principal: margen y separación “bonita” entre controles
        Column(
            Modifier
                .fillMaxWidth(0.9f)  // 👉 Dejamos un 10% de aire a los lados
                .padding(16.dp),     // 👉 Aquí aplicamos el “respiro” general
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // 👉 Título de la app y subtítulo de la pantalla (aplican tipografías del tema)
            Text("ZONALIBROS", style = MaterialTheme.typography.headlineMedium)
            Text("Inicio de sesión", style = MaterialTheme.typography.titleMedium)

            // 👉 Campo de correo: aquí el usuario escribe su email @duoc.cl
            OutlinedTextField(
                value = uiState.email,                          // 👉 Mostramos el valor actual
                onValueChange = { loginViewModel.onEmailChange(it) }, // 👉 Notificamos cambios al VM
                label = { Text("Correo @duoc.cl") },            // 👉 Etiqueta del campo
                singleLine = true,                              // 👉 Estilo de “una sola línea”
                isError = uiState.emailError != null,           // 👉 Aquí pintamos en error si corresponde
                modifier = Modifier.fillMaxWidth()
            )
            // 👉 Si hay error de email, lo mostramos bajo el campo con el color de error del tema
            if (uiState.emailError != null)
                Text(uiState.emailError!!, color = MaterialTheme.colorScheme.error)

            // 👉 Campo de contraseña: aquí escondemos el texto con puntitos
            OutlinedTextField(
                value = uiState.pass,
                onValueChange = { loginViewModel.onPasswordChange(it) }, // 👉 Validación vive en el VM
                label = { Text("Contraseña") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),    // 👉 Aquí ocultamos la pass
                isError = uiState.passError != null,
                modifier = Modifier.fillMaxWidth()
            )
            // 👉 Si hay error de contraseña, lo mostramos
            if (uiState.passError != null)
                Text(uiState.passError!!, color = MaterialTheme.colorScheme.error)

            // 👉 Si el backend o la lógica devolvió un error general de login, lo avisamos aquí
            if (uiState.loginError != null)
                Text(uiState.loginError!!, color = MaterialTheme.colorScheme.error)

            // 👉 Botón de “Ingresar”: solo se habilita si hay datos válidos
            Button(
                onClick = { loginViewModel.login() }, // 👉 Disparamos la acción de login del VM
                enabled =
                    uiState.emailError == null &&
                            uiState.passError == null &&
                            uiState.email.isNotBlank() &&
                            uiState.pass.isNotBlank(),
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    // 🎨 Aquí aplicamos los colores del botón (respetando el tema donde corresponde)
                    containerColor = MaterialTheme.colorScheme.primary, // 👉 Fondo cuando HABILITADO
                    contentColor = Color.White,                         // 👉 Texto cuando HABILITADO
                    disabledContainerColor = Color.DarkGray,            // 👉 Fondo cuando DESHABILITADO
                    disabledContentColor = Color.White                  // 👉 Texto cuando DESHABILITADO
                )
            ) {
                Text("Ingresar") // 👉 Texto del botón
            }

            // 👉 Acciones secundarias: registrarse o recuperar contraseña
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                TextButton(onClick = onRegister) { Text("Crear cuenta") }               // 👉 Navega a registro
                TextButton(onClick = onRecover) { Text("¿Olvidaste tu contraseña?") }   // 👉 Navega a recuperar
            }
        }
    }
}
