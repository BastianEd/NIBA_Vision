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

@Composable
fun RegisterScreen(
    onBack: () -> Unit,
    onRegistered: () -> Unit,
    registerViewModel: RegisterViewModel = viewModel()
) {
    val uiState by registerViewModel.uiState.collectAsState()
    val genreOptions = Genre.entries

    LaunchedEffect(uiState.isRegistrationSuccess) {
        if (uiState.isRegistrationSuccess) {
            onRegistered()
        }
    }

    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Registro", style = MaterialTheme.typography.headlineMedium)

        OutlinedTextField(
            value = uiState.fullName,
            onValueChange = { registerViewModel.onFullNameChange(it) },
            label = { Text("Nombre completo") },
            isError = uiState.nameError != null,
            modifier = Modifier.fillMaxWidth()
        )
        if (uiState.nameError != null) Text(uiState.nameError!!, color = MaterialTheme.colorScheme.error)

        OutlinedTextField(
            value = uiState.email,
            onValueChange = { registerViewModel.onEmailChange(it) },
            label = { Text("Correo @duoc.cl") },
            isError = uiState.emailError != null,
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        if (uiState.emailError != null) Text(uiState.emailError!!, color = MaterialTheme.colorScheme.error)

        OutlinedTextField(
            value = uiState.pass,
            onValueChange = { registerViewModel.onPasswordChange(it) },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            isError = uiState.passError != null,
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        if (uiState.passError != null) Text(uiState.passError!!, color = MaterialTheme.colorScheme.error)

        OutlinedTextField(
            value = uiState.confirmPass,
            onValueChange = { registerViewModel.onConfirmPasswordChange(it) },
            label = { Text("Confirmar contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            isError = uiState.confirmPassError != null,
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        if (uiState.confirmPassError != null) Text(uiState.confirmPassError!!, color = MaterialTheme.colorScheme.error)

        OutlinedTextField(
            value = uiState.phone,
            onValueChange = { registerViewModel.onPhoneChange(it) },
            label = { Text("Teléfono (opcional)") },
            isError = uiState.phoneError != null,
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        if (uiState.phoneError != null) Text(uiState.phoneError!!, color = MaterialTheme.colorScheme.error)

        Text("Géneros favoritos (selecciona al menos uno):", style = MaterialTheme.typography.titleMedium)
        genreOptions.forEachIndexed { index, genre ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = uiState.checkedGenres[index],
                    onCheckedChange = { isChecked -> registerViewModel.onGenreCheckedChange(index, isChecked) }
                )
                Text(genre.name.replace("_", " "))
            }
        }
        if (uiState.genresError != null) Text(uiState.genresError!!, color = MaterialTheme.colorScheme.error)

        if (uiState.submitError != null) Text(uiState.submitError!!, color = MaterialTheme.colorScheme.error)

        Button(
            onClick = { registerViewModel.register() },
            enabled = uiState.allValid,
            modifier = Modifier.fillMaxWidth()
        ) { Text("Crear cuenta") }

        TextButton(onClick = onBack, modifier = Modifier.align(Alignment.CenterHorizontally)) {
            Text("Volver")
        }
    }
}