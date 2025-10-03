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
                    //No es como el el ejemplo del profe, no alcance a copiar y luego se quedo en la clase SaludoBienvenida.
                    Surface (modifier = Modifier.fillMaxSize()) {
                        saludo()
                    }
                }
        }
    }
}

