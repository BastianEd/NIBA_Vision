package com.example.niba_vision.data

data class User(
    val fullName: String,
    val email: String,
    val password: String,
    val phone: String?,
    val favoriteGenres: List<Genre>
)

enum class Genre { FICCION, NO_FICCION, MISTERIO, TERROR, SUSPENSO, HISTORIA }
