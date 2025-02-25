package com.aliny.palmpet.ui.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.aliny.palmpet.data.repository.UserRepository
import com.aliny.palmpet.ui.components.BackButton
import com.aliny.palmpet.ui.components.CustomButton
import com.aliny.palmpet.ui.components.CustomTextField
import com.aliny.palmpet.ui.theme.AzulFontes
import com.aliny.palmpet.ui.theme.PalmPetTheme
import com.aliny.palmpet.viewmodel.UserViewModel

class EditEmail : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PalmPetTheme {
                Surface{
                    EditEmailScreen()
                }
            }
        }
    }
}

@Composable
fun EditEmailScreen(
    userViewModel: UserViewModel = viewModel()
) {
    var newEmail by remember { mutableStateOf(TextFieldValue("")) }
    var password by remember { mutableStateOf(TextFieldValue("")) }
    val userData by userViewModel.userData.observeAsState()
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(25.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        BackButton(
            onBackClick = { (context as? ComponentActivity)?.finish() },
            iconColor = AzulFontes,
            iconSize = 45
        )

        Text(
            text = "Alterar E-mail",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
                .padding(top = 35.dp),
            fontSize = 22.sp,
        )

        CustomTextField(
            value = newEmail,
            onValueChange = { newEmail = it },
            placeholderText = "Novo E-mail",
            modifier = Modifier.fillMaxWidth(),
            keyboardType = KeyboardType.Email
        )

        CustomTextField(
            value = password,
            onValueChange = { password = it },
            placeholderText = "Senha",
            modifier = Modifier.fillMaxWidth(),
            keyboardType = KeyboardType.Password,
            visualTransformation = PasswordVisualTransformation()
        )

        //salvar as alterações
        CustomButton(
            onClickAction = {
                userData?.let { user ->
                    UserRepository.updateUserEmail(
                        uidUser = user.id_usuario,
                        newEmail = newEmail.text,
                        password = password.text,
                        context = context
                    )
                }
            },
            modifier = Modifier.fillMaxWidth(),
            text = "Salvar Alterações")
    }
}

@Preview(showBackground = true)
@Composable
fun EditEmailScreenPreview() {
    PalmPetTheme {
        EditEmailScreen()
    }
}
