package com.example.niba_vision.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.niba_vision.data.Book
import com.example.niba_vision.data.BookRepository
import com.example.niba_vision.data.CartRepository
import com.example.niba_vision.ui.screens.home.HomeRoute // <-- AÑADIDO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class HomeUiState(
    val books: List<Book> = emptyList(),
    val isLoading: Boolean = true,
    val cartItemCount: Int = 0,
    val selectedScreen: HomeRoute = HomeRoute.Home // <-- AÑADIDO: Estado de la pestaña
)

class HomeViewModel(
    private val bookRepository: BookRepository,
    private val cartRepository: CartRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadBooks()
        listenToCartCount()
    }

    private fun loadBooks() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val books = bookRepository.getBooks()
            _uiState.update { it.copy(books = books, isLoading = false) }
        }
    }

    private fun listenToCartCount() {
        viewModelScope.launch {
            cartRepository.getCartItemCount().collect { count ->
                _uiState.update { it.copy(cartItemCount = count) }
            }
        }
    }

    fun addToCart(book: Book) {
        viewModelScope.launch(Dispatchers.IO) {
            cartRepository.addToCart(book)
        }
    }

    /**
     * Evento para cambiar la pestaña de navegación inferior.
     */
    fun onHomeNavigationSelected(route: HomeRoute) {
        _uiState.update { it.copy(selectedScreen = route) }
    }
}