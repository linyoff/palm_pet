package com.aliny.palmpet.ui.screens

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Password
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aliny.palmpet.ui.theme.PalmPetTheme

class Seguranca : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PalmPetTheme {

                val context = LocalContext.current

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    SecurityScreen(
                        modifier = Modifier.padding(innerPadding),
                        onAlterarEmailClick = {
                            val intent = Intent(context, EditEmail::class.java)
                            context.startActivity(intent)
                        },
                        onAlterarSenhaClick = {
                            val intent = Intent(context, EditPassword::class.java)
                            context.startActivity(intent)
                        },
                        onDesativarContaClick = {
                            // Chamar navegação para a tela de desativar conta
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun SecurityScreen(
    modifier: Modifier = Modifier,
    onAlterarEmailClick: () -> Unit,
    onAlterarSenhaClick: () -> Unit,
    onDesativarContaClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(text = "Segurança e Privacidade", style = MaterialTheme.typography.titleLarge)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onAlterarEmailClick() }
                .padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Email,
                contentDescription = "alterar email",
                tint = Color.Black,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = "Alterar Email", fontSize = 15.sp)
        }
        Divider(color = Color.LightGray, thickness = 1.dp)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onAlterarSenhaClick() }
                .padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Password,
                contentDescription = "alterar senha",
                tint = Color.Black,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = "Alterar Senha", fontSize = 15.sp)
        }
        Divider(color = Color.LightGray, thickness = 1.dp)

        Button(
            onClick = onDesativarContaClick,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
        ) {
            Text(text = "Desativar Conta")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SecurityScreenPreview() {
    PalmPetTheme {
        SecurityScreen(
            onAlterarEmailClick = {},
            onAlterarSenhaClick = {},
            onDesativarContaClick = {}
        )
    }
}
