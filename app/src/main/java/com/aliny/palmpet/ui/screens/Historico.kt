package com.aliny.palmpet.ui.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aliny.palmpet.ui.components.BackButton
import com.aliny.palmpet.ui.theme.AzulFontes
import com.aliny.palmpet.ui.theme.CinzaContainersClaro
import com.aliny.palmpet.ui.theme.PalmPetTheme
import com.aliny.palmpet.ui.theme.VermelhoAlertas

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

    val context = LocalContext.current

    Column (
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ){
        BackButton(
            onBackClick = { (context as? ComponentActivity)?.finish() },
            iconColor = AzulFontes,
            iconSize = 45
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .padding(bottom = 16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = "Historico",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = AzulFontes
            )

            Icon(
                imageVector = Icons.Filled.FileDownload,
                contentDescription = "Baixar histórico",
                tint = AzulFontes,
                modifier = Modifier
                    .size(45.dp)
                    .clickable {

                    }
            )
        }

        ItemHistorico(
            titulo = "Entrega de exames",
            data = "04 de março de 2024",
            tipo = "Consulta"
        )
    }
}

@Composable
fun ItemHistorico(titulo: String, data: String, tipo: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .background(CinzaContainersClaro, RoundedCornerShape(12.dp))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        //coluna com os textos
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = titulo,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = AzulFontes
            )
            Text(
                text = data,
                fontSize = 14.sp,
                color = VermelhoAlertas
            )
            Text(
                text = tipo,
                fontSize = 14.sp,
                color = AzulFontes
            )
        }

        //icone de download
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.clickable {

            }
        ) {
            Icon(
                imageVector = Icons.Filled.FileDownload,
                contentDescription = "Resultado",
                tint = AzulFontes,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "resultado",
                fontSize = 12.sp,
                color = AzulFontes
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HistoricoPreview() {
    PalmPetTheme {
        HistoricoScreen()
    }
}