package com.example.niba_vision.data

/**
 * Modelo de datos que representa a un usuario del sistema.
 *
 * @property fullName El nombre completo del usuario.
 * @property email El correo electrónico del usuario, usado como identificador único.
 * @property password La contraseña del usuario.
 * @property phone El número de teléfono opcional del usuario.
 * @property favoriteGenres Una lista de los géneros de libros favoritos del usuario.
 */
data class User(
    val fullName: String,
    val email: String,
    val password: String,
    val phone: String?,
    val favoriteGenres: List<Genre>
)

/**
 * Enumeración que define los géneros de libros disponibles.
 *
 * Estos valores se utilizan en el formulario de registro para que el usuario
 * seleccione sus preferencias.
 */
enum class Genre {
    FICCION,
    NO_FICCION,
    MISTERIO,
    TERROR,
    SUSPENSO,
    HISTORIA
}