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
import androidx.compose.animation.*
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.InfiniteTransition
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import com.example.niba_vision.R

const val ANIMATION_DURATION_MS = 600

// 💡 Nuevo Composable: Simulación de animación de libro pasando páginas
@Composable
fun BookLoadingAnimation() {
    val infiniteTransition = rememberInfiniteTransition(label = "BookFlipAnimation")

    val rotation by infiniteTransition.animateFloat(
        initialValue = -15f,
        targetValue = 15f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 800),
            repeatMode = RepeatMode.Reverse
        ),
        label = "BookRotation"
    )

    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 400),
            repeatMode = RepeatMode.Reverse
        ),
        label = "BookScale"
    )

    Box(
        modifier = Modifier
            .size(150.dp), // 💡 Mayor tamaño para que se note más
        contentAlignment = Alignment.Center
    ) {
        // Indicador de progreso circular
        CircularProgressIndicator(
            modifier = Modifier
                .fillMaxSize(),
            color = MaterialTheme.colorScheme.primary,
            strokeWidth = 8.dp
        )

        // Icono temático de libro
        Icon(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = "Cargando libro pasando páginas",
            // 💡 Usamos el color de contraste (onPrimary) para que se vea sobre el Primary del círculo
            tint = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier
                .size(100.dp)
                .rotate(rotation)
                .scale(scale)
        )
    }
}


@Composable
fun LoginScreen(
    onRegister: () -> Unit,
    onRecover: () -> Unit,
    onLoggedIn: () -> Unit,
    loginViewModel: LoginViewModel = viewModel()
) {
    val uiState by loginViewModel.uiState.collectAsState()

    val transitionState = remember { MutableTransitionState(false) }
    LaunchedEffect(Unit) { transitionState.targetState = true }

    LaunchedEffect(uiState.isLoginSuccess) {
        if (uiState.isLoginSuccess) {
            onLoggedIn()
        }
    }

    Box(
        Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        // 💫 Animación de entrada "vívida" del formulario
        AnimatedVisibility(
            visibleState = transitionState,
            enter = slideInVertically(
                initialOffsetY = { it * 2 },
                animationSpec = tween(ANIMATION_DURATION_MS)
            ) + fadeIn(tween(ANIMATION_DURATION_MS * 3 / 4, delayMillis = ANIMATION_DURATION_MS / 4)),
            exit = fadeOut(tween(300))
        ) {
            Column(
                Modifier
                    .fillMaxWidth(0.9f)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // ... [Contenido del formulario de Login] ...

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
                if (uiState.emailError != null)
                    Text(uiState.emailError!!, color = MaterialTheme.colorScheme.error)

                OutlinedTextField(
                    value = uiState.pass,
                    onValueChange = { loginViewModel.onPasswordChange(it) },
                    label = { Text("Contraseña") },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    isError = uiState.passError != null,
                    modifier = Modifier.fillMaxWidth()
                )
                if (uiState.passError != null)
                    Text(uiState.passError!!, color = MaterialTheme.colorScheme.error)

                if (uiState.loginError != null)
                    Text(uiState.loginError!!, color = MaterialTheme.colorScheme.error)

                Button(
                    onClick = { loginViewModel.login() },
                    enabled =
                        !uiState.isLoginLoading &&
                                uiState.emailError == null &&
                                uiState.passError == null &&
                                uiState.email.isNotBlank() &&
                                uiState.pass.isNotBlank(),
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = Color.White,
                        disabledContainerColor = Color.DarkGray,
                        disabledContentColor = Color.White
                    )
                ) {
                    Text("Ingresar")
                }

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextButton(onClick = onRegister) { Text("Crear cuenta") }
                    TextButton(onClick = onRecover) { Text("¿Olvidaste tu contraseña?") }
                }
            }
        }

        // 💡 Animación de carga de libro superpuesta (con dimming effect)
        AnimatedVisibility(
            visible = uiState.isLoginLoading,
            enter = fadeIn(tween(300)),
            exit = fadeOut(tween(300))
        ) {
            // 💡 Fondo oscuro y opaco para garantizar que se vea el cargador
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = Color.Black.copy(alpha = 0.5f) // Oscurece el fondo
            ) {
                Box(contentAlignment = Alignment.Center) {
                    BookLoadingAnimation() // Muestra el libro animado
                }
            }
        }
    }
}