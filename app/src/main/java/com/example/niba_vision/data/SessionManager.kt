package com.example.niba_vision.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Objeto singleton para gestionar la sesión del usuario actual en toda la aplicación.
 */
object SessionManager {
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser = _currentUser.asStateFlow()

    /**
     * Inicia la sesión de un usuario, almacenando sus datos.
     * @param user El usuario que ha iniciado sesión.
     */
    fun login(user: User) {
        _currentUser.value = user
    }

    /**
     * Cierra la sesión actual, eliminando los datos del usuario.
     */
    fun logout() {
        _currentUser.value = null
    }
}