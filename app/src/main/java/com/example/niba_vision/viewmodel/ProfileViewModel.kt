package com.example.niba_vision.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.niba_vision.data.SessionManager
import com.example.niba_vision.data.User
import com.example.niba_vision.data.UserRepository // 👈 Importante: Ahora usamos el repositorio
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

data class ProfileUiState(
    val currentUser: User? = null,
    val isLoading: Boolean = true
)

// 👇 CAMBIO 1: Ahora recibimos el repositorio en el constructor
class ProfileViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        observeCurrentUser()
    }

    private fun observeCurrentUser() {
        viewModelScope.launch {
            // Observamos al usuario actual para mantener la UI actualizada
            SessionManager.currentUser.collect { user ->
                _uiState.update { it.copy(currentUser = user, isLoading = false) }
            }
        }
    }

    // 👇 CAMBIO 2: Función principal para cambiar la foto
    fun changeProfilePicture(uri: Uri, context: Context) {
        val user = _uiState.value.currentUser ?: return

        viewModelScope.launch {
            // 1. Copiamos la foto de la galería a la carpeta privada de la app
            val newPath = copyImageToInternalStorage(uri, context)

            if (newPath != null) {
                // 2. Actualizamos la ruta en la Base de Datos
                val updatedUser = userRepository.updateProfilePicture(user.email, newPath)

                // 3. Si se guardó bien, actualizamos la sesión (esto refresca la pantalla automáticamente)
                if (updatedUser != null) {
                    SessionManager.login(updatedUser)
                }
            }
        }
    }

    // 👇 CAMBIO 3: Función auxiliar para guardar el archivo físicamente
    private fun copyImageToInternalStorage(uri: Uri, context: Context): String? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri)
            val fileName = "profile_${System.currentTimeMillis()}.jpg"
            val file = File(context.filesDir, fileName)
            val outputStream = FileOutputStream(file)

            inputStream?.use { input ->
                outputStream.use { output ->
                    input.copyTo(output)
                }
            }
            file.absolutePath // Retornamos la ruta donde quedó guardada
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}