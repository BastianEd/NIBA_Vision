package com.example.niba_vision.data

import androidx.room.Entity
import androidx.room.PrimaryKey

// La anotación @Entity le dice a Room que esta clase representa una tabla.
@Entity(tableName = "users")
data class UserEntity(
    // @PrimaryKey define la clave primaria de la tabla.
    // autoGenerate = true hace que el ID se genere automáticamente.
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val fullName: String,
    val email: String,
    val password: String,
    val phone: String?,
    val favoriteGenres: String, // Almacenaremos los géneros como un String simple
    val profilePictureUri: String? = null
)