package com.aliny.palmpet.data.repository

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.aliny.palmpet.util.ValidationUtils
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.Date
import java.util.UUID

object MedRepository {

    val db = Firebase.firestore

    fun addMedicamento(
        idPet: String,
        idTutor1: String,
        idTutor2: String?,
        nome: String,
        tipo: String,
        dataString: String,
        doseReforcoString: String,
        observacoes: String,
        lembrete: Boolean,
        context: Context,
        onSuccess: () -> Unit,
        onFailure: () -> Unit
    ) {
        try {
            ValidationUtils.validarCampo(nome, "nome")
            ValidationUtils.validarCampo(tipo, "tipo")

            val idMedicamento = UUID.randomUUID().toString()

            //conversão de datas
            val dateFormat = SimpleDateFormat("dd/MM/yyyy")
            val data1 = dateFormat.parse(dataString)
            val data = Timestamp(Date(data1.time))

            var doseReforco: Timestamp? = null
            if (doseReforcoString.isNotEmpty()) {
                val data2 = dateFormat.parse(doseReforcoString)
                doseReforco = Timestamp(Date(data2.time))
            }

            val medicamento = hashMapOf(
                "id_medicamento" to idMedicamento,
                "id_pet" to idPet,
                "id_tutor1" to idTutor1,
                "id_tutor2" to idTutor2,
                "nome" to nome,
                "tipo" to tipo,
                "data" to data,
                "dose_reforco" to doseReforco,
                "observacoes" to observacoes,
                "lembrete" to lembrete
            )

            //adicionando medicamento ao Firestore
            db.collection("medicamentos")
                .document(idMedicamento)
                .set(medicamento)
                .addOnSuccessListener {
                    Log.i("TESTE", "Medicamento adicionado com sucesso!")
                    Toast.makeText(context, "Medicamento adicionado com sucesso!", Toast.LENGTH_SHORT).show()
                    onSuccess()
                }
                .addOnFailureListener { e ->
                    Log.e("TESTE", "Erro ao adicionar medicamento", e)
                    Toast.makeText(context, "Erro ao adicionar medicamento no Firestore: ${e.message}", Toast.LENGTH_SHORT).show()
                    onFailure()
                }

        } catch (e: Exception) {
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
            onFailure()
        }
    }
}
