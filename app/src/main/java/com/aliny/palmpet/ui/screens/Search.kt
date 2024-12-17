package com.aliny.palmpet.ui.screens

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.aliny.palmpet.ui.components.CustomTextField
import com.aliny.palmpet.ui.theme.AzulFontes
import com.aliny.palmpet.ui.theme.CinzaContainersEscuro
import com.aliny.palmpet.ui.theme.PalmPetTheme
import com.aliny.palmpet.viewmodel.UserViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@Composable
fun Search(userViewModel: UserViewModel = viewModel()) {
    var searchText by remember { mutableStateOf(TextFieldValue()) }
    val users by userViewModel.userList.observeAsState(emptyList())
    val context = LocalContext.current
    val currentUserId = Firebase.auth.currentUser?.uid //pega o ID do usuário logado

    //filtra a lista de usuários para remover o usuário logado
    val filteredUsers = users.filter { it.id_usuario != currentUserId }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        CustomTextField(
            value = searchText,
            onValueChange = {
                searchText = it
                userViewModel.searchUsers(it.text)
            },
            placeholderText = "Buscar usuário",
            modifier = Modifier.fillMaxWidth(),
            keyboardType = KeyboardType.Text
        )

        if (filteredUsers.isEmpty()) {
            Text("Nenhum usuário encontrado")
        } else {
            LazyColumn {
                //adicionando o filtro
                items(filteredUsers) { user ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start,
                        modifier = Modifier
                            .padding(vertical = 8.dp)
                            .clickable {
                                val intent = Intent(context, PublicUserProfile::class.java)
                                intent.putExtra("USER_ID", user.id_usuario)
                                context.startActivity(intent)
                            }
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter(user.imageUrl),
                            contentDescription = "Foto do usuário",
                            modifier = Modifier
                                .size(60.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                        Column(
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = user.nome,
                                fontSize = 20.sp,
                                color = AzulFontes,
                                modifier = Modifier.padding(start = 10.dp)
                            )
                            Text(
                                text = "@${user.nome_usuario}",
                                fontSize = 16.sp,
                                color = CinzaContainersEscuro,
                                modifier = Modifier.padding(start = 10.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun SearchPreview() {
    PalmPetTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color.White
        ) {
            Search()
        }
    }
}
