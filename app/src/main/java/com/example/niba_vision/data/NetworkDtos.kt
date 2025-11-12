package com.example.niba_vision.data

// Coincide con LoginRequest.kt de la API
data class LoginRequest(
    val email: String,
    val password: String
)

// Coincide con el User.kt de la API (lo que se envía al registrar)
// Usamos @SerializedName si los nombres de variable no coinciden
data class UserApiRequest(
    val fullName: String,
    val email: String,
    val password: String,
    val phone: String?,
    val address: String?,
    val profilePictureUri: String?,
    val favoriteGenres: String? // La API espera un String, ej: "FICCION,TERROR"
)

// Coincide con el User.kt de la API (lo que se recibe al hacer login)
// Nota: La API no devuelve la contraseña, lo cual es correcto.
data class UserApiResponse(
    val id: Long,
    val fullName: String,
    val email: String,
    val phone: String?,
    val address: String?,
    val profilePictureUri: String?,
    val favoriteGenres: String?
)