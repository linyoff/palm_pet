package com.aliny.palmpet.ui.components

import android.app.Activity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.aliny.palmpet.ui.theme.AzulFontes

@Composable
fun BackButton(
    modifier: Modifier = Modifier,
    onBackClick: (() -> Unit)? = null, //callback opcional para customização
    iconColor: Color = AzulFontes,
    iconSize: Int = 45
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .padding(bottom = 16.dp, top = 30.dp)
            .clickable {
                onBackClick?.invoke() ?: run {
                    //finaliza a activity atual caso o callback não seja fornecido
                    val activity = (modifier as? Activity)
                    activity?.finish()
                }
            }
    ) {
        Icon(
            imageVector = Icons.Filled.ArrowBack,
            contentDescription = "Voltar",
            tint = iconColor,
            modifier = Modifier.size(iconSize.dp)
        )
    }
}
