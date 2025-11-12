package com.example.niba_vision.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.niba_vision.data.Genre
import com.example.niba_vision.data.User
import com.example.niba_vision.data.UserRepository
import com.example.niba_vision.util.Validators
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import android.net.Uri

/**
 * Data class que representa el estado completo de la UI de la pantalla de Registro.
 * Es inmutable; cualquier cambio crea una nueva instancia.
 */
data class RegisterUiState(
    val fullName: String = "",
    val email: String = "",
    val pass: String = "",
    val confirmPass: String = "",
    val phone: String = "",
    val address: String = "", // <-- CAMBIO: Estado para la dirección
    val checkedGenres: List<Boolean> = List(Genre.entries.size) { false },
    val profilePictureUri: Uri? = null,
    val nameError: String? = null,
    val emailError: String? = null,
    val passError: String? = null,
    val confirmPassError: String? = null,
    val phoneError: String? = null,
    val addressError: String? = null, // <-- CAMBIO: Estado de error para la dirección
    val genresError: String? = null,
    val submitError: String? = null,
    val isRegistrationSuccess: Boolean = false
) {
    /**
     * Propiedad computada (calculada) que determina si el formulario es válido.
     * Se usa para habilitar o deshabilitar el botón de registro.
     */
    val allValid: Boolean
        get() = nameError == null && emailError == null && passError == null &&
                confirmPassError == null && phoneError == null && genresError == null &&
                addressError == null && // <-- CAMBIO: Comprobación de error de dirección
                fullName.isNotBlank() && email.isNotBlank() && pass.isNotBlank() &&
                confirmPass.isNotBlank() && address.isNotBlank() // <-- CAMBIO: Se asegura que la dirección no esté vacía
}

/**
 * ViewModel para la pantalla de Registro.
 * Maneja la lógica de negocio, validación y comunicación con el Repositorio.
 */
class RegisterViewModel(private val userRepository: UserRepository) : ViewModel() {

    // Flujo de estado privado y mutable que contiene el estado actual de la UI.
    private val _uiState = MutableStateFlow(RegisterUiState())
    // Flujo de estado público e inmutable expuesto a la UI.
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    // Normaliza el teléfono para guardarlo con prefijo
    private fun normalizePhone(phone: String): String {
        val trimmedPhone = phone.trim()
        // Si solo se ingresan 8 dígitos, se asume el prefijo +569
        return if (trimmedPhone.length == 8 && trimmedPhone.all { it.isDigit() }) {
            "+569$trimmedPhone"
        } else {
            trimmedPhone
        }
    }

    // Evento llamado cuando el campo de nombre cambia
    fun onFullNameChange(name: String) {
        _uiState.update { it.copy(fullName = name, nameError = Validators.validateName(name)) }
    }
    // Evento llamado cuando el campo de email cambia
    fun onEmailChange(email: String) {
        _uiState.update { it.copy(email = email, emailError = Validators.validateEmail(email)) }
    }
    // Evento llamado cuando el campo de contraseña cambia
    fun onPasswordChange(pass: String) {
        _uiState.update {
            it.copy(
                pass = pass,
                passError = Validators.validatePassword(pass),
                // Re-valida la confirmación de contraseña cada vez que la original cambia
                confirmPassError = Validators.validateConfirmPassword(pass, it.confirmPass)
            )
        }
    }
    // Evento llamado cuando el campo de confirmar contraseña cambia
    fun onConfirmPasswordChange(confirmPass: String) {
        _uiState.update {
            it.copy(
                confirmPass = confirmPass,
                confirmPassError = Validators.validateConfirmPassword(it.pass, confirmPass)
            )
        }
    }
    // Evento llamado cuando el campo de teléfono cambia
    fun onPhoneChange(phone: String) {
        val validatedPhone = normalizePhone(phone.ifBlank { "" })
        _uiState.update {
            it.copy(
                phone = phone,
                phoneError = Validators.validatePhone(validatedPhone.ifBlank { null })
            )
        }
    }

    /**
     * Evento llamado cuando el campo de dirección cambia.
     * Actualiza el estado de la dirección y su error.
     */
    fun onAddressChange(address: String) {
        val error = if (address.isBlank()) "La dirección no puede estar vacía." else null
        _uiState.update { it.copy(address = address, addressError = error) }
    }

    // Evento llamado cuando un checkbox de género cambia
    fun onGenreCheckedChange(index: Int, isChecked: Boolean) {
        val updatedGenres = _uiState.value.checkedGenres.toMutableList()
        updatedGenres[index] = isChecked
        val genresError = Validators.validateGenres(updatedGenres.count { it }) // Valida si al menos uno está marcado
        _uiState.update { it.copy(checkedGenres = updatedGenres, genresError = genresError) }
    }

    // Evento llamado cuando se toma una foto de perfil
    fun onProfilePictureChange(uri: Uri?) {
        _uiState.update { it.copy(profilePictureUri = uri) }
    }

    // Evento llamado cuando se presiona el botón "Crear cuenta"
    // Dentro de RegisterViewModel.kt

    fun register() {
        if (!_uiState.value.allValid) {
            _uiState.update { it.copy(submitError = "Por favor, corrige los errores.") }
            return
        }

        viewModelScope.launch(Dispatchers.IO) { // Sigue usando Dispatchers.IO para red
            val state = _uiState.value

            // Mapea los géneros
            val selectedGenres = Genre.entries.toTypedArray().filterIndexed { index, _ -> state.checkedGenres[index] }
            val normalizedPhone = normalizePhone(state.phone.ifBlank { "" })

            // Crea el objeto User (modelo de dominio)
            val user = User(
                fullName = state.fullName.trim(),
                email = state.email.trim(),
                password = state.pass, // El repositorio se encargará de esto
                phone = normalizedPhone.ifBlank { null },
                favoriteGenres = selectedGenres,
                profilePictureUri = state.profilePictureUri?.toString(),
                address = state.address.trim()
            )

            // *** ESTA ES LA LÍNEA QUE CAMBIA ***
            // Llama a la nueva función del repositorio
            val result = userRepository.registerUserApi(user)

            if (result.isSuccess) {
                // Éxito
                _uiState.update { it.copy(isRegistrationSuccess = true, submitError = null) }
            } else {
                // Falla (la API ya verificó si el email existía)
                _uiState.update { it.copy(submitError = result.exceptionOrNull()?.message) }
            }
        }
    }
}