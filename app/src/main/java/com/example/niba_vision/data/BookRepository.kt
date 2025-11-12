package com.example.niba_vision.data

// Importamos nuestro nuevo cliente de red
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class BookRepository(private val apiService: ApiService) {
// -----------------

    /**
     * Obtiene la lista de libros desde la API.
     */
    suspend fun getBooks(): List<Book> {
        return withContext(Dispatchers.IO) {
            try {
                // Llama a la función del 'apiService' que recibimos
                apiService.getBooks()
                // -----------------
            } catch (e: Exception) {
                e.printStackTrace()
                emptyList()
            }
        }
    }
}