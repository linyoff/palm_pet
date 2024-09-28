package com.aliny.palmpet.data.repository

import Usuario
import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import com.aliny.palmpet.data.model.Pet
import com.aliny.palmpet.util.ValidationUtils
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.text.SimpleDateFormat
import java.util.Date
import java.util.UUID

object PetRepository {

    val db = Firebase.firestore
    //alterar
    var cachedPets: List<Pet>? = null
    val storage = Firebase.storage

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
        tipo_pelagem: String,
        ja_cruzou: Boolean?,
        teve_filhote: Boolean?,
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
            ValidationUtils.validarCampo(tipo_pelagem, "tipo_pelagem")

            val id_pet = UUID.randomUUID().toString()
            val id_tutor1 = userData.id_usuario

            val dateFormat = SimpleDateFormat("dd/MM/yyyy")
            val data1 = dateFormat.parse(dataNascString)
            val data_nascimento = Timestamp(Date(data1.time))
            var data_cio: Timestamp? = null
            if (dataCioString != "") {
                val data2 = dateFormat.parse(dataCioString)
                data_cio = Timestamp(Date(data2.time))
            }

            //fazer o upload da imagem
            fun uploadImageAndSavePet(imageUri: Uri?) {
                if (imageUri != null) {
                    val storageRef = storage.reference.child("pets/$id_pet.jpg")
                    storageRef.putFile(imageUri)
                        .addOnSuccessListener { taskSnapshot ->
                            storageRef.downloadUrl.addOnSuccessListener { uri ->
                                val pet = Pet(
                                    id_pet,
                                    id_tutor1,
                                    null,
                                    nome,
                                    data_nascimento,
                                    especie,
                                    raca,
                                    castrado,
                                    peso,
                                    sexo,
                                    cor,
                                    tipo_pelagem,
                                    ja_cruzou,
                                    teve_filhote,
                                    data_cio,
                                    uri.toString() // Salvar a URL da imagem no Firestore
                                )

                                db.collection("pets")
                                    .document(pet.id_pet)
                                    .set(pet)
                                    .addOnSuccessListener {
                                        Log.i("TESTE", "Novo pet adicionado com sucesso!")
                                        Toast.makeText(context, "Pet adicionado com sucesso!", Toast.LENGTH_SHORT).show()
                                        updateCachedPets(userData)
                                    }
                                    .addOnFailureListener { e ->
                                        Log.e("TESTE", "Erro ao adicionar novo pet", e)
                                        Toast.makeText(context, "Erro ao adicionar pet no Firestore: ${e.message}", Toast.LENGTH_SHORT).show()
                                    }
                            }
                        }
                        .addOnFailureListener { e ->
                            Log.e("TESTE", "Erro ao fazer upload da imagem", e)
                            Toast.makeText(context, "Erro ao fazer upload da imagem: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    val pet = Pet(
                        id_pet,
                        id_tutor1,
                        null,
                        nome,
                        data_nascimento,
                        especie,
                        raca,
                        castrado,
                        peso,
                        sexo,
                        cor,
                        tipo_pelagem,
                        ja_cruzou,
                        teve_filhote,
                        data_cio
                    )

                    db.collection("pets")
                        .document(pet.id_pet)
                        .set(pet)
                        .addOnSuccessListener {
                            Log.i("TESTE", "Novo pet adicionado com sucesso!")
                            Toast.makeText(context, "Pet adicionado com sucesso!", Toast.LENGTH_SHORT).show()
                            updateCachedPets(userData)
                        }
                        .addOnFailureListener { e ->
                            Log.e("TESTE", "Erro ao adicionar novo pet", e)
                            Toast.makeText(context, "Erro ao adicionar pet no Firestore: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                }
            }

            uploadImageAndSavePet(imageUri)

        } catch (e: Exception) {
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
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
        if (cachedPets != null) {
            onSuccess(cachedPets!!)
            return
        }

        db.collection("pets")
            .whereEqualTo("id_tutor1", userId)
            .get()
            .addOnSuccessListener { result ->
                val pets = result.documents.mapNotNull { document ->
                    document.toObject(Pet::class.java)
                }
                cachedPets = pets
                onSuccess(pets)
            }
            .addOnFailureListener { e ->
                Log.e("TESTE", "Erro ao buscar pets", e)
                onFailure(e)
            }
    }

    //alterar
    private fun updateCachedPets(userData: Usuario) {
        getPetsForLoggedUser(userData, {}, {})
    }
}
