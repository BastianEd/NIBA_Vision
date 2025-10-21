package com.example.niba_vision.viewmodel

import androidx.lifecycle.ViewModel
import com.example.niba_vision.util.Validators
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class RecoverUiState(
    val email: String = "",
    val emailError: String? = null,
    val message: String? = null
)

class RecoverViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(RecoverUiState())
    val uiState: StateFlow<RecoverUiState> = _uiState.asStateFlow()

    fun onEmailChange(email: String) {
        _uiState.update { it.copy(email = email, emailError = Validators.validateEmail(email)) }
    }

    fun sendRecoveryInstructions() {
        if (_uiState.value.emailError == null) {
            val email = _uiState.value.email
            _uiState.update {
                it.copy(message = "Si el correo '$email' existe en nuestro sistema, enviaremos las instrucciones de recuperación.")
            }
        }
    }
}