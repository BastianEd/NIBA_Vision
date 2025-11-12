package com.example.niba_vision.data

/**
 * Modelo de datos que representa un libro.
 *
 * @param coverImageUrl AHORA ES UN STRING (URL) para la portada.
 */
data class Book(
    val id: Int,
    val title: String?,
    val author: String?,
    val price: String?,
    val priceValue: Double?,
    val coverImageUrl: String?,
    val isNew: Boolean = false
)