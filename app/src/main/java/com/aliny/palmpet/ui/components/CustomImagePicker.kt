package com.aliny.palmpet.ui.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddPhotoAlternate
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter

@Composable
fun ImagePicker(
    onImageSelected: (Uri) -> Unit
) {
    // Estado para gerenciar a URI da imagem selecionada
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    // Abrir a galeria para selecionar a imagem
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            imageUri = it // Atualiza a URI da imagem selecionada
            onImageSelected(it) // Chama o callback para informar a imagem selecionada
        }
    }

    // Função para abrir a galeria
    fun openGallery() {
        galleryLauncher.launch("image/*")
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(100.dp) // Tamanho ajustado para o ícone
                .clip(CircleShape) // Mantém o formato circular
                .clickable { openGallery() }
                .background(Color.Gray), // Fundo para o ícone
            contentAlignment = Alignment.Center
        ) {
            imageUri?.let {
                Image(
                    painter = rememberImagePainter(it),
                    contentDescription = "Foto",
                    contentScale = ContentScale.Crop, // Faz a imagem preencher a área de forma recortada
                    modifier = Modifier
                        .fillMaxSize() // Preenche completamente o ícone
                        .clip(CircleShape) // Garante que a imagem fique circular
                )
            } ?: run {
                Icon(
                    imageVector = Icons.Outlined.AddPhotoAlternate,
                    contentDescription = "Selecionar Foto",
                    modifier = Modifier.size(60.dp), // Tamanho do ícone
                    tint = Color.White
                )
            }
        }
    }
}

