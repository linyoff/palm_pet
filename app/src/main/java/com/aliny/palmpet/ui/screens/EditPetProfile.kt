package com.aliny.palmpet.ui.screens

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.aliny.palmpet.data.repository.PetRepository
import com.aliny.palmpet.ui.components.BackButton
import com.aliny.palmpet.ui.components.CustomButton
import com.aliny.palmpet.ui.components.CustomDatePicker
import com.aliny.palmpet.ui.components.CustomTextField
import com.aliny.palmpet.ui.theme.AzulFontes
import com.aliny.palmpet.ui.theme.CinzaContainersClaro
import com.aliny.palmpet.ui.theme.PalmPetTheme
import com.aliny.palmpet.viewmodel.PetViewModel
import java.text.SimpleDateFormat
import java.util.Locale

class EditPetProfile : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PalmPetTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.White
                ) {
                    EditPetProfileScreen()
                }
            }
        }
    }
}

@Composable
fun EditPetProfileScreen(petViewModel: PetViewModel = viewModel()) {

    val petId = (LocalContext.current as ComponentActivity).intent.getStringExtra("pet_id")
    val petData by petViewModel.pet.observeAsState()
    val context = LocalContext.current

    LaunchedEffect(petId) {
        petId?.let {
            petViewModel.loadPetById(it)
        } ?: Toast.makeText(context, "Erro ao carregar pet", Toast.LENGTH_SHORT).show()
    }

    //controladores de estado para os campos de entrada
    var nomeState by remember { mutableStateOf(TextFieldValue()) }
    var dataNascState by remember { mutableStateOf("") }
    var racaState by remember { mutableStateOf(TextFieldValue()) }
    val castradoState = remember { mutableStateOf(false) }
    var pesoState by remember { mutableStateOf(TextFieldValue()) }
    var corState by remember { mutableStateOf(TextFieldValue()) }
    var dataCioState by remember { mutableStateOf("") }
    val jaCruzouState = remember { mutableStateOf(false) }
    val teveFilhoteState = remember { mutableStateOf(false) }
    var sexoState by remember { mutableStateOf("Selecione o sexo do animal") }
    var pelagemState by remember { mutableStateOf("Tamanho de pelagem") }
    var especieState by remember { mutableStateOf("Selecione a espécie") }

    //estado dos DropDownMenus
    var estadoExpanded by remember { mutableStateOf(false) }
    var estadoExpanded2 by remember { mutableStateOf(false) }
    var estadoExpanded3 by remember { mutableStateOf(false) }

    //listas do DropDownMenus
    val especies = listOf("Cão", "Gato", "Ave", "Tartaruga", "Peixe", "Roedor")
    val sexo = listOf("Macho", "Fêmea")
    val pelagem = listOf("Baixa", "Média", "Alta")

    //estado para a URI da nova imagem selecionada
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    LaunchedEffect(petData) {
        petData?.let { pet ->
            nomeState = TextFieldValue(pet.nome)
            racaState = TextFieldValue(pet.raca)

            //conversão de timestamp para formato legível
            dataNascState = pet.data_nascimento.let { timestamp ->
                try {
                    val date = timestamp.toDate() //converte o timestamp para date
                    SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(date)
                } catch (e: Exception) {
                    ""
                }
            } ?: ""

            especieState = pet.especie
            sexoState = pet.sexo
            castradoState.value = pet.castrado
            pesoState = TextFieldValue(pet.peso.toString())
            corState = TextFieldValue(pet.cor)
            pelagemState = pet.tipo_pelagem

            dataCioState = pet.data_cio?.let { timestamp ->
                try {
                    val date = timestamp.toDate() //converte o timestamp para date
                    SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(date)
                } catch (e: Exception) {
                    ""
                }
            } ?: ""

            jaCruzouState.value = pet.ja_cruzou ?: false
            teveFilhoteState.value = pet.teve_filhote ?: false
        }
    }

    //lançador para selecionar a imagem
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        //atualiza a URI da imagem ao selecionar uma nova
        if (uri != null) {
            imageUri = uri
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 55.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            BackButton(
                onBackClick = { (context as? ComponentActivity)?.finish() },
                iconColor = AzulFontes,
                iconSize = 45
            )

            Box(
                modifier = Modifier
                    .size(200.dp)
                    .clip(CircleShape)
                    .background(CinzaContainersClaro, shape = MaterialTheme.shapes.medium),
                contentAlignment = Alignment.Center
            ) {
                //mostra a nova imagem selecionada
                val painter = if (imageUri != null) {
                    rememberAsyncImagePainter(model = imageUri)
                } else {
                    //mostra imagem anterior do pet
                    rememberAsyncImagePainter(model = petData?.imageUrl)
                }

                Image(
                    painter = painter,
                    contentDescription = "Foto do pet",
                    modifier = Modifier
                        .size(190.dp)
                        .clip(CircleShape)
                )

                Icon(
                    imageVector = Icons.Outlined.Edit,
                    contentDescription = "Selecionar Foto",
                    modifier = Modifier
                        .size(60.dp)
                        .clickable {
                            //seletor de imagem
                            imagePickerLauncher.launch("image/*")
                        }
                        .align(Alignment.Center),
                    tint = Color.White
                )
            }
        }


        CustomTextField(
            value = nomeState,
            onValueChange = { nomeState = it },
            placeholderText = "Nome",
            modifier = Modifier.align(Alignment.CenterHorizontally),
            keyboardType = KeyboardType.Text
        )

        CustomDatePicker(
            label = dataNascState,
            selectedDate = dataNascState,
            onDateSelected = { dataNascState = it.text },
            isFutureAllowed = false,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Column(
            modifier = Modifier
                .height(54.dp)
                .fillMaxWidth(0.78f)
                .padding(vertical = 3.dp)
                .align(Alignment.CenterHorizontally)
                .background(CinzaContainersClaro, RoundedCornerShape(17.dp))
        ) {
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
                especies.forEach { especie ->
                    DropdownMenuItem(
                        onClick = {
                            especieState = especie
                            estadoExpanded = false
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
            modifier = Modifier.align(Alignment.CenterHorizontally),
            keyboardType = KeyboardType.Text
        )

        Column(
            modifier = Modifier
                .height(54.dp)
                .fillMaxWidth(0.78f)
                .padding(vertical = 3.dp)
                .align(Alignment.CenterHorizontally)
                .background(CinzaContainersClaro, RoundedCornerShape(17.dp))
                .clickable { castradoState.value = !castradoState.value }
        ) {
            Row(
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

        Column(
            modifier = Modifier
                .height(54.dp)
                .fillMaxWidth(0.78f)
                .padding(vertical = 3.dp)
                .align(Alignment.CenterHorizontally)
                .background(CinzaContainersClaro, RoundedCornerShape(17.dp))
        ) {
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
                sexo.forEach { sexo ->
                    DropdownMenuItem(
                        onClick = {
                            sexoState = sexo
                            estadoExpanded2 = false
                            Toast.makeText(context, "Clicado: $sexoState", Toast.LENGTH_SHORT).show()
                        },
                        text = { Text(text = sexo) }
                    )
                }
            }
        }

        //exibe mais campos se o pet for fêmea
        if (sexoState == "Fêmea") {
            Column(
                modifier = Modifier
                    .height(54.dp)
                    .fillMaxWidth(0.78f)
                    .padding(vertical = 3.dp)
                    .align(Alignment.CenterHorizontally)
                    .background(CinzaContainersClaro, RoundedCornerShape(17.dp))
                    .clickable { jaCruzouState.value = !jaCruzouState.value }
            ) {
                Row(
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

            if (jaCruzouState.value) {
                Column(
                    modifier = Modifier
                        .height(54.dp)
                        .fillMaxWidth(0.78f)
                        .padding(vertical = 3.dp)
                        .align(Alignment.CenterHorizontally)
                        .background(CinzaContainersClaro, RoundedCornerShape(17.dp))
                        .clickable { teveFilhoteState.value = !teveFilhoteState.value }
                ) {
                    Row(
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

            if (!castradoState.value) {
                CustomDatePicker(
                    label = "Data de último cio",
                    selectedDate = dataCioState,
                    onDateSelected = { dataCioState = it.text },
                    isFutureAllowed = false,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }

        CustomTextField(
            value = pesoState,
            onValueChange = { pesoState = it },
            placeholderText = "Peso",
            modifier = Modifier.align(Alignment.CenterHorizontally),
            keyboardType = KeyboardType.Number
        )

        CustomTextField(
            value = corState,
            onValueChange = { corState = it },
            placeholderText = "Cor",
            modifier = Modifier.align(Alignment.CenterHorizontally),
            keyboardType = KeyboardType.Text
        )

        Column(
            modifier = Modifier
                .height(54.dp)
                .fillMaxWidth(0.78f)
                .padding(vertical = 3.dp)
                .align(Alignment.CenterHorizontally)
                .background(CinzaContainersClaro, RoundedCornerShape(17.dp))
        ) {
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
                pelagem.forEach { pelagem ->
                    DropdownMenuItem(
                        onClick = {
                            pelagemState = pelagem
                            estadoExpanded3 = false
                            Toast.makeText(context, "Clicado: $pelagemState", Toast.LENGTH_SHORT).show()
                        },
                        text = { Text(text = pelagem) }
                    )
                }
            }
        }

        CustomButton(
            onClickAction = {

                //validar e converter os estados dos campos necessários
                val peso = try {
                    pesoState.text.toFloat()
                } catch (e: NumberFormatException) {
                    Toast.makeText(context, "Peso inválido. Insira um número válido", Toast.LENGTH_SHORT).show()
                    return@CustomButton
                }

                petData?.let {
                    PetRepository.updatePet(
                        petId = petId.toString(),
                        nome = nomeState.text,
                        dataNascString = dataNascState,
                        especie = especieState,
                        raca = racaState.text,
                        castrado = castradoState.value,
                        peso = peso,
                        sexo = sexoState,
                        cor = corState.text,
                        tipoPelagem = pelagemState,
                        jaCruzou = jaCruzouState.value,
                        teveFilhote = teveFilhoteState.value,
                        dataCioString = dataCioState,
                        imageUri = imageUri,
                        context = context
                    )
                }
            },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 2.dp),
            text = "Salvar"
        )
    }
}

@Preview(showBackground = true)
@Composable
fun EditPetProfilePreview() {
    PalmPetTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color.White
        ) {
            EditPetProfileScreen()
        }
    }
}