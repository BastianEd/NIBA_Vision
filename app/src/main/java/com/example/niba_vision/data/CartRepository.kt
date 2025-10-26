package com.example.niba_vision.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

/**
 * Un repositorio singleton (object) que gestiona el estado de la cesta de compra en memoria.
 *
 * Mantiene un mapa de artículos, donde la clave es el ID del libro.
 */
object CartRepository {

    // Un StateFlow privado que contiene el estado actual de la cesta (un mapa de ID de libro a CartItem)
    private val _items = MutableStateFlow<Map<Int, CartItem>>(emptyMap())

    /**
     * Expone públicamente el contenido de la cesta como un StateFlow de solo lectura.
     */
    val cartItems: StateFlow<Map<Int, CartItem>> = _items.asStateFlow()

    /**
     * Un Flow que calcula y emite el número total de artículos en la cesta
     * (sumando las cantidades de todos los artículos).
     */
    fun getCartItemCount(): kotlinx.coroutines.flow.Flow<Int> {
        return cartItems.map { cartMap ->
            cartMap.values.sumOf { it.quantity }
        }
    }

    /**
     * Añade un libro a la cesta.
     * Si el libro ya existe, incrementa su cantidad.
     * Si no, lo añade con cantidad 1.
     */
    fun addToCart(book: Book) {
        _items.update { currentCart ->
            val cart = currentCart.toMutableMap()
            val currentItem = cart[book.id]

            if (currentItem == null) {
                // Añadir nuevo artículo
                cart[book.id] = CartItem(book = book, quantity = 1)
            } else {
                // Incrementar cantidad
                cart[book.id] = currentItem.copy(quantity = currentItem.quantity + 1)
            }
            cart // Devuelve el mapa actualizado
        }
    }

    // (Aquí podrías añadir funciones como removeFromCart, clearCart, etc.)
}