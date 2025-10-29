package com.example.niba_vision.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Define la estructura de la tabla 'users' en la base de datos Room.
 * Esta clase representa cómo se almacenan los datos en SQLite.
 */
@Entity(tableName = "users")
data class UserEntity(
    // @PrimaryKey define la clave primaria de la tabla.
    // autoGenerate = true hace que el ID se genere automáticamente.
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val fullName: String,
    val email: String,
    val password: String,
    val phone: String?,
    val favoriteGenres: String, // Almacena la lista de géneros como un String (ej: "FICCION,TERROR")
    val profilePictureUri: String? = null,
    val address: String? = null // <-- CAMBIO: Añadido campo de dirección
)