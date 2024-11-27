package com.aliny.palmpet.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.aliny.palmpet.ui.components.CustomOutlinedTextField
import com.aliny.palmpet.ui.theme.PalmPetTheme
import com.aliny.palmpet.viewmodel.UserViewModel

@Composable
fun Search(userViewModel: UserViewModel = viewModel()) {
    var searchText by remember { mutableStateOf(TextFieldValue()) }
    val users by userViewModel.userList.observeAsState(emptyList())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        CustomOutlinedTextField(
            value = searchText,
            onValueChange = {
                searchText = it
                userViewModel.searchUsers(it.text)
            },
            placeholderText = "Buscar...",
            modifier = Modifier.fillMaxWidth(),
            keyboardType = KeyboardType.Text
        )

        if (users.isEmpty()) {
            Text("Nenhum usuário encontrado.")
        } else {
            LazyColumn {
                items(users) { user ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start,
                        modifier = Modifier
                            .padding(vertical = 8.dp)
                            .clickable {

                            }
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter(user.imageUrl),
                            contentDescription = "Foto do usuário",
                            modifier = Modifier
                                .size(48.dp)
                                .padding(end = 8.dp),
                            contentScale = ContentScale.Crop
                        )
                        Text(user.nome_usuario)
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
