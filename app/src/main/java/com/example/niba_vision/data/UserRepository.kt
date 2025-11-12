package com.example.niba_vision.data

import java.io.IOException

/**
 * Repositorio que gestiona los datos del usuario, AHORA interactuando
 * con la API remota a través de ApiService.
 *
 * @property apiService El cliente de Retrofit para hacer las llamadas de red.
 */
class UserRepository(private val apiService: ApiService) {

    /**
     * Autentica a un usuario contra la API.
     */
    suspend fun login(email: String, password: String): Result<User> {
        try {
            val loginRequest = LoginRequest(email, password)
            val response = apiService.login(loginRequest)

            if (response.isSuccessful) {
                val apiUser = response.body() ?: return Result.failure(Exception("Respuesta vacía del servidor."))

                // Convertimos la respuesta de la API (UserApiResponse)
                // a nuestro modelo de dominio de la app (User)
                val genres = if (apiUser.favoriteGenres.isNullOrBlank()) {
                    emptyList()
                } else {
                    // Convertimos "FICCION,TERROR" a List<Genre>
                    apiUser.favoriteGenres.split(",").map { Genre.valueOf(it.trim()) }
                }

                val domainUser = User(
                    fullName = apiUser.fullName,
                    email = apiUser.email,
                    password = "", // No guardamos la contraseña post-login
                    phone = apiUser.phone,
                    favoriteGenres = genres,
                    profilePictureUri = apiUser.profilePictureUri,
                    address = apiUser.address
                )
                return Result.success(domainUser)
            } else {
                // Error de la API (ej: 401 Credenciales inválidas)
                return Result.failure(Exception("Credenciales inválidas (Error ${response.code()})"))
            }
        } catch (e: IOException) {
            // Error de red (ej: no se puede conectar al servidor 10.0.2.2)
            return Result.failure(Exception("Error de red: ${e.message}"))
        } catch (e: Exception) {
            // Otro error (ej: JSON malformado, etc.)
            return Result.failure(Exception("Error inesperado: ${e.message}"))
        }
    }

    /**
     * Registra un nuevo usuario en la API.
     * Convierte el [User] (modelo de dominio) a [UserApiRequest] (DTO de red).
     */
    suspend fun registerUserApi(user: User): Result<Unit> {
        try {
            // Convertir del modelo de dominio (app) al DTO (API)
            val apiRequest = UserApiRequest(
                fullName = user.fullName,
                email = user.email,
                password = user.password,
                phone = user.phone,
                address = user.address,
                profilePictureUri = user.profilePictureUri,
                // Convertimos List<Genre> a "FICCION,TERROR"
                favoriteGenres = user.favoriteGenres.joinToString(",") { it.name }
            )

            val response = apiService.register(apiRequest)

            return if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                // Error de la API (ej: 400 Correo ya existe)
                val errorBody = response.errorBody()?.string() ?: "Error desconocido"
                Result.failure(Exception("Error de registro: $errorBody"))
            }
        } catch (e: IOException) {
            return Result.failure(Exception("Error de red: ${e.message}"))
        } catch (e: Exception) {
            return Result.failure(Exception("Error inesperado: ${e.message}"))
        }
    }

    // La función 'exists' ya no es necesaria localmente, la API se encarga
    // de verificar duplicados durante el registro.
}