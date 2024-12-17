package com.aliny.palmpet.ui.screens

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.aliny.palmpet.R
import com.aliny.palmpet.data.model.Pet
import com.aliny.palmpet.ui.components.ActionButton
import com.aliny.palmpet.ui.components.CustomLoadingIndicator
import com.aliny.palmpet.ui.theme.AzulFontes
import com.aliny.palmpet.ui.theme.CianoBotoes
import com.aliny.palmpet.ui.theme.CinzaContainersClaro
import com.aliny.palmpet.ui.theme.PalmPetTheme
import com.aliny.palmpet.ui.theme.RosaPrincipal
import com.aliny.palmpet.viewmodel.PetViewModel
import com.aliny.palmpet.viewmodel.UserViewModel
import com.google.accompanist.flowlayout.FlowRow

@Composable
fun Home(userViewModel: UserViewModel = viewModel(), petViewModel: PetViewModel = viewModel()) {
    val userData by userViewModel.userData.observeAsState()
    val pets by petViewModel.pets.observeAsState()
    val error by petViewModel.error.observeAsState()

    userData?.let {
        //busca os pets quando o usuário estiver logado
        petViewModel.loadPets(it)
    }

    if (userData == null) {
        CustomLoadingIndicator()
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            userData?.let { Greeting(name = it.nome) }
            BotoesDeAcao()
            PetsList(pets ?: emptyList())

            error?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(16.dp)
                )
            }

        }
    }
}

@Composable
fun Greeting(name: String) {
    val firstName = name.split(" ").firstOrNull() ?: ""
    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            text = "Olá, $firstName!",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(top = 5.dp, start = 8.dp),
            color = AzulFontes
        )
    }
}

@Composable
fun BotoesDeAcao() {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally)
    ) {
        ActionButton(
            text = "Procurar veterinários",
            backgroundColor = RosaPrincipal,
            iconResId = R.drawable.icon_procurar_vet,
            onClick = {

            },
            modifier = Modifier.size(130.dp)
        )
        ActionButton(
            text = "Agendar um Compromisso",
            backgroundColor = CianoBotoes,
            iconResId = R.drawable.icon_compromisso,
            onClick = {

            },
            modifier = Modifier.size(130.dp)
        )
    }
}

@Composable
fun PetsList(pets: List<Pet>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Meus pets",
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .align(Alignment.Start),
            color = AzulFontes
        )
        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 9.dp),
            mainAxisSpacing = 13.dp,
            crossAxisSpacing = 13.dp
        ) {
            pets.forEach { pet ->
                PetItem(pet = pet)
            }
            BotaoAddPet()
        }
    }
}

@Composable
fun PetItem(pet: Pet) {
    val context = LocalContext.current
    val imageUrl = pet.imageUrl

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(112.dp)
                .background(CinzaContainersClaro, RoundedCornerShape(21.dp))
                .clickable {
                    val intent = Intent(context, TelaPet::class.java).apply {
                        putExtra("pet_id", pet.id_pet)
                    }
                    context.startActivity(intent)
                },
            contentAlignment = Alignment.Center
        ) {
            if (imageUrl != null && imageUrl.isNotEmpty()) {
                val painter: Painter = rememberAsyncImagePainter(model = imageUrl)
                Image(
                    painter = painter,
                    contentDescription = "Foto do pet",
                    modifier = Modifier
                        .size(112.dp)
                )
            } else {
                Text(
                    text = pet.nome.take(1),
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        Text(
            text = pet.nome,
            color = AzulFontes,
            fontSize = 17.sp,
            modifier = Modifier.padding(top = 6.dp)
        )
    }
}

@Composable
fun BotaoAddPet() {
    val context = LocalContext.current

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(112.dp)
                .background(CinzaContainersClaro, shape = RoundedCornerShape(21.dp))
                .clickable {
                    val intent = Intent(context, TelaCadastroPet::class.java)
                    context.startActivity(intent)
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "+ add pet",
                color = AzulFontes,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomePreview() {
    PalmPetTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color.White
        ) {
            Home()
        }
    }
}