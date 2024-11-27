package com.aliny.palmpet.ui.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.aliny.palmpet.ui.theme.PalmPetTheme

class Historico : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PalmPetTheme {
                Surface {
                    HistoricoScreen()
                }
            }
        }
    }
}

@Composable
fun HistoricoScreen() {
    Text(text = "Historico")
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview3() {
    PalmPetTheme {
        
    }
}