package com.aliny.palmpet.ui.screens

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.aliny.palmpet.data.repository.MedRepository
import com.aliny.palmpet.ui.components.BackButton
import com.aliny.palmpet.ui.components.CustomButton
import com.aliny.palmpet.ui.components.CustomDatePicker
import com.aliny.palmpet.ui.components.CustomTextField
import com.aliny.palmpet.ui.theme.AzulFontes
import com.aliny.palmpet.ui.theme.CinzaContainersClaro
import com.aliny.palmpet.ui.theme.CinzaContainersEscuro
import com.aliny.palmpet.ui.theme.PalmPetTheme
import com.aliny.palmpet.ui.theme.RosaPrincipal
import com.aliny.palmpet.viewmodel.PetViewModel
import com.aliny.palmpet.viewmodel.UserViewModel

class AgendarCompromisso : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PalmPetTheme {
                Surface {
                    CompromissScreen()
                }
            }
        }
    }
}

@Composable
fun CompromissScreen(userViewModel: UserViewModel = viewModel(), petViewModel: PetViewModel = viewModel()) {

    val userData by userViewModel.userData.observeAsState()
    var nomeState by remember { mutableStateOf(TextFieldValue()) }
    var dataState by remember { mutableStateOf("") }
    val doseReforcoState by remember { mutableStateOf("") }
    var obsState by remember { mutableStateOf(TextFieldValue()) }
    var lembreteState by remember { mutableStateOf(false) }
    var petSelecionadoState by remember { mutableStateOf("Selecionar Pet") }
    var estadoExpanded by remember { mutableStateOf(false) }
    val context = LocalContext.current

    val pets by petViewModel.pets.observeAsState(emptyList())

    LaunchedEffect(userData) {
        if (userData != null) {
            petViewModel.loadPets(userData!!)
        } else {
            Log.e("CompromissScreen", "userData é nulo, não carregando pets")
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        BackButton(
            onBackClick = { (context as? ComponentActivity)?.finish() },
            iconColor = AzulFontes,
            iconSize = 45
        )

        Text(
            text = "Adicionar um compromisso",
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 80.dp, bottom = 15.dp),
            fontSize = 22.sp,
            color = RosaPrincipal
        )

        CustomTextField(
            value = nomeState,
            onValueChange = { nomeState = it },
            placeholderText = "Nome compromisso",
            modifier = Modifier.align(Alignment.CenterHorizontally),
            keyboardType = KeyboardType.Text
        )

        CustomDatePicker(
            label = "Data",
            selectedDate = dataState,
            onDateSelected = { dataState = it.text },
            isFutureAllowed = false,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        //novo campo para seleção do pet
        Column(
            modifier = Modifier
                .padding(5.dp)
                .height(52.dp)
                .fillMaxWidth(0.80f)
                .align(Alignment.CenterHorizontally)
                .background(CinzaContainersClaro, RoundedCornerShape(12.dp))
        ) {
            Text(
                text = petSelecionadoState,
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
                pets.forEach { pet ->
                    DropdownMenuItem(
                        onClick = {
                            petSelecionadoState = pet.nome
                            estadoExpanded = false
                        },
                        text = { Text(text = pet.nome) }
                    )
                }
            }
        }

        CustomTextField(
            value = obsState,
            onValueChange = { obsState = it },
            placeholderText = "Observações",
            modifier = Modifier.align(Alignment.CenterHorizontally),
            keyboardType = KeyboardType.Text
        )

        //switch para lembrete
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 40.dp, vertical = 15.dp)
                .align(Alignment.CenterHorizontally),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Ativar lembrete", fontSize = 18.sp)
            Switch(
                checked = lembreteState,
                onCheckedChange = { lembreteState = it },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = AzulFontes,
                    uncheckedThumbColor = CinzaContainersClaro,
                    checkedTrackColor = RosaPrincipal,
                    uncheckedTrackColor = CinzaContainersEscuro
                )
            )
        }

        CustomButton(
            onClickAction = {
                val petSelecionado = pets.find { it.nome == petSelecionadoState }
                if (petSelecionado != null) {
                    MedRepository.addMedicamento(
                        idPet = petSelecionado.id_pet,
                        idTutor1 = petSelecionado.id_tutor1,
                        idTutor2 = petSelecionado.id_tutor2,
                        nome = nomeState.text,
                        tipo = "Compromisso",
                        dataString = dataState,
                        doseReforcoString = doseReforcoState,
                        observacoes = obsState.text,
                        lembrete = lembreteState,
                        context = context
                    )
                } else {
                    Toast.makeText(context, "Selecione um pet!", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = "Adicionar"
        )
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview5() {
    PalmPetTheme {

    }
}