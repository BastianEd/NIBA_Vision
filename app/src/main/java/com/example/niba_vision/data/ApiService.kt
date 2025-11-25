package com.example.niba_vision.data

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.GET
import retrofit2.http.PUT

// IMPORTANTE:
// Usa "10.0.2.2" para conectar al "localhost" de tu computadora
// desde el emulador de Android.

    //Si estas en Windows recordar habilitar el firewall para que acepte conexiones entrantes en el puerto 8080
    //Remplazar la IP por la IP local de tu computadora
private const val BASE_URL = "http:192.168.1.22:8080/"

// Creamos el cliente de Retrofit
object ApiClient {
    val instance: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}

// Interfaz que define los endpoints de la API
// Basado en tu AuthController.kt
interface ApiService {

    @POST("api/auth/register")
    suspend fun register(@Body user: UserApiRequest): Response<String> // Devuelve un String ("Usuario registrado")

    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<UserApiResponse> // Devuelve el objeto User

    // Define el endpoint GET para /api/books
    // Devuelve una Lista de tu modelo de datos Book
    @GET("api/books")
    suspend fun getBooks(): List<Book>

    // Agrega esto junto a tus otros métodos (@POST login, @POST register, etc.)
    // Asumimos que el backend acepta una petición PUT a "user" o "update" con los datos nuevos.
    // Ajusta la ruta ("user/update" o la que use tu API real) según corresponda.
    @PUT("user/update")
    suspend fun updateUser(@Body request: UserApiRequest): Response<UserApiResponse>
}