package com.example.niba_vision.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.niba_vision.data.UserRepository
import com.example.niba_vision.util.Validators
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// Data class para representar el estado de la UI
data class LoginUiState(
    val email: String = "",
    val pass: String = "",
    val emailError: String? = null,
    val passError: String? = null,
    val loginError: String? = null,
    val isLoginSuccess: Boolean = false,
    val isLoginLoading: Boolean = false // 💡 Nuevo: Estado para indicar si el proceso de login está activo
)

class LoginViewModel(private val userRepository: UserRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onEmailChange(email: String) {
        _uiState.update { it.copy(email = email, emailError = Validators.validateEmail(email)) }
    }

    fun onPasswordChange(pass: String) {
        val passError = if (pass.isBlank()) "La contraseña no puede estar vacía." else null
        _uiState.update { it.copy(pass = pass, passError = passError) }
    }

    fun login() {
        // Marcamos el inicio de la carga y limpiamos errores de intentos anteriores.
        _uiState.update { it.copy(isLoginLoading = true, loginError = null) }

        // Usamos una corrutina para llamar a la base de datos de forma segura
        viewModelScope.launch { // Llamar a la BD
            val state = _uiState.value
            // Nueva función 'suspend' del repositorio
            val result = userRepository.login(state.email.trim(), state.pass)
            if (result.isSuccess) {
                _uiState.update { it.copy(isLoginSuccess = true, loginError = null, isLoginLoading = false) } // 💡 Limpiamos la carga al terminar
            } else {
                _uiState.update { it.copy(loginError = result.exceptionOrNull()?.message, isLoginLoading = false) } // 💡 Limpiamos la carga al fallar
            }
        }
    }
}