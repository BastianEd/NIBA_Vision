package com.example.niba_vision.data

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object SessionManager {
    private const val PREF_NAME = "user_session"
    private const val KEY_USER = "logged_in_user"

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    private lateinit var prefs: SharedPreferences
    private val gson = Gson()

    // Inicializamos las preferencias (Llamar en MainActivity)
    fun init(context: Context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        loadUserFromPrefs()
    }

    fun login(user: User) {
        _currentUser.value = user
        saveUserToPrefs(user)
    }

    fun logout() {
        _currentUser.value = null
        prefs.edit().remove(KEY_USER).apply()
    }

    private fun saveUserToPrefs(user: User) {
        val json = gson.toJson(user)
        prefs.edit().putString(KEY_USER, json).apply()
    }

    private fun loadUserFromPrefs() {
        val json = prefs.getString(KEY_USER, null)
        if (json != null) {
            try {
                val user = gson.fromJson(json, User::class.java)
                _currentUser.value = user
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}