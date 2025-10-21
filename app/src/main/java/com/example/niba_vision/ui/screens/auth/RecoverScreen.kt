package com.example.niba_vision.ui.screens.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.niba_vision.viewmodel.RecoverViewModel

@Composable
fun RecoverScreen(onBack: () -> Unit, recoverViewModel: RecoverViewModel = viewModel()) {
    val uiState by recoverViewModel.uiState.collectAsState()

    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            Modifier
                .fillMaxWidth(0.9f)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Recuperar contraseña", style = MaterialTheme.typography.headlineSmall)

            OutlinedTextField(
                value = uiState.email,
                onValueChange = { recoverViewModel.onEmailChange(it) },
                label = { Text("Correo @duoc.cl") },
                isError = uiState.emailError != null,
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            if (uiState.emailError != null) Text(uiState.emailError!!, color = MaterialTheme.colorScheme.error)

            if (uiState.message != null) Text(uiState.message!!)

            Button(
                onClick = { recoverViewModel.sendRecoveryInstructions() },
                enabled = uiState.emailError == null && uiState.email.isNotBlank(),
                modifier = Modifier.fillMaxWidth()
            ) { Text("Enviar instrucciones") }

            TextButton(onClick = onBack, modifier = Modifier.align(Alignment.CenterHorizontally)) {
                Text("Volver")
            }
        }
    }
}