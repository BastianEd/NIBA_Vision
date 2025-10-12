package com.example.niba_vision

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.example.niba_vision.ui.AppNavHost
import com.example.niba_vision.view.theme.NIBA_VisionTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NIBA_VisionTheme {
                App()
            }
        }
    }
}

@Composable
fun App() {
    val nav = rememberNavController()
    AppNavHost(nav)
}
