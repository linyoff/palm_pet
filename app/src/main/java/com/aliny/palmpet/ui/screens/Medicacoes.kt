package com.aliny.palmpet.ui.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.aliny.palmpet.ui.theme.PalmPetTheme

class Medicacoes : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PalmPetTheme {
                Surface{
                    MedicacoesScreen()
                }
            }
        }
    }
}

@Composable
fun MedicacoesScreen() {
    Text(
        text = "Medicações"
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    PalmPetTheme {

    }
}