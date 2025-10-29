package com.example.niba_vision.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// --- CAMBIO 1: Versión incrementada de 2 a 3 ---
// Esto le dice a Room que la estructura ha cambiado.
@Database(entities = [UserEntity::class], version = 3, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

    // El 'companion object' permite acceder a los métodos sin instanciar la clase.
    // Esto crea un Singleton para asegurar que solo haya una instancia de la BD.
    companion object {
        // @Volatile asegura que el valor de INSTANCE esté siempre actualizado
        // y sea visible para todos los hilos de ejecución.
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            // 'synchronized' asegura que solo un hilo a la vez pueda
            // ejecutar este bloque, evitando que se creen dos instancias de la BD
            // si dos hilos acceden a esto al mismo tiempo.
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "zonalibros_database"
                )
                    // --- CAMBIO 2: 'false' cambiado a 'true' ---
                    // Esto permite a Room DESTRUIR la base de datos antigua y crear una nueva
                    // si no encuentra una migración. Es la solución más fácil para desarrollo.
                    .fallbackToDestructiveMigration(true)
                    .build()
                INSTANCE = instance
                // Devuelve la instancia recién creada
                instance
            }
        }
    }
}