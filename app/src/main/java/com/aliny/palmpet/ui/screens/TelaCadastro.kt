package com.aliny.palmpet.ui.screens

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aliny.palmpet.R
import com.aliny.palmpet.data.repository.UserRepository
import com.aliny.palmpet.ui.components.CustomButton
import com.aliny.palmpet.ui.theme.PalmPetTheme
import com.aliny.palmpet.ui.theme.RosaPrincipal
import com.aliny.palmpet.ui.components.CustomDatePicker
import com.aliny.palmpet.ui.components.CustomTextField
import com.aliny.palmpet.ui.components.ImagePicker


class TelaCadastro : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PalmPetTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.White
                ) {
                    TeladeCadastroPreview()
                }
            }
        }
    }
}

@Composable
fun TeladeCadastro() {

    var nomeState by remember { mutableStateOf(TextFieldValue()) }
    var nomeUsuarioState by remember { mutableStateOf(TextFieldValue()) }
    var dataNascState by remember { mutableStateOf("") }
    var telefoneState by remember { mutableStateOf(TextFieldValue()) }
    var emailState by remember { mutableStateOf(TextFieldValue()) }
    var senhaState by remember { mutableStateOf(TextFieldValue()) }
    val context = LocalContext.current

    val focusManager = LocalFocusManager.current
    val focusRequester1 = remember { FocusRequester() }
    val focusRequester2 = remember { FocusRequester() }
    val focusRequester3 = remember { FocusRequester() }
    val focusRequester4 = remember { FocusRequester() }
    val focusRequester5 = remember { FocusRequester() }

    Column (
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ){
        Image(painter = painterResource(id = R.drawable.logopalmpet),
            contentDescription = "Logo",
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .padding(top = 80.dp, bottom = 15.dp)
        )
        Text(
            text = "Faça seu cadastro:",
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(vertical = 8.dp),
            fontSize = 22.sp,
            color = RosaPrincipal
        )
        CustomTextField(
            value = nomeState,
            onValueChange = { nomeState = it },
            placeholderText = "Nome completo",
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .focusRequester(focusRequester1),
            keyboardType = KeyboardType.Text,
            onImeAction = { focusRequester2.requestFocus() }
        )

        CustomTextField(
            value = nomeUsuarioState,
            onValueChange = {
                if(it.text.length<=50){ nomeUsuarioState = it }
            },
            placeholderText = "Nome de usuário",
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .focusRequester(focusRequester2),
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Done,
            onImeAction = { focusManager.clearFocus() }
        )

        CustomDatePicker(
            label = "Data de nascimento",
            selectedDate = dataNascState,
            onDateSelected = { dataNascState = it.text },
            isFutureAllowed = false,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
        )

        CustomTextField(
            value = telefoneState,
            onValueChange = {
                //limitar o comprimento ao número de dígitos do telefone sem a máscara
                val unmaskedText = it.text.filter { char -> char.isDigit() }
                if (unmaskedText.length <= 11) { //considerando 11 dígitos (DDD + número)
                    telefoneState = it
                }
            },
            placeholderText = "Telefone",
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .focusRequester(focusRequester3),
            keyboardType = KeyboardType.Phone,
            onImeAction = { focusRequester4.requestFocus() }
        )


        CustomTextField(value = emailState,
            onValueChange = { emailState = it },
            placeholderText = "Email",
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .focusRequester(focusRequester4),
            keyboardType = KeyboardType.Email,
            onImeAction = { focusRequester5.requestFocus() }
        )

        CustomTextField(
            value = senhaState,
            onValueChange = { senhaState = it },
            placeholderText = "Senha",
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .focusRequester(focusRequester5),
            visualTransformation = PasswordVisualTransformation(),
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done,
            onImeAction = { focusManager.clearFocus() }
        )

        //método que irá inserir a foto
        var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

        ImagePicker(
            onImageSelected = { uri -> selectedImageUri = uri }
        )

        CustomButton(
            onClickAction = {
                UserRepository.setUser(
                    nomeUsuarioState.text,
                    nomeState.text,
                    dataNascState,
                    telefoneState.text,
                    emailState.text,
                    senhaState.text,
                    selectedImageUri,
                    context
                )
            },
            modifier = Modifier
                .align(Alignment.CenterHorizontally),
            text = "Cadastrar"
        )
    }
}


@Preview(showBackground = true)
@Composable
fun TeladeCadastroPreview() {
    PalmPetTheme {
        TeladeCadastro()
    }
}






