package com.example.niba_vision

import com.example.niba_vision.util.Validators
import org.junit.Assert.*
import org.junit.Test

class ValidatorsTest {

    // 1. Test para correos electrónicos
    @Test
    fun email_validator_correct() {
        // Caso: Correo válido de duoc
        val result = Validators.validateEmail("alumno@duoc.cl")
        assertNull("El correo debería ser válido (null)", result)
    }

    @Test
    fun email_validator_incorrect_domain() {
        // Caso: Correo que no es duoc.cl
        val result = Validators.validateEmail("usuario@gmail.com")
        assertEquals("Debe ser un correo @duoc.cl válido.", result)
    }

    @Test
    fun email_validator_empty() {
        // Caso: Correo vacío
        val result = Validators.validateEmail("")
        assertEquals("El correo no puede estar vacío.", result)
    }

    // 2. Test para contraseñas
    @Test
    fun password_validator_weak() {
        // Caso: Contraseña corta y sin caracteres especiales
        val result = Validators.validatePassword("12345")
        assertNotNull("Debería devolver error", result)
        // Verificamos que el mensaje contenga la advertencia de longitud
        assertTrue(result!!.contains("Al menos 10 caracteres"))
    }

    @Test
    fun password_validator_strong() {
        // Caso: Contraseña fuerte (Mayúscula, minúscula, número, especial, largo)
        val result = Validators.validatePassword("HolaMundo123@")
        assertNull("La contraseña debería ser válida", result)
    }

    // 3. Test para Teléfono (basado en tu nueva lógica de 8 dígitos)
    @Test
    fun phone_validator_correct() {
        // Caso: 8 dígitos sin prefijo (tu código lo permite en la validación)
        val result = Validators.validatePhone("12345678")
        assertNull(result)
    }
}