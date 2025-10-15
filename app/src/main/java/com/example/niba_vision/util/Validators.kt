package com.example.niba_vision.util

/**
 * Objeto singleton que contiene funciones de validación para los campos del formulario.
 *
 * Proporciona métodos estáticos para validar nombres, correos electrónicos, contraseñas,
 * teléfonos y la selección de géneros, siguiendo las reglas de negocio de ZONALIBROS.
 */
object Validators {
    // Expresiones regulares para las validaciones.
    private val nameRegex = Regex("^[A-Za-zÁÉÍÓÚáéíóúÑñ ]{1,100}$")
    private val emailRegex = Regex("^[A-Za-z0-9._%+-]+@duoc\\.cl$", RegexOption.IGNORE_CASE)
    private val upper = Regex(".*[A-Z].*")
    private val lower = Regex(".*[a-z].*")
    private val digit = Regex(".*[0-9].*")
    private val special = Regex(".*[@#\\$%].*")
    private val phoneRegex = Regex("^\\+?\\d{7,15}$")

    /**
     * Valida el nombre completo del usuario.
     * @param name El nombre a validar.
     * @return Un mensaje de error si no es válido, o `null` si es correcto.
     */
    fun validateName(name: String) = when {
        name.isBlank() -> "El nombre no puede estar vacío."
        !nameRegex.matches(name.trim()) -> "Solo letras y espacios (máx. 100)."
        else -> null
    }

    /**
     * Valida el correo electrónico, asegurando que sea de dominio @duoc.cl.
     * @param email El correo a validar.
     * @return Un mensaje de error si no es válido, o `null` si es correcto.
     */
    fun validateEmail(email: String) = when {
        email.isBlank() -> "El correo no puede estar vacío."
        email.length > 60 -> "Máx. 60 caracteres."
        !emailRegex.matches(email.trim()) -> "Debe ser un correo @duoc.cl válido."
        else -> null
    }

    /**
     * Valida la complejidad de la contraseña.
     * @param pw La contraseña a validar.
     * @return Una cadena con todos los errores encontrados, o `null` si es válida.
     */
    fun validatePassword(pw: String) = buildList {
        if (pw.length < 10) add("Al menos 10 caracteres.")
        if (!upper.matches(pw)) add("Debe incluir una mayúscula.")
        if (!lower.matches(pw)) add("Debe incluir una minúscula.")
        if (!digit.matches(pw)) add("Debe incluir un número.")
        if (!special.matches(pw)) add("Debe incluir un caracter especial (@#$%).")
    }.takeIf { it.isNotEmpty() }?.joinToString(" ")

    /**
     * Valida que la contraseña de confirmación coincida con la original.
     * @param pw La contraseña original.
     * @param confirm La contraseña de confirmación.
     * @return Un mensaje de error si no coinciden, o `null` si son iguales.
     */
    fun validateConfirmPassword(pw: String, confirm: String) =
        if (confirm != pw) "Las contraseñas no coinciden." else null

    /**
     * Valida el número de teléfono (opcional).
     * @param phone El número de teléfono a validar.
     * @return Un mensaje de error si es inválido, o `null` si es correcto o está vacío.
     */
    fun validatePhone(phone: String?): String? =
        if (phone.isNullOrBlank()) null
        else if (!phoneRegex.matches(phone)) "Número inválido." else null

    /**
     * Valida que se haya seleccionado al menos un género favorito.
     * @param selected El número de géneros seleccionados.
     * @return Un mensaje de error si es menor a 1, o `null` en caso contrario.
     */
    fun validateGenres(selected: Int) =
        if (selected < 1) "Selecciona al menos un género." else null
}