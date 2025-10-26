package com.example.niba_vision.data

// Ya no necesitamos R
// import com.example.niba_vision.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

/**
 * Repositorio para gestionar los datos de los libros.
 * AHORA USA URLs para las portadas.
 */
class BookRepository {

    suspend fun getBooks(): List<Book> {
        delay(1000)
        return withContext(Dispatchers.IO) {
            // 👇 HE DEJADO ESPACIOS "TU_LINK_AQUI" PARA QUE PEGUES TUS URLs
            listOf(
                Book(
                    id = 1,
                    title = "Harry Potter y la piedra filosofal",
                    author = "ROWLING, J.K.",
                    price = "$22.900",
                    priceValue = 22900.0,
                    // Reemplaza esto con tu link de Pinterest/Imgur
                    coverImageUrl = "https://i.pinimg.com/1200x/ee/23/df/ee23df3a67f6f27ab8645debc9f6d5e3.jpg", 
                    isNew = true
                ),
                Book(
                    id = 2,
                    title = "Harry Potter y la cámara secreta",
                    author = "ROWLING, J.K.",
                    price = "$23.500",
                    priceValue = 23500.0,
                    coverImageUrl = "https://i.pinimg.com/736x/fe/a1/c8/fea1c802f18a47c29e47a8fcc13e1e7f.jpg", 
                ),
                Book(
                    id = 3,
                    title = "Harry Potter y el prisionero de Azkaban",
                    author = "ROWLING, J.K.",
                    price = "$24.900",
                    priceValue = 24900.0,
                    coverImageUrl = "https://i.pinimg.com/1200x/57/8c/f5/578cf5e990eaeb85ccdadf786006c6a7.jpg",
                ),
                Book(
                    id = 4,
                    title = "Harry Potter y el cáliz de fuego",
                    author = "ROWLING, J.K.",
                    price = "$27.900",
                    priceValue = 27900.0,
                    coverImageUrl = "https://i.pinimg.com/1200x/66/ca/68/66ca681acf564417ac238874ccff97d5.jpg",
                    isNew = true
                ),
                Book(
                    id = 5,
                    title = "Harry Potter y la Orden del Fénix",
                    author = "ROWLING, J.K.",
                    price = "$29.900",
                    priceValue = 29900.0,
                    coverImageUrl = "https://i.pinimg.com/736x/3f/b1/2c/3fb12c0230d31a576c7687ffe894de75.jpg",
                ),
                Book(
                    id = 6,
                    title = "Harry Potter y el misterio del príncipe",
                    author = "ROWLING, J.K.",
                    price = "$30.500",
                    priceValue = 30500.0,
                    coverImageUrl = "https://i.pinimg.com/736x/d5/b0/a9/d5b0a9bc8dd1b2b2ff462c820ac0f1a8.jpg",
                ),
                Book(
                    id = 7,
                    title = "Harry Potter y las Reliquias de la Muerte",
                    author = "ROWLING, J.K.",
                    price = "$32.000",
                    priceValue = 32000.0,
                    coverImageUrl = "https://i.pinimg.com/736x/0c/82/5d/0c825d08fb10e3ee03472de41bae6183.jpg",
                ),
                Book(
                    id = 8,
                    title = "Harry Potter y el legado maldito",
                    author = "ROWLING, J.K.",
                    price = "$25.000",
                    priceValue = 25000.0,
                    coverImageUrl = "https://i.pinimg.com/736x/99/46/d8/9946d822a2e92707bdf3ca47d15ebed1.jpg",
                )
            )
        }
    }
}