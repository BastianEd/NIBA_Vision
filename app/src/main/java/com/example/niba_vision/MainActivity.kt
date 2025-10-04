package com.example.niba_vision

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.niba_vision.view.theme.NIBA_VisionTheme
import com.example.niba_vision.view.theme.saludo
import org.w3c.dom.Text

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NIBA_VisionTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // Surface ahora usa el padding proporcionado por Scaffold
                    Surface(modifier = Modifier.padding(innerPadding)) {
                        saludo()
                    }
                }
            }
        }
    }
}

