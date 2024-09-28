package com.aliny.palmpet.ui.screens

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.layout.ContentScale
import coil.compose.rememberAsyncImagePainter
import com.aliny.palmpet.R
import com.aliny.palmpet.data.repository.PetRepository
import com.aliny.palmpet.ui.components.CustomButton
import com.aliny.palmpet.ui.components.CustomDatePicker
import com.aliny.palmpet.ui.components.CustomTextField
import com.aliny.palmpet.ui.components.ImagePicker
import com.aliny.palmpet.ui.theme.CinzaContainersClaro
import com.aliny.palmpet.ui.theme.PalmPetTheme
import com.aliny.palmpet.ui.theme.RosaPrincipal
import com.aliny.palmpet.viewmodel.UserViewModel

class TelaCadastroPet : ComponentActivity() {
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
                    TeladeCadastroPet(userViewModel = userViewModel)
                }
            }
        }
    }
}

@Composable
fun TeladeCadastroPet(userViewModel: UserViewModel) {

    //carregando os dados do usuário
    val userData by userViewModel.userData.observeAsState()
    val context = LocalContext.current

    //variaveis de controle de estado
    var nomeState by remember { mutableStateOf(TextFieldValue()) }
    var dataNascState by remember { mutableStateOf(TextFieldValue()) }
    var racaState by remember { mutableStateOf(TextFieldValue()) }
    var castradoState = remember { mutableStateOf(false) }
    var pesoState by remember { mutableStateOf(TextFieldValue()) }
    var corState by remember { mutableStateOf(TextFieldValue()) }
    var dataCioState by remember { mutableStateOf("") }
    var jaCruzouState = remember { mutableStateOf(false) }
    var teveFilhoteState = remember { mutableStateOf(false) }
    var sexoState by remember { mutableStateOf("Selecione o sexo do animal") }
    var pelagemState by remember { mutableStateOf("Tamanho de pelagem") }
    var especieState by remember { mutableStateOf("Selecione a espécie") }

    //váriaveis de estado dos DropDownMenus
    var estadoExpanded by remember { mutableStateOf(false) }
    var estadoExpanded2 by remember { mutableStateOf(false) }
    var estadoExpanded3 by remember { mutableStateOf(false) }

    //lista com as opções dos DropDownMenus
    val especies = listOf("Cão", "Gato", "Ave", "Tartaruga", "Peixe", "Roedor")
    val sexo = listOf("Macho", "Fêmea")
    var pelagem = listOf("Baixa", "Média", "Alta")

    Column (
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ){
        Image(painter = painterResource(id = R.drawable.logopalmpet),
            contentDescription = "Logo",
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .padding(top = 30.dp, bottom = 3.dp)
        )
        Text(
            text = "Faça o cadastro do seu Pet:",
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(vertical = 8.dp),
            fontSize = 22.sp,
            color = RosaPrincipal
        )
        CustomTextField(
            value = nomeState,
            onValueChange = { nomeState = it },
            placeholderText = "Nome",
            modifier = Modifier
                .align(Alignment.CenterHorizontally),
            keyboardType = KeyboardType.Text
        )

        CustomDatePicker(
            label = "Data de nascimento",
            selectedDate = dataNascState.text,
            onDateSelected = { dataNascState = it },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
        )

        //código para a seleção de espécie
        Column(
            modifier = Modifier
                .height(54.dp)
                .fillMaxWidth(0.78f)
                .padding(vertical = 3.dp)
                .align(Alignment.CenterHorizontally)
                .background(CinzaContainersClaro, RoundedCornerShape(17.dp))
        ){
            Text(
                text = especieState,
                fontSize = 13.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { estadoExpanded = !estadoExpanded }
                    .padding(15.dp)
            )
            DropdownMenu(
                expanded = estadoExpanded,
                onDismissRequest = { estadoExpanded = false }
            ) {
                especies.forEach{especie ->
                    DropdownMenuItem(
                        onClick = {
                            especieState = especie
                            estadoExpanded = false
                            Log.i("Teste", "Clicado: $especieState")
                            Toast.makeText(context, "Clicado: $especieState", Toast.LENGTH_SHORT).show()
                        },
                        text = { Text(text = especie) }
                    )
                }
            }
        }

        CustomTextField(
            value = racaState,
            onValueChange = { racaState = it },
            placeholderText = "Raça",
            modifier = Modifier
                .align(Alignment.CenterHorizontally),
            keyboardType = KeyboardType.Text
        )

        //selecionando se animal é castrado
        Column(
            modifier = Modifier
                .height(54.dp)
                .fillMaxWidth(0.78f)
                .padding(vertical = 3.dp)
                .align(Alignment.CenterHorizontally)
                .background(CinzaContainersClaro, RoundedCornerShape(17.dp))
                .clickable { castradoState.value = !castradoState.value }
        ) {
            Row (
                modifier = Modifier.align(Alignment.Start)
            ) {
                Checkbox(
                    checked = castradoState.value,
                    onCheckedChange = { castradoState.value = it }
                )
                Text(
                    text = "Castrado",
                    fontSize = 13.sp,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
            }
        }

        //selecionando sexo do animal
        Column(
            modifier = Modifier
                .height(54.dp)
                .fillMaxWidth(0.78f)
                .padding(vertical = 3.dp)
                .align(Alignment.CenterHorizontally)
                .background(CinzaContainersClaro, RoundedCornerShape(17.dp))
        ){
            Text(
                text = sexoState,
                fontSize = 13.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { estadoExpanded2 = !estadoExpanded2 }
                    .padding(15.dp)
            )
            DropdownMenu(
                expanded = estadoExpanded2,
                onDismissRequest = { estadoExpanded2 = false }
            ) {
                sexo.forEach{sexo ->
                    DropdownMenuItem(
                        onClick = {
                            sexoState = sexo
                            estadoExpanded2 = false
                            Log.i("Teste", "Clicado: $sexoState")
                            Toast.makeText(context, "Clicado: $sexoState", Toast.LENGTH_SHORT).show()
                        },
                        text = { Text(text = sexo) }
                    )
                }
            }
        }

        //verificação se é femea para mostrar mais opções de informação a serem inseridas
        if(sexoState=="Fêmea"){

            Column(
                modifier = Modifier
                    .height(54.dp)
                    .fillMaxWidth(0.78f)
                    .padding(vertical = 3.dp)
                    .align(Alignment.CenterHorizontally)
                    .background(CinzaContainersClaro, RoundedCornerShape(17.dp))
                    .clickable { jaCruzouState.value = !jaCruzouState.value }
            ) {
                Row (
                    modifier = Modifier.align(Alignment.Start)
                ) {
                    Checkbox(
                        checked = jaCruzouState.value,
                        onCheckedChange = { jaCruzouState.value = it }
                    )
                    Text(
                        text = "Já cruzou",
                        fontSize = 13.sp,
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                }
            }

            if(jaCruzouState.value){
                Column(
                    modifier = Modifier
                        .height(54.dp)
                        .fillMaxWidth(0.78f)
                        .padding(vertical = 3.dp)
                        .align(Alignment.CenterHorizontally)
                        .background(CinzaContainersClaro, RoundedCornerShape(17.dp))
                        .clickable { teveFilhoteState.value = !teveFilhoteState.value }
                ) {
                    Row (
                        modifier = Modifier.align(Alignment.Start)
                    ) {
                        Checkbox(
                            checked = teveFilhoteState.value,
                            onCheckedChange = { teveFilhoteState.value = it }
                        )
                        Text(
                            text = "Já teve filhote",
                            fontSize = 13.sp,
                            modifier = Modifier.align(Alignment.CenterVertically)
                        )
                    }
                }
            }

            if(!castradoState.value){
                CustomDatePicker(
                    label = "Data de último cio",
                    selectedDate = dataCioState,
                    onDateSelected = { dataCioState = it.text },
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                )
            }
        }

        CustomTextField(
            value = pesoState,
            onValueChange = { pesoState = it },
            placeholderText = "Peso",
            modifier = Modifier
                .align(Alignment.CenterHorizontally),
            keyboardType = KeyboardType.Number
        )

        CustomTextField(
            value = corState,
            onValueChange = { corState = it },
            placeholderText = "Cor",
            modifier = Modifier
                .align(Alignment.CenterHorizontally),
            keyboardType = KeyboardType.Text
        )

        //inserir tamanha de pelagem do animal
        Column(
            modifier = Modifier
                .height(54.dp)
                .fillMaxWidth(0.78f)
                .padding(vertical = 3.dp)
                .align(Alignment.CenterHorizontally)
                .background(CinzaContainersClaro, RoundedCornerShape(17.dp))
        ){
            Text(
                text = pelagemState,
                fontSize = 13.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { estadoExpanded3 = !estadoExpanded3 }
                    .padding(15.dp)
            )
            DropdownMenu(
                expanded = estadoExpanded3,
                onDismissRequest = { estadoExpanded3 = false }
            ) {
                pelagem.forEach{pelagem ->
                    DropdownMenuItem(
                        onClick = {
                            pelagemState = pelagem
                            estadoExpanded3 = false
                            Log.i("Teste", "Clicado: $pelagemState")
                            Toast.makeText(context, "Clicado: $pelagemState", Toast.LENGTH_SHORT).show()
                        },
                        text = { Text(text = pelagem) }
                    )
                }
            }
        }

        //método que irá inserir imagem do animal
        var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

        ImagePicker(
            onImageSelected = { uri -> selectedImageUri = uri }
        )
        
        CustomButton(
            onClickAction = {
                //validar e converter os estados dos campos necessários
                val peso = try {
                    pesoState.text.toFloat()
                } catch (e: NumberFormatException) {
                    Toast.makeText(context, "Peso inválido. Insira um número válido", Toast.LENGTH_SHORT).show()
                    return@CustomButton
                }

                //função adicionarPet com os valores coletados
                userData?.let {
                    PetRepository.addPet(
                        userData = it,
                        nome = nomeState.text,
                        dataNascString = dataNascState.text,
                        especie = especieState,
                        raca = racaState.text,
                        castrado = castradoState.value,
                        peso = peso,
                        sexo = sexoState,
                        cor = corState.text,
                        tipo_pelagem = pelagemState,
                        ja_cruzou = jaCruzouState.value,
                        teve_filhote = teveFilhoteState.value,
                        dataCioString = dataCioState,
                        imageUri = selectedImageUri,
                        context = context
                    )
                }
            },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 50.dp),
            text = "Cadastrar"
        )

    }
}

@Preview(showBackground = true)
@Composable
fun  TeladeCadastroPetPreview() {
    PalmPetTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            val userViewModel: UserViewModel = viewModel()
            TeladeCadastroPet(userViewModel = userViewModel)
        }
    }
}
