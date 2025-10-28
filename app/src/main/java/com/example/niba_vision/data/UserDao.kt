package com.example.niba_vision.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao {

    // suspend indica que esta función debe ser llamada desde una corrutina.
    @Insert
    suspend fun registerUser(user: UserEntity)

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun findUserByEmail(email: String): UserEntity?

    // Obtiene el último usuario insertado en la tabla (el más reciente).
    @Query("SELECT * FROM users ORDER BY id DESC LIMIT 1")
    suspend fun getLastRegisteredUser(): UserEntity?
}