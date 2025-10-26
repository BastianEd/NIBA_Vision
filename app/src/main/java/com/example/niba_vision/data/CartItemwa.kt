package com.example.niba_vision.data

/**
 * Modelo que representa un artículo dentro de la cesta de compra.
 * Combina un [Book] con una [quantity].
 */
data class CartItem(
    val book: Book,
    val quantity: Int
)