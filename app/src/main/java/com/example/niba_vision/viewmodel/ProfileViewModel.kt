package com.example.niba_vision.viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.niba_vision.data.User
import com.example.niba_vision.data.UserRepository
import com.example.niba_vision.data.SessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// Estado de la UI
data class ProfileUiState(
    val user: User? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val updateSuccess: Boolean = false,
    val selectedImageUri: Uri? = null
)

class ProfileViewModel(
    private val userRepository: UserRepository,
    private val sessionManager: SessionManager // SessionManager es un object, pero aquí lo inyectamos o usamos directo
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        loadUserProfile()
    }

    private fun loadUserProfile() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                // CORRECCIÓN: Usamos .value para obtener el usuario actual del StateFlow
                val currentUser = sessionManager.currentUser.value

                if (currentUser != null) {
                    _uiState.update {
                        it.copy(
                            user = currentUser,
                            isLoading = false,
                            selectedImageUri = if (!currentUser.profilePictureUri.isNullOrEmpty()) Uri.parse(currentUser.profilePictureUri) else null
                        )
                    }
                } else {
                    _uiState.update { it.copy(isLoading = false, error = "No hay sesión activa") }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }

    fun onProfileImageSelected(uri: Uri) {
        _uiState.update { it.copy(selectedImageUri = uri) }
    }

    fun updateUserProfile(fullName: String, email: String) {
        viewModelScope.launch {
            // CORRECCIÓN: Obtenemos el usuario del estado actual
            val currentUser = _uiState.value.user ?: return@launch

            val newImageUriString = _uiState.value.selectedImageUri?.toString() ?: currentUser.profilePictureUri

            _uiState.update { it.copy(isLoading = true, error = null, updateSuccess = false) }

            try {
                val userToUpdate = currentUser.copy(
                    fullName = fullName,
                    email = email,
                    profilePictureUri = newImageUriString
                )

                // CORRECCIÓN: Llamamos al método que acabamos de crear en el repositorio
                val result = userRepository.updateUser(userToUpdate)

                result.onSuccess { updatedUser ->
                    // CORRECCIÓN: Usamos 'login' para actualizar la sesión en memoria (SessionManager no tiene saveUser)
                    sessionManager.login(updatedUser)

                    _uiState.update {
                        it.copy(
                            user = updatedUser,
                            isLoading = false,
                            updateSuccess = true
                        )
                    }
                }.onFailure { error ->
                    _uiState.update { it.copy(isLoading = false, error = "Error al actualizar: ${error.message}") }
                }

            } catch (e: Exception) {
                Log.e("ProfileViewModel", "Error updating profile", e)
                _uiState.update { it.copy(isLoading = false, error = "Error al actualizar: ${e.message}") }
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null, updateSuccess = false) }
    }
}