package com.example.niba_vision.data

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

// IMPORTANTE:
// Usa "10.0.2.2" para conectar al "localhost" de tu computadora
// desde el emulador de Android.
private const val BASE_URL = "http://10.0.2.2:8080/"

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
}