package com.aliny.palmpet.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aliny.palmpet.ui.theme.AzulFontes
import com.aliny.palmpet.ui.theme.PalmPetTheme

@Composable
fun Calendar() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Calendário",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = AzulFontes
        )

        InteractiveCalendar(
            month = "Dezembro",
            onDateClick = { clickedDate ->
                println("Data selecionada: $clickedDate")
            }
        )
    }
}

@Composable
fun InteractiveCalendar(
    month: String,
    days: List<String> = listOf("D", "S", "T", "Q", "Q", "S", "S"),
    dates: List<Int?> = listOf(
        null, null, null, null, null, null, 1, 2, 3, 4, 5, 6,
        7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21,
        22, 23, 24, 25, 26, 27, 28, 29, 30, 31, null, null
    ),
    onDateClick: (Int) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = month,
            color = Color(0xFF3A3A3A),
            modifier = Modifier.padding(bottom = 8.dp)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(8.dp)
        ) {
            // Cabeçalho com os dias da semana
            items(days) { day ->
                Text(
                    text = day,
                    modifier = Modifier
                        .padding(4.dp)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    color = Color.Gray
                )
            }
            // Adicionando os números do calendário
            items(dates) { date ->
                Box(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .padding(4.dp)
                        .clickable(enabled = date != null) {
                            date?.let { onDateClick(it) }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    if (date != null) {
                        Text(
                            text = date.toString(),
                            color = Color.DarkGray
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CalendarPreview() {
    PalmPetTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color.White
        ) {
            Calendar()
        }
    }
}