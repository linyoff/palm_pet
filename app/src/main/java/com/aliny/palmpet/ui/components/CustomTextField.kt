package com.aliny.palmpet.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aliny.palmpet.ui.theme.CinzaContainersClaro

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTextField(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    placeholderText: String,
    modifier: Modifier,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardType: KeyboardType,
    imeAction: ImeAction = ImeAction.Next,
    onImeAction: () -> Unit = {}
) {

    TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(
                text = placeholderText,
                fontSize = 14.sp
            )
        },
        modifier = modifier
            .fillMaxWidth(0.80f)
            .padding(3.dp),
        shape = RoundedCornerShape(17.dp),
        colors = TextFieldDefaults.textFieldColors(
            containerColor = CinzaContainersClaro,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        ),
        visualTransformation = visualTransformation,
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = keyboardType,
            imeAction = imeAction
        ),
        keyboardActions = KeyboardActions(
            onNext = { onImeAction() }
        ),
        singleLine = true
    )
}