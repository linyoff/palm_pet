package com.aliny.palmpet.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.aliny.palmpet.ui.theme.AzulFontes
import com.aliny.palmpet.ui.theme.CianoBotoes

@Composable
fun CustomButton(
    onClickAction: () -> Unit,
    modifier: Modifier,
    text: String
){
    Button(
        onClick = onClickAction,
        modifier = modifier
            .padding(16.dp),
        shape = RoundedCornerShape(15.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = CianoBotoes,
            contentColor = AzulFontes
        ),
        border = BorderStroke(0.3.dp, AzulFontes)
    ) {
        Text(text)
    }
}
