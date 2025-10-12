package com.example.niba_vision.data

object UserRepository {
    private val usersByEmail = mutableMapOf<String, User>()

    fun register(user: User): Result<Unit> {
        val key = user.email.lowercase()
        return if (usersByEmail.containsKey(key))
            Result.failure(IllegalArgumentException("El correo ya está registrado."))
        else {
            usersByEmail[key] = user
            Result.success(Unit)
        }
    }

    fun login(email: String, password: String): Result<User> {
        val u = usersByEmail[email.lowercase()]
        return if (u != null && u.password == password) Result.success(u)
        else Result.failure(IllegalArgumentException("Credenciales inválidas."))
    }

    fun exists(email: String) = usersByEmail.containsKey(email.lowercase())
}
