package com.example.niba_vision

import com.example.niba_vision.data.Book
import com.example.niba_vision.data.CartRepository
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class CartRepositoryTest {

    // Datos de prueba falsos
    private val fakeBook = Book(
        id = 1,
        title = "Libro de Test",
        author = "Tester",
        price = "$10.000",
        priceValue = 10000.0,
        coverImageUrl = "http://fake.url/img.jpg",
        isNew = false
    )

    // Al ser un Singleton, el estado se mantiene entre tests.
    // Es vital limpiarlo antes de cada prueba.
    @Before
    fun setup() {
        CartRepository.clearCart()
    }

    @Test
    fun add_item_to_cart_increases_size() {
        // Acción: Agregar libro al carrito
        CartRepository.addToCart(fakeBook)

        // Verificación: El mapa debe tener 1 elemento
        val items = CartRepository.cartItems.value
        assertEquals(1, items.size)

        // Verificación: La cantidad de ese libro debe ser 1
        assertEquals(1, items[1]?.quantity)
    }

    @Test
    fun add_same_item_twice_increases_quantity() {
        // Acción: Agregar el mismo libro dos veces
        CartRepository.addToCart(fakeBook)
        CartRepository.addToCart(fakeBook)

        val items = CartRepository.cartItems.value

        // El tamaño del mapa sigue siendo 1 (mismo producto)
        assertEquals(1, items.size)
        // Pero la cantidad debe ser 2
        assertEquals(2, items[1]?.quantity)
    }

    @Test
    fun clear_cart_removes_all_items() {
        // Pre-condición: Llenar carrito
        CartRepository.addToCart(fakeBook)

        // Acción: Vaciar carrito
        CartRepository.clearCart()

        // Verificación
        val items = CartRepository.cartItems.value
        assertTrue("El carrito debería estar vacío", items.isEmpty())
    }
}