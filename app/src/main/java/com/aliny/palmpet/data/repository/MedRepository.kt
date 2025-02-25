package com.aliny.palmpet.data.repository

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.aliny.palmpet.data.model.Medicacao
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
        context: Context
    ) {
        try {
            ValidationUtils.validarCampo(nome, "nome")
            ValidationUtils.validarCampo(tipo, "tipo")

            val idMedicacao = UUID.randomUUID().toString()

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
                "id_medicacao" to idMedicacao,
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
            db.collection("medicacoes")
                .document(idMedicacao)
                .set(medicamento)
                .addOnSuccessListener {
                    Log.i("TESTE", "Medicação adicionada com sucesso!")
                    Toast.makeText(context, "Medicacão adicionada com sucesso!", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    Log.e("TESTE", "Erro ao adicionar medicação", e)
                    Toast.makeText(context, "Erro ao adicionar medicação no Firestore: ${e.message}", Toast.LENGTH_SHORT).show()
                }

        } catch (e: Exception) {
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
        }
    }

    fun getMedicacoesForPet(
        petId: String,
        onSuccess: (List<Medicacao>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        db.collection("medicacoes")
            .whereEqualTo("id_pet", petId)
            .get()
            .addOnSuccessListener { result ->
                val medicacoesList = mutableListOf<Medicacao>()
                for (document in result) {
                    val medicacao = document.toObject(Medicacao::class.java)
                    medicacoesList.add(medicacao)
                }
                onSuccess(medicacoesList)  //função de sucesso com a lista de medicações
            }
            .addOnFailureListener { exception ->
                Log.e("MedRepository", "Erro ao buscar medicações: ${exception.message}")
                onFailure(exception)  //função de falha com a exceção
            }
    }

    fun getMedicacaoById(
        medicacaoId: String,
        onSuccess: (Medicacao?) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        db.collection("medicacoes")
            .document(medicacaoId)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val medicacao = document.toObject(Medicacao::class.java)
                    onSuccess(medicacao)  //função de sucesso com a medicação encontrada
                } else {
                    onSuccess(null)  //se a medicação não existir
                }
            }
            .addOnFailureListener { exception ->
                Log.e("MedRepository", "Erro ao buscar medicação: ${exception.message}")
                onFailure(exception)  //função de falha com a exceção
            }
    }

    fun updateMedicamento(
        idMedicacao: String,
        nome: String? = null,
        tipo: String? = null,
        dataString: String? = null,
        doseReforcoString: String? = null,
        observacoes: String? = null,
        lembrete: Boolean? = null,
        context: Context
    ) {
        val updatedFields = mutableMapOf<String, Any>()

        try {
            nome?.let {
                ValidationUtils.validarCampo(it, "nome")
                updatedFields["nome"] = it
            }
            tipo?.let {
                ValidationUtils.validarCampo(it, "tipo")
                updatedFields["tipo"] = it
            }
            dataString?.let {
                updatedFields["data"] = Timestamp(SimpleDateFormat("dd/MM/yyyy").parse(it)!!)
            }
            doseReforcoString?.let {
                updatedFields["dose_reforco"] = Timestamp(SimpleDateFormat("dd/MM/yyyy").parse(it)!!)
            }
            observacoes?.let {
                updatedFields["observacoes"] = it
            }
            lembrete?.let {
                updatedFields["lembrete"] = it
            }

            if (updatedFields.isNotEmpty()) {
                db.collection("medicacoes")
                    .document(idMedicacao)
                    .update(updatedFields)
                    .addOnSuccessListener {
                        Toast.makeText(context, "Medicação atualizada com sucesso!", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener { e ->
                        Log.e("MedRepository", "Erro ao atualizar medicação: ${e.message}", e)
                        Toast.makeText(context, "Erro ao atualizar: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(context, "Nenhum campo foi alterado.", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
        }
    }


}
