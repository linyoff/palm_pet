package com.aliny.palmpet.ui.screens

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Medication
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.aliny.palmpet.data.model.Medicacao
import com.aliny.palmpet.ui.components.BackButton
import com.aliny.palmpet.ui.theme.AzulFontes
import com.aliny.palmpet.ui.theme.PalmPetTheme
import com.aliny.palmpet.ui.theme.VermelhoAlertas
import com.aliny.palmpet.viewmodel.MedViewModel
import com.aliny.palmpet.viewmodel.PetViewModel
import java.text.SimpleDateFormat
import java.util.Locale

class Medicacoes : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PalmPetTheme {
                Surface{
                    MedicacoesScreen()
                }
            }
        }
    }
}

@Composable
fun MedicacoesScreen(
    petViewModel: PetViewModel = viewModel(),
    medViewModel: MedViewModel = viewModel()
) {
    val petId = (LocalContext.current as ComponentActivity).intent.getStringExtra("pet_id")
    val pet by petViewModel.pet.observeAsState()
    val medicacoes by medViewModel.medicacoes.observeAsState(emptyList())
    val context = LocalContext.current

    //verifica se o petId é válido e carrega o pet e suas medicações
    LaunchedEffect(petId) {
        petId?.let {
            petViewModel.loadPetById(it)
            medViewModel.loadMedicacoesByPetId(it) //carrega as medicações para o pet
        }
    }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        BackButton(
            onBackClick = { (context as? ComponentActivity)?.finish() },
            iconColor = AzulFontes,
            iconSize = 45
        )

        //titulo e botão para adicionar medicação
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Medicações e Vacinas",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = AzulFontes,
                modifier = Modifier
                    .padding(start = 16.dp)
            )
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = "Adicionar Medicação",
                tint = AzulFontes,
                modifier = Modifier
                    .size(50.dp)
                    .clickable {
                        val intent = Intent(context, AdicionarMedicacao::class.java).apply {
                            putExtra("pet_id", petId)
                        }
                        context.startActivity(intent)
                    }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        //lista de medicações
        if (medicacoes.isEmpty()) {
            Text(
                text = "Nenhuma medicação cadastrada para este pet.",
                fontSize = 16.sp,
                color = VermelhoAlertas,
                modifier = Modifier.padding(16.dp)
            )
        } else {
            medicacoes.forEach { medicacao ->
                MedicacaoItem(medicacao = medicacao)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun MedicacaoItem(medicacao: Medicacao) {

    var context = LocalContext.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                val intent = Intent(context, DetalhesMedicacao::class.java).apply {
                    putExtra("id_medicacao", medicacao.id_medicacao)
                }
                context.startActivity(intent)
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Filled.Medication,
            contentDescription = "Ícone de medicação",
            tint = AzulFontes,
            modifier = Modifier.size(40.dp)
        )
        Column(
            modifier = Modifier
                .padding(start = 16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = medicacao.nome,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = AzulFontes
            )

            val date = medicacao.data.toDate() // converte o timestamp para Date
            val formattedDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(date) // formata a data

            Text(
                text = "Data: ${formattedDate}",
                fontSize = 14.sp,
                color = AzulFontes
            )
        }
    }
}



@Preview(showBackground = true)
@Composable
fun MedicacoesPreview() {
    PalmPetTheme {
        MedicacoesScreen()
    }
}