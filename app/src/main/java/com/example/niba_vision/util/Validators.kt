package com.example.niba_vision.util

object Validators {
    private val nameRegex = Regex("^[A-Za-zÁÉÍÓÚáéíóúÑñ ]{1,100}$")
    private val emailRegex = Regex("^[A-Za-z0-9._%+-]+@duoc\\.cl$", RegexOption.IGNORE_CASE)
    private val upper = Regex(".*[A-Z].*")
    private val lower = Regex(".*[a-z].*")
    private val digit = Regex(".*[0-9].*")
    private val special = Regex(".*[@#\\$%].*")
    private val phoneRegex = Regex("^\\+?\\d{7,15}$")

    fun validateName(name: String) = when {
        name.isBlank() -> "El nombre no puede estar vacío."
        !nameRegex.matches(name.trim()) -> "Solo letras y espacios (máx. 100)."
        else -> null
    }

    fun validateEmail(email: String) = when {
        email.isBlank() -> "El correo no puede estar vacío."
        email.length > 60 -> "Máx. 60 caracteres."
        !emailRegex.matches(email.trim()) -> "Debe ser un correo @duoc.cl válido."
        else -> null
    }

    fun validatePassword(pw: String) = buildList {
        if (pw.length < 10) add("Al menos 10 caracteres.")
        if (!upper.matches(pw)) add("Debe incluir una mayúscula.")
        if (!lower.matches(pw)) add("Debe incluir una minúscula.")
        if (!digit.matches(pw)) add("Debe incluir un número.")
        if (!special.matches(pw)) add("Debe incluir un caracter especial (@#$%).")
    }.takeIf { it.isNotEmpty() }?.joinToString(" ")

    fun validateConfirmPassword(pw: String, confirm: String) =
        if (confirm != pw) "Las contraseñas no coinciden." else null

    fun validatePhone(phone: String?): String? =
        if (phone.isNullOrBlank()) null
        else if (!phoneRegex.matches(phone)) "Número inválido." else null

    fun validateGenres(selected: Int) =
        if (selected < 1) "Selecciona al menos un género." else null
}
