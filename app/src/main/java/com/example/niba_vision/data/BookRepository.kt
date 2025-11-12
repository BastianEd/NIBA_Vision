package com.example.niba_vision.data

// Importamos nuestro nuevo cliente de red
import com.example.niba_vision.data.network.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Repositorio de Libros.
 * Antes tenía una lista de libros "quemada" (escrita aquí mismo).
 * ¡Ahora es profesional y los pide a la API!
 */
class BookRepository {

    // 1. Obtenemos a nuestro mensajero (el ApiService que creamos)
    private val apiService = RetrofitClient.apiService

    /**
     * Obtiene la lista de libros desde la API.
     */
    suspend fun getBooks(): List<Book> {
        // 2. 'withContext(Dispatchers.IO)' es MUY importante.
        // Le dice a Kotlin: "Oye, haz esta llamada de red en un hilo
        // secundario (IO), ¡no vayas a bloquear la pantalla principal (UI)!"
        return withContext(Dispatchers.IO) {
            try {
                // 3. ¡Llamamos a la API! 🚀
                // Esto llama a la función en nuestra "carta" (ApiService)
                apiService.getBooks()
            } catch (e: Exception) {
                // 4. ¡Siempre hay que tener un plan B!
                // Si algo falla (la API está apagada, no hay internet),
                // la app no debe crashear 💥.
                // Simplemente imprimimos el error en la consola y devolvemos una lista vacía.
                e.printStackTrace()
                emptyList()
            }
        }
    }
}