package com.aliny.palmpet.ui.components

import android.app.DatePickerDialog
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aliny.palmpet.ui.theme.CinzaContainersClaro
import com.aliny.palmpet.ui.theme.CinzaContainersEscuro
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun CustomDatePicker(
    label: String,
    selectedDate: String,
    modifier: Modifier = Modifier,
    onDateSelected: (TextFieldValue) -> Unit
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    val datePickerDialog = createStyledDatePickerDialog(
        context,
        year,
        month,
        day,
        onDateSelected
    )

    Column(
        modifier = modifier
            .height(70.dp)
            .fillMaxWidth(0.78f)
            .padding(top = 3.dp)
    ) {
        Text(
            //preenche com o texto do label ou com a data selecionada
            text = if (selectedDate.isEmpty()) label else selectedDate,
            fontSize = 13.sp,
            modifier = modifier
                .background(CinzaContainersClaro, RoundedCornerShape(15.dp))
                .clickable { datePickerDialog.show() }
                .padding(18.dp)
                .fillMaxWidth()
        )
    }
}

//função que irá criar um date picker personalizado
//as cores foram alteradas nos arquivos de colors.xml e style.xml
fun createStyledDatePickerDialog(
    context: Context,
    year: Int,
    month: Int,
    day: Int,
    onDateSelected: (TextFieldValue) -> Unit
): DatePickerDialog {
    val themeResId = context.resources.getIdentifier("CustomDatePickerDialogTheme", "style", context.packageName)
    val datePickerDialog = DatePickerDialog(
        context,
        themeResId,
        { _, selectedYear, selectedMonth, selectedDay ->
            val calendar = Calendar.getInstance()
            calendar.set(selectedYear, selectedMonth, selectedDay)
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val formattedDate = dateFormat.format(calendar.time)
            onDateSelected(TextFieldValue(formattedDate))
        },
        year,
        month,
        day
    )

    //define as datas minimas e maximas
    val minDate = Calendar.getInstance().apply {
        set(year - 100, month, day) //até 100 anos atras
    }
    val maxDate = Calendar.getInstance() //data atual

    datePickerDialog.datePicker.maxDate = maxDate.timeInMillis
    datePickerDialog.datePicker.minDate = minDate.timeInMillis

    return datePickerDialog
}
