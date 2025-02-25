package com.aliny.palmpet.ui.screens

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.aliny.palmpet.R
import com.aliny.palmpet.data.repository.PetRepository
import com.aliny.palmpet.ui.components.ActionButton
import com.aliny.palmpet.ui.theme.AzulFontes
import com.aliny.palmpet.ui.theme.CianoBotoes
import com.aliny.palmpet.ui.theme.CinzaContainersClaro
import com.aliny.palmpet.ui.theme.PalmPetTheme
import com.aliny.palmpet.ui.theme.RosaPrincipal
import com.aliny.palmpet.viewmodel.PetViewModel
import java.text.SimpleDateFormat
import java.util.Locale

class TelaPet : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PalmPetTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.White
                ){
                    TelaDoPet()
                }
            }
        }
    }
}

@Composable
fun TelaDoPet(petViewModel: PetViewModel = viewModel()) {

    val petId = (LocalContext.current as ComponentActivity).intent.getStringExtra("pet_id")
    val pet by petViewModel.pet.observeAsState()
    var showDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current

    //verifica se o petId é válido e se o pet já foi carregado
    LaunchedEffect(petId) {
        petId?.let {
            petViewModel.loadPetById(it)
        }
    }

    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ){
        //ícone de voltar
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(bottom = 16.dp, top = 30.dp)
                .clickable {
                    //finaliza a tela atual
                    val activity = context as? ComponentActivity
                    activity?.finish()
                }
        ) {
            Icon(
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = "Voltar",
                tint = AzulFontes,
                modifier = Modifier
                    .size(45.dp)
            )
        }

        BotoesDeAcaoPet(petId.toString())
        Button(
            onClick = {
                val intent = Intent(context, EditPetProfile::class.java).apply {
                    putExtra("pet_id", petId)
                }
                context.startActivity(intent)
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = AzulFontes
            ),
            border = BorderStroke(1.dp, AzulFontes),
            modifier = Modifier
                .align(Alignment.End)
                .padding(end = 25.dp)
        ) {
            Text(
                text = "Editar",
                color = AzulFontes,
            )
        }
        Column(
            modifier = Modifier
                .padding(bottom = 30.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(200.dp)
                    .clip(CircleShape)
                    .background(CinzaContainersClaro, shape = RoundedCornerShape(21.dp)),
                contentAlignment = Alignment.Center
            ) {
                pet?.let {
                    if (!it.imageUrl.isNullOrEmpty()) {
                        val painter: Painter = rememberAsyncImagePainter(model = it.imageUrl)
                        Image(
                            painter = painter,
                            contentDescription = "Foto do pet",
                            modifier = Modifier
                                .size(190.dp)
                                .clip(CircleShape)
                        )
                    } else {
                        Text(
                            text = it.nome.take(1), //incial do nome do pet
                            fontSize = 48.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }
        Text(
            text = "Informações do Pet:",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(horizontal = 22.dp),
            color = AzulFontes
        )
        //informações do pet
        pet?.let { petInfo ->
            InfoCard(label = "Nome", value = petInfo.nome)

            //formatando a data de nascimento
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val dataNascimentoStr = dateFormat.format(petInfo.data_nascimento.toDate())
            InfoCard(label = "Data de nascimento", value = dataNascimentoStr)
            InfoCard(label = "Sexo", value = petInfo.sexo)
            val castradoStr = if (petInfo.castrado) "Sim" else "Não"
            InfoCard(label = "Castrado", value = castradoStr)
            InfoCard(label = "Espécie", value = petInfo.especie)
            InfoCard(label = "Raça", value = petInfo.raca)
            InfoCard(label = "Peso", value = petInfo.peso.toString() + " kg")
            InfoCard(label = "Cor", value = petInfo.cor)
            InfoCard(label = "Tamanho de pelagem", value = petInfo.tipo_pelagem)
            InfoCard(label = "Já cruzou", value = if (petInfo.ja_cruzou == true) "Sim" else "Não")
            InfoCard(label = "Teve filhotes", value = if (petInfo.teve_filhote == true) "Sim" else "Não")

            //verifica se a data do cio está disponível
            val dataCioStr = petInfo.data_cio?.let { dateFormat.format(it.toDate()) } ?: "N/A"
            InfoCard(label = "Última data do cio", value = dataCioStr)

            InfoCard(label = "Guarda compartilhada", value = petInfo.id_tutor2 ?: "N/A")

            Button(
                onClick = { showDialog = true },
                modifier = Modifier
                    .padding(bottom = 35.dp)
                    .align(Alignment.CenterHorizontally),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AzulFontes,
                )
            ) {
                Text(text = "Excluir Pet", color = Color.White)
            }

        } ?: run {
            Text(
                text = "Carregando informações do pet...",
                fontSize = 18.sp,
                modifier = Modifier.padding(19.dp),
                color = AzulFontes
            )
        }
    }
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = {
                Text(text = "Confirmar Exclusão")
            },
            text = {
                Text("Tem certeza de que deseja excluir este pet? Não será possível recuperar seus dados após isso.", textAlign = TextAlign.Center)
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        petId?.let { id ->
                            PetRepository.deletePet(
                                petId = id,
                                context = context,
                                onSuccess = {
                                    showDialog = false
                                },
                                onFailure = {
                                    showDialog = false
                                }
                            )
                        }
                    }
                ) {
                    Text("Excluir")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
fun InfoCard(label: String, value: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 7.dp, horizontal = 28.dp)
            .background(CinzaContainersClaro, RoundedCornerShape(12.dp))
            .padding(11.dp)
    ) {
        Column {
            Text(
                text = label,
                fontSize = 16.sp,
                color = Color.Gray,
                fontWeight = FontWeight.Light
            )
            Text(
                text = value,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = AzulFontes
            )
        }
    }
}

@Composable
fun BotoesDeAcaoPet(petId : String) {

    val context = LocalContext.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 30.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        ActionButton(
            text = "Medicações e vacinas",
            backgroundColor = CianoBotoes,
            iconResId = R.drawable.icon_medicacoes,
            onClick = {
                val intent = Intent(context, Medicacoes::class.java).apply {
                    putExtra("pet_id", petId)
                }
                context.startActivity(intent)
            },
            modifier = Modifier
                .size(110.dp)
        )
        Spacer(modifier = Modifier.width(11.dp))
        ActionButton(
            text = "Histórico",
            backgroundColor = RosaPrincipal,
            iconResId = R.drawable.icon_exame,
            onClick = {
                val intent = Intent(context, Historico::class.java)
                context.startActivity(intent)
            },
            modifier = Modifier
                .size(110.dp)
        )
        Spacer(modifier = Modifier.width(11.dp))
        ActionButton(
            text = "Agendar consulta",
            backgroundColor = CianoBotoes,
            iconResId = R.drawable.icon_consulta,
            onClick = {
                val intent = Intent(context, AgendarConsulta::class.java)
                context.startActivity(intent)
            },
            modifier = Modifier
                .size(110.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TelaDoPetPreview() {
    PalmPetTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ){
            TelaDoPet()
        }
    }
}