package com.example.niba_vision.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.niba_vision.data.UserRepository

/**
 * Fábrica (Factory) para crear instancias de nuestros ViewModels.
 *
 * Esto es necesario porque ahora nuestros ViewModels tienen dependencias
 * (como UserRepository) que necesitan ser pasadas en su constructor.
 */
class AppViewModelFactory(private val userRepository: UserRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(userRepository) as T
        }
        if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RegisterViewModel(userRepository) as T
        }
        // Agrega aquí otros ViewModels si los tuvieras
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}