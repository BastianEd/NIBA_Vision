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

// ... (data class RegisterUiState sin cambios) ...
data class RegisterUiState(
    val fullName: String = "",
    val email: String = "",
    val pass: String = "",
    val confirmPass: String = "",
    val phone: String = "",
    val checkedGenres: List<Boolean> = List(Genre.entries.size) { false },
    val profilePictureUri: Uri? = null,
    val nameError: String? = null,
    val emailError: String? = null,
    val passError: String? = null,
    val confirmPassError: String? = null,
    val phoneError: String? = null,
    val genresError: String? = null,
    val submitError: String? = null,
    val isRegistrationSuccess: Boolean = false
) {
    val allValid: Boolean
        get() = nameError == null && emailError == null && passError == null &&
                confirmPassError == null && phoneError == null && genresError == null &&
                fullName.isNotBlank() && email.isNotBlank() && pass.isNotBlank() && confirmPass.isNotBlank()
}

class RegisterViewModel(private val userRepository: UserRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    private fun normalizePhone(phone: String): String {
        val trimmedPhone = phone.trim()
        return if (trimmedPhone.length == 8 && trimmedPhone.all { it.isDigit() }) {
            "+569$trimmedPhone"
        } else {
            trimmedPhone
        }
    }

    fun onFullNameChange(name: String) {
        _uiState.update { it.copy(fullName = name, nameError = Validators.validateName(name)) }
    }
    fun onEmailChange(email: String) {
        _uiState.update { it.copy(email = email, emailError = Validators.validateEmail(email)) }
    }
    fun onPasswordChange(pass: String) {
        _uiState.update {
            it.copy(
                pass = pass,
                passError = Validators.validatePassword(pass),
                confirmPassError = Validators.validateConfirmPassword(pass, it.confirmPass)
            )
        }
    }
    fun onConfirmPasswordChange(confirmPass: String) {
        _uiState.update {
            it.copy(
                confirmPass = confirmPass,
                confirmPassError = Validators.validateConfirmPassword(it.pass, confirmPass)
            )
        }
    }
    fun onPhoneChange(phone: String) {
        val validatedPhone = normalizePhone(phone.ifBlank { "" })

        _uiState.update {
            it.copy(
                phone = phone,
                phoneError = Validators.validatePhone(validatedPhone.ifBlank { null })
            )
        }
    }
    fun onGenreCheckedChange(index: Int, isChecked: Boolean) {
        val updatedGenres = _uiState.value.checkedGenres.toMutableList()
        updatedGenres[index] = isChecked
        val genresError = Validators.validateGenres(updatedGenres.count { it })
        _uiState.update { it.copy(checkedGenres = updatedGenres, genresError = genresError) }
    }

    // 2. AÑADIMOS LA FUNCIÓN PARA MANEJAR LA FOTO
    fun onProfilePictureChange(uri: Uri?) {
        _uiState.update { it.copy(profilePictureUri = uri) }
    }
    fun register() {
        if (!_uiState.value.allValid) {
            _uiState.update { it.copy(submitError = "Por favor, corrige los errores.") }
            return
        }
        // Le decimos a la corutina que se ejecute en un hilo de fondo
        viewModelScope.launch(Dispatchers.IO) {
            val state = _uiState.value
            if (userRepository.exists(state.email.trim())) {
                _uiState.update { it.copy(submitError = "El correo ya está registrado.") }
            } else {
                val selectedGenres = Genre.entries.toTypedArray().filterIndexed { index, _ -> state.checkedGenres[index] }
                val normalizedPhone = normalizePhone(state.phone.ifBlank { "" })

                val user = User(
                    fullName = state.fullName.trim(),
                    email = state.email.trim(),
                    password = state.pass,
                    phone = normalizedPhone.ifBlank { null },
                    favoriteGenres = selectedGenres,
                    profilePictureUri = state.profilePictureUri?.toString()
                )
                userRepository.registerUserInDb(user)
                _uiState.update { it.copy(isRegistrationSuccess = true, submitError = null) }
            }
        }
    }
}