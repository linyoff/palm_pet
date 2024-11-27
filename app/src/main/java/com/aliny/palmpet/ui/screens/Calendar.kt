package com.aliny.palmpet.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.aliny.palmpet.ui.theme.PalmPetTheme

@Composable
fun Calendar(){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ){
        Text(text = "Calendário")
    }
}

@Preview(showBackground = true)
@Composable
fun CalendarPreview() {
    PalmPetTheme {
        androidx.compose.material3.Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color.White
        ) {
            Calendar()
        }
    }
}