package com.aliny.palmpet.ui.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.aliny.palmpet.data.model.Pet
import com.aliny.palmpet.ui.theme.AzulFontes
import com.aliny.palmpet.ui.theme.CinzaContainersClaro
import com.aliny.palmpet.ui.theme.PalmPetTheme
import com.aliny.palmpet.viewmodel.PetViewModel
import com.aliny.palmpet.viewmodel.UserViewModel
import com.google.accompanist.flowlayout.FlowRow

class PublicUserProfile : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        //recupera o id do usuário passado pelo Intent
        val userId = intent.getStringExtra("USER_ID") ?: ""

        setContent {
            PalmPetTheme {
                //chama a tela de perfil do usuário com o id
                PublicUserProfileScreen(userId)
            }
        }
    }
}

@Composable
fun PublicUserProfileScreen(userId: String) {

    val context = LocalContext.current
    val userViewModel: UserViewModel = viewModel()
    val petViewModel: PetViewModel = viewModel()

    //carrega os dados do usuário com o id
    LaunchedEffect(userId) {
        userViewModel.loadUserById(userId)
    }

    val user by userViewModel.thirdUserData.observeAsState()

    //carrega os pets assim que os dados do usuário estiverem disponíveis
    LaunchedEffect(user) {
        user?.let {
            petViewModel.loadPets(it)
        }
    }

    //observa os pets carregados
    val pets by petViewModel.pets.observeAsState(emptyList())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

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

        Column(
            modifier = Modifier
                .padding(top = 60.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(200.dp)
                    .clip(CircleShape)
                    .background(CinzaContainersClaro, shape = MaterialTheme.shapes.medium),
                contentAlignment = Alignment.Center
            ) {
                user?.let { user ->
                    if (!user.imageUrl.isNullOrEmpty()) {
                        val painter = rememberAsyncImagePainter(model = user.imageUrl)
                        Image(
                            painter = painter,
                            contentDescription = "Foto do usuário",
                            modifier = Modifier
                                .size(190.dp)
                                .clip(CircleShape)
                        )
                    } else {

                        Text(
                            text = user.nome.take(1),
                            fontSize = 48.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }

        Column {
            user?.let { user ->
                Text(
                    text = "@${user.nome_usuario}",
                    fontWeight = FontWeight.Bold,
                    color = AzulFontes,
                    fontSize = 20.sp,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Text(
                    text = user.nome,
                    fontWeight = FontWeight.Bold,
                    color = AzulFontes,
                    fontSize = 28.sp,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }

        //lista de pets
        if (pets.isNotEmpty()) {
            PetsListUser(pets = pets)
        } else {
            Text(
                text = "Nenhum pet encontrado.",
                color = Color.Gray,
                fontSize = 16.sp,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}


@Composable
fun PetsListUser(pets: List<Pet>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Pets",
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
                PetItemUser(pet = pet)
            }
        }
    }
}

@Composable
fun PetItemUser(pet: Pet) {

    val imageUrl = pet.imageUrl

    //estado para mostrar ou esconder o botão
    var showButton by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .size(112.dp)
            .clickable {
                //muda o estado ao clicar
                showButton = !showButton
            },
        contentAlignment = Alignment.Center
    ) {
        //foto do Pet
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(112.dp)
                    .background(CinzaContainersClaro, RoundedCornerShape(21.dp)),
                contentAlignment = Alignment.Center
            ) {
                if (!imageUrl.isNullOrEmpty()) {
                    val painter: Painter = rememberAsyncImagePainter(model = imageUrl)
                    Image(
                        painter = painter,
                        contentDescription = "Foto do pet",
                        modifier = Modifier
                            .size(112.dp)
                            .clip(RoundedCornerShape(21.dp))
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

        //botão para solicitar guarda
        if (showButton) {
            Box(
                modifier = Modifier
                    .zIndex(2f)
                    .align(Alignment.TopEnd)
                    .background(CinzaContainersClaro, RoundedCornerShape(12.dp))
                    .border(1.dp, AzulFontes, RoundedCornerShape(12.dp))
                    .clickable {

                    }
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Text(
                    text = "Guarda compartilhada",
                    color = AzulFontes,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    PalmPetTheme {

    }
}
