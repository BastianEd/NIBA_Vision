package com.example.niba_vision.data

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

object CartRepository {
    private const val PREF_NAME = "shopping_cart"
    private const val KEY_CART = "cart_items"

    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems.asStateFlow()

    private lateinit var prefs: SharedPreferences
    private val gson = Gson()

    // Inicializamos las preferencias (Llamar en MainActivity)
    fun init(context: Context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        loadCartFromPrefs()
    }

    fun addToCart(book: Book) {
        _cartItems.update { currentItems ->
            val existingItem = currentItems.find { it.book.id == book.id }
            if (existingItem != null) {
                currentItems.map {
                    if (it.book.id == book.id) it.copy(quantity = it.quantity + 1) else it
                }
            } else {
                currentItems + CartItem(book, 1)
            }
        }
        saveCartToPrefs()
    }

    fun removeFromCart(book: Book) {
        _cartItems.update { currentItems ->
            currentItems.filter { it.book.id != book.id }
        }
        saveCartToPrefs()
    }

    fun clearCart() {
        _cartItems.value = emptyList()
        saveCartToPrefs()
    }

    private fun saveCartToPrefs() {
        val json = gson.toJson(_cartItems.value)
        prefs.edit().putString(KEY_CART, json).apply()
    }

    private fun loadCartFromPrefs() {
        val json = prefs.getString(KEY_CART, null)
        if (json != null) {
            try {
                val type = object : TypeToken<List<CartItem>>() {}.type
                val items: List<CartItem> = gson.fromJson(json, type)
                _cartItems.value = items
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}