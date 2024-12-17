package com.aliny.palmpet.ui.screens

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aliny.palmpet.ui.theme.AzulFontes
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

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 40.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Medicações e Vacinas",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = AzulFontes,
                modifier = Modifier
                    .padding(start = 16.dp)
            )
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = "Adicionar Medicação",
                tint = AzulFontes,
                modifier = Modifier
                    .size(50.dp)
                    .clickable {
                        val intent = Intent(context, AdicionarMedicacao::class.java)
                        context.startActivity(intent)
                    }
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun MedicacoesPreview() {
    PalmPetTheme {
        MedicacoesScreen()
    }
}