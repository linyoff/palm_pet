package com.aliny.palmpet.ui.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aliny.palmpet.data.repository.UserRepository
import com.aliny.palmpet.ui.components.CustomButton
import com.aliny.palmpet.ui.components.CustomTextField
import com.aliny.palmpet.ui.theme.PalmPetTheme

class EditPassword : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PalmPetTheme {
                Surface {
                    EditPasswordScreen()
                }
            }
        }
    }
}

@Composable
fun EditPasswordScreen() {
    var currentPassword by remember { mutableStateOf(TextFieldValue("")) }
    var newPassword by remember { mutableStateOf(TextFieldValue("")) }
    var confirmPassword by remember { mutableStateOf(TextFieldValue("")) }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Alterar Senha",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
                .padding(top = 35.dp),
            fontSize = 22.sp,
        )

        //senha atual
        CustomTextField(
            value = currentPassword,
            onValueChange = { currentPassword = it },
            placeholderText = "Senha Atual",
            modifier = Modifier.fillMaxWidth(),
            keyboardType = KeyboardType.Password,
            visualTransformation = PasswordVisualTransformation()
        )

        //nova senha
        CustomTextField(
            value = newPassword,
            onValueChange = { newPassword = it },
            placeholderText = "Nova Senha",
            modifier = Modifier.fillMaxWidth(),
            keyboardType = KeyboardType.Password,
            visualTransformation = PasswordVisualTransformation()
        )

        //confirmar a nova senha
        CustomTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            placeholderText = "Confirmar Nova Senha",
            modifier = Modifier.fillMaxWidth(),
            keyboardType = KeyboardType.Password,
            visualTransformation = PasswordVisualTransformation()
        )

        //salvar as alterações
        CustomButton(
            onClickAction = {
                UserRepository.updateUserPassword(
                    currentPassword = currentPassword.text,
                    context = context,
                    newPassword = newPassword.text
                )
            },
            modifier = Modifier.fillMaxWidth(),
            text = "Salvar Alterações"
        )
    }
}

@Preview(showBackground = true)
@Composable
fun EditPasswordScreenPreview() {
    PalmPetTheme {
        EditPasswordScreen()
    }
}
