package com.aliny.palmpet.data.repository

import Usuario
import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import com.aliny.palmpet.data.model.Pet
import com.aliny.palmpet.util.CampoInvalidoException
import com.aliny.palmpet.util.ValidationUtils
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.text.SimpleDateFormat
import java.util.Date
import java.util.UUID

object PetRepository {

    @SuppressLint("StaticFieldLeak")
    val db = Firebase.firestore
    private val storage = Firebase.storage

    @SuppressLint("SimpleDateFormat")
    fun addPet(
        userData: Usuario,
        nome: String,
        dataNascString: String,
        especie: String,
        raca: String,
        castrado: Boolean,
        peso: Float,
        sexo: String,
        cor: String,
        tipoPelagem: String,
        jaCruzou: Boolean?,
        teveFilhote: Boolean?,
        dataCioString: String,
        imageUri: Uri?, //parâmetro para a URI da imagem
        context: Context,
        onSuccess: () -> Unit, //callback para sucesso
        onFailure: () -> Unit //callback para falha
    ) {
        try {
            ValidationUtils.validarCampo(nome, "nome")
            ValidationUtils.validarCampo(especie, "especie")
            ValidationUtils.validarCampo(raca, "raca")
            ValidationUtils.validarCampo(sexo, "sexo")
            ValidationUtils.validarCampo(cor, "cor")
            ValidationUtils.validarCampo(tipoPelagem, "tipo_pelagem")

            val idPet = UUID.randomUUID().toString()
            val idTutor1 = userData.id_usuario

            val dateFormat = SimpleDateFormat("dd/MM/yyyy")
            val data1 = dateFormat.parse(dataNascString)
            val dataNasc = Timestamp(Date(data1!!.time))
            var dataCio: Timestamp? = null
            if (dataCioString != "") {
                val data2 = dateFormat.parse(dataCioString)
                dataCio = Timestamp(Date(data2!!.time))
            }

            //fazer o upload da imagem
            fun uploadImageAndSavePet(imageUri: Uri?) {
                if (imageUri != null) {
                    val storageRef = storage.reference.child("pets/$idPet.jpg")
                    storageRef.putFile(imageUri)
                        .addOnSuccessListener { _ ->
                            storageRef.downloadUrl.addOnSuccessListener { uri ->
                                val pet = Pet(
                                    idPet,
                                    idTutor1,
                                    null,
                                    nome,
                                    dataNasc,
                                    especie,
                                    raca,
                                    castrado,
                                    peso,
                                    sexo,
                                    cor,
                                    tipoPelagem,
                                    jaCruzou,
                                    teveFilhote,
                                    dataCio,
                                    uri.toString() //salvar a URL da imagem no Firestore
                                )

                                db.collection("pets")
                                    .document(pet.id_pet)
                                    .set(pet)
                                    .addOnSuccessListener {
                                        Log.i("TESTE", "Novo pet adicionado com sucesso!")
                                        Toast.makeText(context, "Pet adicionado com sucesso!", Toast.LENGTH_SHORT).show()
                                        onSuccess() //callback de sucesso
                                    }
                                    .addOnFailureListener { e ->
                                        Log.e("TESTE", "Erro ao adicionar novo pet", e)
                                        Toast.makeText(context, "Erro ao adicionar pet no Firestore: ${e.message}", Toast.LENGTH_SHORT).show()
                                        onFailure() //callback de falha
                                    }
                            }
                        }
                        .addOnFailureListener { e ->
                            Log.e("TESTE", "Erro ao fazer upload da imagem", e)
                            Toast.makeText(context, "Erro ao fazer upload da imagem: ${e.message}", Toast.LENGTH_SHORT).show()
                            onFailure() //callback de falha
                        }
                } else {
                    val pet = Pet(
                        idPet,
                        idTutor1,
                        null,
                        nome,
                        dataNasc,
                        especie,
                        raca,
                        castrado,
                        peso,
                        sexo,
                        cor,
                        tipoPelagem,
                        jaCruzou,
                        teveFilhote,
                        dataCio
                    )

                    db.collection("pets")
                        .document(pet.id_pet)
                        .set(pet)
                        .addOnSuccessListener {
                            Log.i("TESTE", "Novo pet adicionado com sucesso!")
                            Toast.makeText(context, "Pet adicionado com sucesso!", Toast.LENGTH_SHORT).show()
                            onSuccess() //callback de sucesso
                        }
                        .addOnFailureListener { e ->
                            Log.e("TESTE", "Erro ao adicionar novo pet", e)
                            Toast.makeText(context, "Erro ao adicionar pet no Firestore: ${e.message}", Toast.LENGTH_SHORT).show()
                            onFailure() //chama o callback de falha
                        }
                }
            }

            uploadImageAndSavePet(imageUri)

        } catch (e: Exception) {
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
            onFailure() //chama o callback de falha
        }
    }

    fun getPetById(petId: String, onSuccess: (Pet?) -> Unit, onFailure: (Exception) -> Unit) {
        db.collection("pets")
            .document(petId)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val pet = document.toObject(Pet::class.java)
                    onSuccess(pet)
                } else {
                    onSuccess(null)
                }
            }
            .addOnFailureListener { e ->
                Log.e("PetRepository", "Erro ao buscar pet por ID: ${e.message}")
                onFailure(e)
            }
    }


    fun getPetsForLoggedUser(
        userData: Usuario,
        onSuccess: (List<Pet>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val userId = userData.id_usuario


        db.collection("pets")
            .whereEqualTo("id_tutor1", userId)
            .get()
            .addOnSuccessListener { result ->
                val pets = result.documents.mapNotNull { document ->
                    document.toObject(Pet::class.java)
                }
                onSuccess(pets)
            }
            .addOnFailureListener { e ->
                Log.e("TESTE", "Erro ao buscar pets", e)
                onFailure(e)
            }
    }

    fun deletePet(
        petId: String,
        context: Context,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        db.collection("pets")
            .document(petId)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val pet = document.toObject(Pet::class.java)
                    val imageUrl = pet?.imageUrl //url da imagem

                    //excluindo a imagem primeiro
                    if (!imageUrl.isNullOrEmpty()) {
                        val storageRef = storage.getReferenceFromUrl(imageUrl)
                        storageRef.delete()
                            .addOnSuccessListener {
                                //imagem excluída
                                deletePetFromFirestore(petId, context, onSuccess, onFailure)
                            }
                            .addOnFailureListener { e ->
                                Log.e("TESTE", "Erro ao excluir a imagem do pet", e)
                                Toast.makeText(context, "Erro ao excluir a imagem do pet: ${e.message}", Toast.LENGTH_SHORT).show()
                                onFailure(e)
                            }
                    } else {
                        //se não houver imagem exclui o pet
                        deletePetFromFirestore(petId, context, onSuccess, onFailure)
                    }
                } else {
                    Toast.makeText(context, "Pet não encontrado", Toast.LENGTH_SHORT).show()
                    onFailure(Exception("Pet não encontrado"))
                }
            }
            .addOnFailureListener { e ->
                Log.e("TESTE", "Erro ao buscar o pet", e)
                Toast.makeText(context, "Erro ao buscar o pet: ${e.message}", Toast.LENGTH_SHORT).show()
                onFailure(e)
            }
    }

    private fun deletePetFromFirestore(
        petId: String,
        context: Context,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        db.collection("pets")
            .document(petId)
            .delete()
            .addOnSuccessListener {
                Log.i("TESTE", "Pet excluído com sucesso")
                Toast.makeText(context, "Pet excluído com sucesso", Toast.LENGTH_SHORT).show()
                onSuccess()
            }
            .addOnFailureListener { e ->
                Log.e("TESTE", "Erro ao excluir pet", e)
                Toast.makeText(context, "Erro ao excluir pet: ${e.message}", Toast.LENGTH_SHORT).show()
                onFailure(e)
            }
    }

    @SuppressLint("SimpleDateFormat")
    fun updatePet(
        petId: String,
        nome: String,
        dataNascString: String,
        especie: String,
        raca: String,
        castrado: Boolean,
        peso: Float,
        sexo: String,
        cor: String,
        tipoPelagem: String,
        jaCruzou: Boolean?,
        teveFilhote: Boolean?,
        dataCioString: String,
        imageUri: Uri?, //parâmetro para a URI da imagem
        context: Context
    ) {
        try {
            ValidationUtils.validarCampo(nome, "nome")
            ValidationUtils.validarCampo(especie, "especie")
            ValidationUtils.validarCampo(raca, "raca")
            ValidationUtils.validarCampo(sexo, "sexo")
            ValidationUtils.validarCampo(cor, "cor")
            ValidationUtils.validarCampo(tipoPelagem, "tipo_pelagem")

            //convertendo datas para timestamp
            val dateFormat = SimpleDateFormat("dd/MM/yyyy")
            val dataNasc = dateFormat.parse(dataNascString)
            val dataNascimento = Timestamp(Date(dataNasc!!.time))

            var dataCio: Timestamp? = null
            if (dataCioString.isNotEmpty()) {
                val dataCioParsed = dateFormat.parse(dataCioString)
                dataCio = Timestamp(Date(dataCioParsed!!.time))
            }

            //atualiza os dados do pet no Firestore
            fun updatePetData(imageUrl: String?) {
                val updatedData = mutableMapOf<String, Any>(
                    "nome" to nome,
                    "data_nascimento" to dataNascimento,
                    "especie" to especie,
                    "raca" to raca,
                    "castrado" to castrado,
                    "peso" to peso,
                    "sexo" to sexo,
                    "cor" to cor,
                    "tipo_pelagem" to tipoPelagem
                )

                jaCruzou?.let { updatedData["ja_cruzou"] = it }
                teveFilhote?.let { updatedData["teve_filhote"] = it }
                dataCio?.let { updatedData["data_cio"] = it }
                imageUrl?.let { updatedData["imageUrl"] = it }

                //atualizando os dados do pet no Firestore
                db.collection("pets")
                    .document(petId)
                    .update(updatedData)
                    .addOnSuccessListener {
                        Toast.makeText(context, "Pet atualizado com sucesso!", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener { e ->
                        Log.e("TESTE", "Erro ao atualizar pet", e)
                        Toast.makeText(context, "Erro ao atualizar pet: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            }

            //verificando se há nova imagem para upload
            if (imageUri != null) {
                val storageRef = storage.reference.child("pets/$petId.jpg")
                storageRef.putFile(imageUri)
                    .addOnSuccessListener {
                        storageRef.downloadUrl.addOnSuccessListener { uri ->
                            updatePetData(uri.toString())
                        }
                    }
                    .addOnFailureListener { e ->
                        Log.e("TESTE", "Erro ao fazer upload da imagem", e)
                        Toast.makeText(context, "Erro ao fazer upload da imagem: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            } else {
                //vpenas atualiza os dados do pet sem modificar a URL da imagem
                updatePetData(null)
            }

        } catch (e: CampoInvalidoException) {
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Log.e("TESTE", "Erro inesperado: ${e.message}", e)
        }
    }


}
