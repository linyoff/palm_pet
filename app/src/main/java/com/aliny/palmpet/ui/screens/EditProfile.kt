package com.aliny.palmpet.ui.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberImagePainter
import com.aliny.palmpet.data.repository.UserRepository
import com.aliny.palmpet.ui.components.CustomButton
import com.aliny.palmpet.ui.components.CustomTextField
import com.aliny.palmpet.ui.theme.AzulFontes
import com.aliny.palmpet.ui.theme.CinzaContainersClaro
import com.aliny.palmpet.ui.theme.PalmPetTheme
import com.aliny.palmpet.viewmodel.UserViewModel
import java.text.SimpleDateFormat
import java.util.*

class EditProfile : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PalmPetTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.White
                ) {
                    val userViewModel: UserViewModel = viewModel()
                    EditProfile(userViewModel = userViewModel)
                }
            }
        }
    }
}

@Composable
fun EditProfile(userViewModel: UserViewModel = viewModel()) {
    // Observa os dados do usuário
    val userData by userViewModel.userData.observeAsState()

    // Controladores de estado para os campos de entrada com TextFieldValue para preservar o cursor
    var nome by remember { mutableStateOf(TextFieldValue("")) }
    var nomeUsuario by remember { mutableStateOf(TextFieldValue("")) }
    var dataNascimento by remember { mutableStateOf(TextFieldValue("")) }
    var telefone by remember { mutableStateOf(TextFieldValue("")) }
    var email by remember { mutableStateOf(TextFieldValue("")) }

    // Atualiza os campos de entrada quando userData mudar
    LaunchedEffect(userData) {
        userData?.let { user ->
            nome = TextFieldValue(user.nome ?: "")
            nomeUsuario = TextFieldValue(user.nome_usuario ?: "")
            dataNascimento = TextFieldValue(
                user.data_nascimento?.toDate()?.let {
                    SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(it)
                } ?: ""
            )
            telefone = TextFieldValue(user.telefone ?: "")
            email = TextFieldValue(user.email ?: "")
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
    ) {

        Spacer(modifier = Modifier.height(40.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 55.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(200.dp)
                    .clip(CircleShape)
                    .background(CinzaContainersClaro, shape = MaterialTheme.shapes.medium),
                contentAlignment = Alignment.Center
            ) {
                userData?.let { user ->
                    if (!user.imageUrl.isNullOrEmpty()) {
                        // Imagem do usuário
                        val painter = rememberImagePainter(data = user.imageUrl)
                        Image(
                            painter = painter,
                            contentDescription = "Foto do usuário",
                            modifier = Modifier
                                .size(190.dp)
                                .clip(CircleShape)
                        )
                    }
                }

                Icon(
                    imageVector = Icons.Outlined.Edit,
                    contentDescription = "Selecionar Foto",
                    modifier = Modifier
                        .size(60.dp)
                        .clickable { }
                        .align(Alignment.Center),
                    tint = Color.White
                )
            }
        }

        Text(
            text = "Informações do Usuário:",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(horizontal = 22.dp, vertical = 8.dp),
            color = AzulFontes
        )

        // Campos customizados usando CustomTextField
        CustomTextField(
            value = nome,
            onValueChange = { nome = it },
            placeholderText = "Nome completo",
            modifier = Modifier.align(Alignment.CenterHorizontally),
            keyboardType = KeyboardType.Text
        )

        CustomTextField(
            value = nomeUsuario,
            onValueChange = { nomeUsuario = it },
            placeholderText = "Nome de usuário",
            modifier = Modifier.align(Alignment.CenterHorizontally),
            keyboardType = KeyboardType.Text
        )

        CustomTextField(
            value = dataNascimento,
            onValueChange = { dataNascimento = it },
            placeholderText = "Data de nascimento",
            modifier = Modifier.align(Alignment.CenterHorizontally),
            keyboardType = KeyboardType.Text
        )

        CustomTextField(
            value = telefone,
            onValueChange = { telefone = it },
            placeholderText = "Telefone",
            modifier = Modifier.align(Alignment.CenterHorizontally),
            keyboardType = KeyboardType.Phone
        )

        CustomTextField(
            value = email,
            onValueChange = { email = it },
            placeholderText = "Email",
            modifier = Modifier.align(Alignment.CenterHorizontally),
            keyboardType = KeyboardType.Email
        )

        CustomButton(
            onClickAction = {

            },
            modifier = Modifier
                .align(Alignment.CenterHorizontally),
            text = "Salvar"
        )
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewEditProfile() {
    PalmPetTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color.White
        ) {
            val userViewModel: UserViewModel = viewModel()
            EditProfile(userViewModel = userViewModel)
        }
    }
}
