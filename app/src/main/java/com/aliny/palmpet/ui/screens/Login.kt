package com.aliny.palmpet.ui.screens

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aliny.palmpet.R
import com.aliny.palmpet.data.auth.AuthService
import com.aliny.palmpet.ui.theme.AzulFontes
import com.aliny.palmpet.ui.theme.CianoBotoes
import com.aliny.palmpet.ui.components.CustomButton
import com.aliny.palmpet.ui.components.CustomTextField
import com.aliny.palmpet.ui.theme.PalmPetTheme
import com.aliny.palmpet.ui.theme.RosaPrincipal


class Login : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PalmPetTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.White
                ) {
                    LoginScreen()
                }
            }
        }
    }
}

@Composable
fun LoginScreen() {
    var emailState by remember { mutableStateOf(TextFieldValue()) }
    var senhaState by remember { mutableStateOf(TextFieldValue()) }
    val context = LocalContext.current

    val focusManager = LocalFocusManager.current
    val focusRequester1 = remember { FocusRequester() }
    val focusRequester2 = remember { FocusRequester() }

    Column (
        modifier = Modifier
            .fillMaxSize()
    ){
        Image(painter = painterResource(id = R.drawable.logopalmpet),
            contentDescription = "Logo",
            modifier = Modifier
                .fillMaxWidth()
                .height(320.dp)
                .padding(top = 100.dp, bottom = 15.dp)
        )
        Text(
            text = "Bem-vindo!",
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(vertical = 10.dp),
            fontSize = 27.sp,
            color = AzulFontes,
        )
        Text(
            text = "Faça o login: ",
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(start = 0.dp, top = 0.dp, end = 0.dp, bottom = 20.dp),
            fontSize = 20.sp,
            color = RosaPrincipal
        )
        CustomTextField(
            value = emailState,
            onValueChange = {emailState = it},
            placeholderText = "Email",
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .focusRequester(focusRequester1),
            keyboardType = KeyboardType.Text,
            onImeAction = { focusRequester2.requestFocus() }
        )
        CustomTextField(
            value = senhaState,
            onValueChange = {senhaState = it},
            placeholderText = "Senha",
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .focusRequester(focusRequester2),
            visualTransformation = PasswordVisualTransformation(),
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done,
            onImeAction = { focusManager.clearFocus() }
        )
        CustomButton(onClickAction = { AuthService.loginComEmailESenha(emailState.text, senhaState.text, context)},
            modifier = Modifier
                .align(Alignment.CenterHorizontally),
            text = "Login"
        )
        Text(
            text = "OU",
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 15.dp),
            fontSize = 15.sp,
            color = AzulFontes
        )
        Row (
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
        ){
            Text(
                text = "Ainda não possui uma conta?",
                fontSize = 15.sp,
                modifier = Modifier
                    .padding(end = 10.dp)
            )
            ClickableText(
                text = AnnotatedString("Cadastre-se"),
                style = TextStyle(color = AzulFontes),
                onClick = {
                    context.startActivity(Intent(context, TelaCadastro::class.java))
                }
            )
        }

    }
}

@Preview(showBackground = true)
@Composable
fun LoginPreview() {
    PalmPetTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            LoginScreen()
        }
    }
}





