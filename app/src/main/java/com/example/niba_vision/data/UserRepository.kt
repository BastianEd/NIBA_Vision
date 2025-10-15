package com.example.niba_vision.data

/**
 * Repositorio singleton para gestionar los datos de los usuarios en memoria.
 *
 * Se encarga de operaciones como registrar, autenticar y verificar la existencia de usuarios.
 * Utiliza un `mutableMapOf` para simular una base de datos en memoria donde la clave es el
 * correo electrónico del usuario en minúsculas para evitar duplicados por mayúsculas/minúsculas.
 */
object UserRepository {
    private val usersByEmail = mutableMapOf<String, User>()

    /**
     * Registra un nuevo usuario en el sistema.
     *
     * @param user El objeto [User] que se va a registrar.
     * @return Un [Result] que será [Result.success] si el registro fue exitoso,
     * o [Result.failure] con una [IllegalArgumentException] si el correo ya existe.
     */
    fun register(user: User): Result<Unit> {
        val key = user.email.lowercase()
        return if (usersByEmail.containsKey(key))
            Result.failure(IllegalArgumentException("El correo ya está registrado."))
        else {
            usersByEmail[key] = user
            Result.success(Unit)
        }
    }

    /**
     * Autentica a un usuario mediante su correo y contraseña.
     *
     * @param email El correo electrónico del usuario.
     * @param password La contraseña del usuario.
     * @return Un [Result] que contiene el [User] si las credenciales son correctas,
     * o [Result.failure] si las credenciales son inválidas.
     */
    fun login(email: String, password: String): Result<User> {
        val u = usersByEmail[email.lowercase()]
        return if (u != null && u.password == password) Result.success(u)
        else Result.failure(IllegalArgumentException("Credenciales inválidas."))
    }

    /**
     * Comprueba si un usuario ya existe en el repositorio.
     *
     * @param email El correo electrónico a verificar.
     * @return `true` si el correo ya está registrado, `false` en caso contrario.
     */
    fun exists(email: String) = usersByEmail.containsKey(email.lowercase())
}