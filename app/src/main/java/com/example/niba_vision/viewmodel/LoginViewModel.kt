package com.example.niba_vision.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.niba_vision.data.SessionManager
import com.example.niba_vision.data.UserRepository
import com.example.niba_vision.util.Validators
import kotlinx.coroutines.Dispatchers
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
    val isLoginLoading: Boolean = false
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
        _uiState.update { it.copy(isLoginLoading = true, loginError = null) }

        // *** CAMBIO AQUÍ: Añadimos (Dispatchers.IO) ***
        // Le decimos a la corutina que se ejecute en un hilo de fondo
        viewModelScope.launch(Dispatchers.IO) {
            val state = _uiState.value
            val result = userRepository.login(state.email.trim(), state.pass)
            if (result.isSuccess) {
                result.getOrNull()?.let {
                    user -> SessionManager.login(user)
                }
                _uiState.update { it.copy(isLoginSuccess = true, loginError = null, isLoginLoading = false) }
            } else {
                _uiState.update { it.copy(loginError = result.exceptionOrNull()?.message, isLoginLoading = false) }
            }
        }
    }
}