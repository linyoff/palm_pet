package com.aliny.palmpet.ui.screens

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import com.aliny.palmpet.ui.components.CustomButton
import com.aliny.palmpet.ui.components.CustomDatePicker
import com.aliny.palmpet.ui.components.CustomTextField
import com.aliny.palmpet.ui.theme.CinzaContainersClaro
import com.aliny.palmpet.ui.theme.PalmPetTheme
import com.aliny.palmpet.ui.theme.RosaPrincipal

class AdicionarMedicacao : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PalmPetTheme {
                Surface {
                    AddMedicacoesScreen()
                }
            }
        }
    }
}

@Composable
fun AddMedicacoesScreen() {

    var nomeState by remember { mutableStateOf(TextFieldValue()) }
    var tipoState by remember { mutableStateOf("Tipo (Medicação ou Vacina)") }
    var dataState by remember { mutableStateOf("") }
    var petState by remember { mutableStateOf(TextFieldValue()) }
    var doseReforcoState by remember { mutableStateOf(TextFieldValue()) }
    var obsState by remember { mutableStateOf(TextFieldValue()) }
    var lembreteState by remember { mutableStateOf(false) }
    val context = LocalContext.current

    val tipos = listOf("Medicação", "Vacina")
    var estadoExpanded by remember { mutableStateOf(false) }

    Column (
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ){
        Text(
            text = "Adiconar Medicação ou Vacina para :",
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 80.dp),
            fontSize = 22.sp,
            color = RosaPrincipal
        )

        CustomTextField(
            value = nomeState,
            onValueChange = { nomeState = it },
            placeholderText = "Nome",
            modifier = Modifier
                .align(Alignment.CenterHorizontally),
            keyboardType = KeyboardType.Text
        )

        //código para a seleção de tipo
        Column(
            modifier = Modifier
                .padding(5.dp)
                .height(52.dp)
                .fillMaxWidth(0.78f)
                .align(Alignment.CenterHorizontally)
                .background(CinzaContainersClaro, RoundedCornerShape(17.dp))
        ){
            Text(
                text = tipoState,
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
                tipos.forEach{tipo ->
                    DropdownMenuItem(
                        onClick = {
                            tipoState = tipo
                            estadoExpanded = false
                            Log.i("Teste", "Clicado: $tipoState")
                            Toast.makeText(context, "Clicado: $tipoState", Toast.LENGTH_SHORT).show()
                        },
                        text = { Text(text = tipo) }
                    )
                }
            }
        }

        CustomDatePicker(
            label = "Data",
            selectedDate = dataState,
            onDateSelected = { dataState = it.text },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
        )

        CustomTextField(
            value = doseReforcoState,
            onValueChange = { doseReforcoState = it },
            placeholderText = "Dose de Reforço (em meses)",
            modifier = Modifier
                .align(Alignment.CenterHorizontally),
            keyboardType = KeyboardType.Text
        )

        CustomTextField(
            value = obsState,
            onValueChange = { obsState = it },
            placeholderText = "Observações",
            modifier = Modifier
                .align(Alignment.CenterHorizontally),
            keyboardType = KeyboardType.Text
        )

        CustomButton(
            onClickAction = {

            },
            modifier = Modifier
                .align(Alignment.CenterHorizontally),
            text = "Adicionar"
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AddMedicacaoPreview() {
    PalmPetTheme {
        AddMedicacoesScreen()
    }
}