package com.example.niba_vision.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.niba_vision.data.SessionManager
import com.example.niba_vision.data.User
//import com.example.niba_vision.data.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Estado de la UI para la pantalla de perfil.
 */
data class ProfileUiState(
    val currentUser: User? = null,
    val isLoading: Boolean = true
)

/**
 * ViewModel para la pantalla de perfil.
 *
 * NOTA: En una app real, obtendrías el ID o email del usuario logueado
 * desde una sesión. Para este ejemplo, simularemos esto obteniendo el
 * último usuario registrado de la base de datos.
 */
class ProfileViewModel() : ViewModel() {
    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        observeCurrentUser()
    }

    private fun observeCurrentUser() {
        viewModelScope.launch {
            // Observamos el usuario actual desde el SessionManager
            SessionManager.currentUser.collect { user ->
                _uiState.update {
                    it.copy(currentUser = user, isLoading = false)
                }
            }
        }
    }
}