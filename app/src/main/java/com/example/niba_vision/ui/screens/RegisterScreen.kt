package com.example.niba_vision.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.niba_vision.data.Genre
import com.example.niba_vision.data.User
import com.example.niba_vision.data.UserRepository
import com.example.niba_vision.util.Validators

/**
 * Pantalla de registro de nuevos usuarios.
 *
 * Contiene un formulario completo con validaciones en tiempo real para todos los campos
 * requeridos por el sistema.
 *
 * @param onBack Lambda para navegar a la pantalla anterior.
 * @param onRegistered Lambda que se invoca tras un registro exitoso.
 */
@Composable
fun RegisterScreen(
    onBack: () -> Unit,
    onRegistered: () -> Unit
) {
    // Estados para cada campo del formulario.
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var pw by remember { mutableStateOf("") }
    var pw2 by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }

    // Estado para los checkboxes de géneros.
    val options = Genre.values().toList()
    val checked = remember {
        mutableStateListOf<Boolean>().apply { addAll(List(options.size) { false }) }
    }

    // Validaciones en tiempo real para cada campo.
    val nameErr = Validators.validateName(fullName)
    val emailErr = Validators.validateEmail(email)
    val pwErr = Validators.validatePassword(pw)
    val pw2Err = Validators.validateConfirmPassword(pw, pw2)
    val phoneErr = Validators.validatePhone(phone.ifBlank { null })
    val genresErr = Validators.validateGenres(checked.count { it })

    // Estado para habilitar el botón de registro y para errores de envío.
    val allValid = listOf(nameErr, emailErr, pwErr, pw2Err, phoneErr, genresErr).all { it == null }
    var submitError by remember { mutableStateOf<String?>(null) }

    // Columna principal con scroll.
    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Registro", style = MaterialTheme.typography.headlineMedium)

        // --- Campos del formulario con sus validaciones ---

        OutlinedTextField(
            value = fullName,
            onValueChange = { fullName = it },
            label = { Text("Nombre completo") },
            isError = nameErr != null,
            modifier = Modifier.fillMaxWidth()
        )
        if (nameErr != null) Text(nameErr, color = MaterialTheme.colorScheme.error)

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Correo @duoc.cl") },
            isError = emailErr != null,
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        if (emailErr != null) Text(emailErr, color = MaterialTheme.colorScheme.error)

        OutlinedTextField(
            value = pw,
            onValueChange = { pw = it },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            isError = pwErr != null,
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        if (pwErr != null) Text(pwErr, color = MaterialTheme.colorScheme.error)

        OutlinedTextField(
            value = pw2,
            onValueChange = { pw2 = it },
            label = { Text("Confirmar contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            isError = pw2Err != null,
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        if (pw2Err != null) Text(pw2Err, color = MaterialTheme.colorScheme.error)

        OutlinedTextField(
            value = phone,
            onValueChange = { phone = it },
            label = { Text("Teléfono (opcional)") },
            isError = phoneErr != null,
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        if (phoneErr != null) Text(phoneErr, color = MaterialTheme.colorScheme.error)

        // --- Checkboxes para géneros favoritos ---
        Text("Géneros favoritos (selecciona al menos uno):", style = MaterialTheme.typography.titleMedium)
        options.forEachIndexed { idx, g ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = checked[idx],
                    onCheckedChange = { checked[idx] = it }
                )
                Text(
                    when (g) {
                        Genre.FICCION -> "FICCIÓN"
                        Genre.NO_FICCION -> "NO FICCIÓN"
                        Genre.MISTERIO -> "MISTERIO"
                        Genre.TERROR -> "TERROR"
                        Genre.SUSPENSO -> "SUSPENSO"
                        Genre.HISTORIA -> "HISTORIA"
                    }
                )
            }
        }
        if (genresErr != null) Text(genresErr, color = MaterialTheme.colorScheme.error)

        // Muestra error si el correo ya existe.
        if (submitError != null) Text(submitError!!, color = MaterialTheme.colorScheme.error)

        // Botón para crear la cuenta.
        Button(
            onClick = {
                // Lógica de registro.
                if (UserRepository.exists(email.trim())) {
                    submitError = "El correo ya está registrado."
                } else {
                    val selected = options.filterIndexed { i, _ -> checked[i] }
                    val user = User(
                        fullName = fullName.trim(),
                        email = email.trim(),
                        password = pw,
                        phone = phone.ifBlank { null },
                        favoriteGenres = selected
                    )
                    val result = UserRepository.register(user)
                    if (result.isSuccess) onRegistered()
                    else submitError = result.exceptionOrNull()?.message
                }
            },
            enabled = allValid, // Solo se activa si todas las validaciones pasan.
            modifier = Modifier.fillMaxWidth()
        ) { Text("Crear cuenta") }

        TextButton(onClick = onBack, modifier = Modifier.align(Alignment.CenterHorizontally)) {
            Text("Volver")
        }
    }
}