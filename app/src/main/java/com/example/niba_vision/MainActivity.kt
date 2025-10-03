package com.example.niba_vision

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
<<<<<<< HEAD
import androidx.compose.material3.Surface
=======
>>>>>>> ad79c16dca55cda809d176a66b05f96e03cd10ae
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
<<<<<<< HEAD
import com.example.niba_vision.view.theme.NIBA_VisionTheme
import com.example.niba_vision.view.theme.saludo
import org.w3c.dom.Text
=======
import com.example.niba_vision.ui.theme.NIBA_VisionTheme
>>>>>>> ad79c16dca55cda809d176a66b05f96e03cd10ae

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
<<<<<<< HEAD
        setContent {
                NIBA_VisionTheme {
                    //No es como el el ejemplo del profe, no alcance a copiar y luego se quedo en la clase SaludoBienvenida.
                    Surface (modifier = Modifier.fillMaxSize()) {
                        saludo()
                    }
                }
=======
        enableEdgeToEdge()
        setContent {
            NIBA_VisionTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
>>>>>>> ad79c16dca55cda809d176a66b05f96e03cd10ae
        }
    }
}

<<<<<<< HEAD
=======
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    NIBA_VisionTheme {
        Greeting("Android")
    }
}
>>>>>>> ad79c16dca55cda809d176a66b05f96e03cd10ae
