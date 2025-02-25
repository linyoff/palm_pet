package com.aliny.palmpet.ui.screens

import android.content.Intent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.aliny.palmpet.ui.theme.AzulFontes
import com.aliny.palmpet.ui.theme.CinzaContainersClaro
import com.aliny.palmpet.ui.theme.PalmPetTheme
import com.aliny.palmpet.viewmodel.UserViewModel

@Composable
fun UserProfile(){

    val userViewModel: UserViewModel = viewModel()
    val userData by userViewModel.userData.observeAsState()
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ){
        Button(
            onClick = {
                context.startActivity(Intent(context, EditProfile::class.java))
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = AzulFontes
            ),
            border = BorderStroke(1.dp, AzulFontes),
            modifier = Modifier
                .align(Alignment.End)
                .padding(end = 15.dp)
        ) {
            Text(
                text = "Editar",
                color = AzulFontes,
            )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Box(
                modifier = Modifier
                    .size(200.dp)
                    .clip(CircleShape)
                    .background(CinzaContainersClaro, shape = MaterialTheme.shapes.medium),
                contentAlignment = Alignment.Center
            ) {
                userData?.let { user ->
                    if (!user.imageUrl.isNullOrEmpty()) {
                        //imagem do usuário
                        val painter = rememberAsyncImagePainter(model = user.imageUrl)
                        Image(
                            painter = painter,
                            contentDescription = "Foto do usuário",
                            modifier = Modifier
                                .size(190.dp)
                                .clip(CircleShape)
                        )
                    } else {
                        //mostra a inicial do nome do usuário se não houver foto
                        androidx.compose.material3.Text(
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
            userData?.let { user ->
                //nome de usuário
                Text(
                    text = "@${user.nome_usuario}",
                    fontWeight = FontWeight.Bold,
                    color = AzulFontes,
                    fontSize = 20.sp,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                )
                //nome do usuário
                Text(
                    text = user.nome,
                    fontWeight = FontWeight.Bold,
                    color = AzulFontes,
                    fontSize = 28.sp,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UserProfilePreview() {
    PalmPetTheme {
        androidx.compose.material3.Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color.White
        ) {
            UserProfile()
        }
    }
}