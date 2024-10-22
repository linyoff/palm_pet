package com.aliny.palmpet.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.aliny.palmpet.ui.components.CustomOutlinedTextField
import com.aliny.palmpet.ui.theme.PalmPetTheme

@Composable
fun Search() {
    var searchText by remember { mutableStateOf(TextFieldValue()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        CustomOutlinedTextField(
            value = searchText,
            onValueChange = { searchText = it },
            placeholderText = "Buscar...",
            modifier = Modifier.fillMaxWidth(),
            keyboardType = KeyboardType.Text
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SearchPreview() {
    PalmPetTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color.White
        ) {
            Search()
        }
    }
}
