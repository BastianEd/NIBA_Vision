package com.example.niba_vision.data

/**
 * Modelo de datos que representa a un usuario del sistema.
 * Esta es la clase de dominio que se usa en la UI y ViewModels.
 *
 * @property fullName El nombre completo del usuario.
 * @property email El correo electrónico del usuario, usado como identificador único.
 * @property password La contraseña del usuario.
 * @property phone El número de teléfono opcional del usuario.
 * @property favoriteGenres Una lista de los géneros de libros favoritos del usuario.
 * @property profilePictureUri La URI (como String) de la foto de perfil.
 * @property address La dirección de despacho del usuario.
 */
data class User(
    val fullName: String,
    val email: String,
    val password: String,
    val phone: String?,
    val favoriteGenres: List<Genre>,
    val profilePictureUri: String? = null,
    val address: String? = null // <-- CAMBIO: Añadido campo de dirección
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