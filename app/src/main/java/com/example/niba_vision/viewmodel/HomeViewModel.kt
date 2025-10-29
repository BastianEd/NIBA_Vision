package com.example.niba_vision.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.niba_vision.data.Book
import com.example.niba_vision.data.BookRepository
import com.example.niba_vision.data.CartRepository
import com.example.niba_vision.data.CartItem // <-- CAMBIO: Importa CartItem
import com.example.niba_vision.ui.screens.home.HomeRoute
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Data class que representa el estado completo de la UI de la pantalla Home.
 */
data class HomeUiState(
    val books: List<Book> = emptyList(),
    val isLoading: Boolean = true,
    val cartItemCount: Int = 0, // Para la insignia (badge)
    val selectedScreen: HomeRoute = HomeRoute.Home,
    val cartItems: Map<Int, CartItem> = emptyMap(), // <-- CAMBIO: Expone la lista de items
    val showPaymentSuccessDialog: Boolean = false // <-- CAMBIO: Controla el pop-up de pago
)

/**
 * ViewModel para la pantalla Home (que incluye Home, Cart y Profile).
 */
class HomeViewModel(
    private val bookRepository: BookRepository,
    private val cartRepository: CartRepository
) : ViewModel() {

    // Flujo de estado privado y mutable
    private val _uiState = MutableStateFlow(HomeUiState())
    // Flujo de estado público e inmutable
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadBooks()
        listenToCartCount()
        listenToCartItems() // <-- CAMBIO: Inicia la escucha de la lista de items
    }

    // Carga la lista de libros desde el repositorio
    private fun loadBooks() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val books = bookRepository.getBooks()
            _uiState.update { it.copy(books = books, isLoading = false) }
        }
    }

    // Escucha el contador de items para la insignia (badge)
    private fun listenToCartCount() {
        viewModelScope.launch {
            cartRepository.getCartItemCount().collect { count ->
                _uiState.update { it.copy(cartItemCount = count) }
            }
        }
    }

    /**
     * Observa la lista completa de items del carrito y la actualiza en el UiState.
     * Esto permite que la pantalla CartScreen reaccione a los cambios.
     */
    private fun listenToCartItems() {
        viewModelScope.launch {
            cartRepository.cartItems.collect { items ->
                _uiState.update { it.copy(cartItems = items) }
            }
        }
    }

    /**
     * Evento llamado desde la UI (BookCard) para añadir un libro al carrito.
     * Se ejecuta en el hilo de IO (fondo).
     */
    fun addToCart(book: Book) {
        viewModelScope.launch(Dispatchers.IO) {
            cartRepository.addToCart(book)
        }
    }

    /**
     * Evento llamado al presionar "Pagar" en la CartScreen.
     * Limpia el carrito y activa el estado para mostrar el diálogo de éxito.
     */
    fun onPaymentClicked() {
        viewModelScope.launch(Dispatchers.IO) {
            cartRepository.clearCart() // Vacía el carrito
            _uiState.update { it.copy(showPaymentSuccessDialog = true) } // Muestra el pop-up
        }
    }

    /**
     * Cierra el diálogo de "Pago Exitoso" (pop-up).
     */
    fun dismissPaymentDialog() {
        _uiState.update { it.copy(showPaymentSuccessDialog = false) }
    }

    /**
     * Evento para cambiar la pestaña de navegación inferior (Home, Cart, Profile).
     */
    fun onHomeNavigationSelected(route: HomeRoute) {
        _uiState.update { it.copy(selectedScreen = route) }
    }
}