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
    // Se usa MutableStateFlow para poder actualizar su valor.
    private val _items = MutableStateFlow<Map<Int, CartItem>>(emptyMap())

    /**
     * Expone públicamente el contenido de la cesta como un StateFlow de solo lectura (inmutable).
     * Las Vistas (UI) observarán este flow.
     */
    val cartItems: StateFlow<Map<Int, CartItem>> = _items.asStateFlow()

    /**
     * Un Flow que calcula y emite el número total de artículos en la cesta
     * (sumando las cantidades de todos los artículos, no los tipos de libro).
     */
    fun getCartItemCount(): kotlinx.coroutines.flow.Flow<Int> {
        // 'map' transforma el flujo del mapa de items en un flujo de un solo número (Int)
        return cartItems.map { cartMap ->
            cartMap.values.sumOf { it.quantity } // Suma la 'quantity' de cada 'CartItem'
        }
    }

    /**
     * Añade un libro a la cesta.
     * Si el libro ya existe, incrementa su cantidad.
     * Si no, lo añade con cantidad 1.
     * Esta operación es atómica gracias a 'update'.
     */
    fun addToCart(book: Book) {
        // 'update' garantiza que la modificación del estado sea segura en concurrencia (thread-safe)
        _items.update { currentCart ->
            // Se crea una copia mutable del mapa actual
            val cart = currentCart.toMutableMap()
            // Se busca si el item ya existe usando el ID del libro como clave
            val currentItem = cart[book.id]

            if (currentItem == null) {
                // Si no existe, se añade un nuevo 'CartItem' con cantidad 1
                cart[book.id] = CartItem(book = book, quantity = 1)
            } else {
                // Si ya existe, se crea una copia del item con la cantidad incrementada
                cart[book.id] = currentItem.copy(quantity = currentItem.quantity + 1)
            }
            cart // Devuelve el mapa actualizado, que se convertirá en el nuevo estado de '_items'
        }
    }

    /**
     * Limpia la cesta de compra (después de un pago exitoso).
     * Reemplaza el mapa actual por un mapa vacío.
     * ESTA ES LA FUNCIÓN QUE FALTABA.
     */
    fun clearCart() {
        _items.update { emptyMap() }
    }
}