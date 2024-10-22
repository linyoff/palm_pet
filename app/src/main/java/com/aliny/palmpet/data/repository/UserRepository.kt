package com.aliny.palmpet.data.repository

import Usuario
import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import com.aliny.palmpet.data.auth.AuthService
import com.aliny.palmpet.util.CampoInvalidoException
import com.aliny.palmpet.util.ValidationUtils
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.text.SimpleDateFormat
import java.util.Date

object UserRepository {
    val db = Firebase.firestore
    val storage = Firebase.storage

    fun setUser(
        nome_usuario: String,
        nome: String,
        dataNascimento: String,
        telefone: String,
        email: String,
        senha: String,
        imageUri: Uri?, //parâmetro para a URI da imagem
        context: Context
    ) {
        try {
            //validações dos campos de entrada
            ValidationUtils.validarCampo(nome_usuario, "nome_usuario")
            ValidationUtils.validarCampo(nome, "nome")
            ValidationUtils.validarCampo(dataNascimento, "data_nascimento")
            ValidationUtils.validarCampo(telefone, "telefone")
            ValidationUtils.validarCampo(email, "email")
            ValidationUtils.validarCampo(senha, "senha")

            //verifica se o nome de usuário já existe
            db.collection("usuarios")
                .whereEqualTo("nome_usuario", nome_usuario)
                .get()
                .addOnSuccessListener { documents ->
                    if (!documents.isEmpty) {
                        //nome de usuário já existe
                        Toast.makeText(context, "Nome de usuário já existe. Escolha outro nome.", Toast.LENGTH_SHORT).show()
                    } else {
                        //convertendo dataNascimento para Timestamp do Firebase
                        val dateFormat = SimpleDateFormat("dd/MM/yyyy")
                        val data = dateFormat.parse(dataNascimento)
                        val timestamp = Timestamp(Date(data.time))

                        //criação do usuário no Firebase Authentication
                        AuthService.criarUsuarioComEmailESenha(email, senha) { success, userId ->
                            if (success && userId != null) {
                                //função para fazer upload da imagem
                                fun uploadImageAndSaveUser(imageUri: Uri?) {
                                    if (imageUri != null) {
                                        val storageRef = storage.reference.child("usuarios/$userId.jpg")
                                        storageRef.putFile(imageUri)
                                            .addOnSuccessListener { taskSnapshot ->
                                                storageRef.downloadUrl.addOnSuccessListener { uri ->
                                                    //criação do usuário no Firestore com a URL da imagem
                                                    val usuario = Usuario(
                                                        userId,
                                                        nome_usuario,
                                                        nome,
                                                        timestamp,
                                                        telefone,
                                                        email,
                                                        uri.toString() //salva a URL da imagem no Firestore
                                                    )

                                                    //adiciona o usuário ao Firestore usando o UID como ID do documento
                                                    db.collection("usuarios")
                                                        .document(userId)
                                                        .set(usuario)
                                                        .addOnSuccessListener {
                                                            Toast.makeText(context, "Novo usuário adicionado com sucesso!", Toast.LENGTH_SHORT).show()
                                                            AuthService.enviarEmailVerificacao(context)
                                                        }
                                                        .addOnFailureListener { e ->
                                                            Log.e("TESTE", "Erro ao adicionar novo usuário", e)
                                                            Toast.makeText(context, "Erro ao adicionar usuário no Firestore: ${e.message}", Toast.LENGTH_SHORT).show()
                                                        }
                                                }
                                            }
                                            .addOnFailureListener { e ->
                                                Log.e("TESTE", "Erro ao fazer upload da imagem", e)
                                                Toast.makeText(context, "Erro ao fazer upload da imagem: ${e.message}", Toast.LENGTH_SHORT).show()
                                            }
                                    } else {
                                        //criação do usuário no Firestore sem imagem
                                        val usuario = Usuario(userId, nome_usuario, nome, timestamp, telefone, email)

                                        db.collection("usuarios")
                                            .document(userId)
                                            .set(usuario)
                                            .addOnSuccessListener {
                                                Toast.makeText(context, "Novo usuário adicionado com sucesso!", Toast.LENGTH_SHORT).show()
                                                AuthService.enviarEmailVerificacao(context)
                                            }
                                            .addOnFailureListener { e ->
                                                Log.e("TESTE", "Erro ao adicionar novo usuário", e)
                                                Toast.makeText(context, "Erro ao adicionar usuário no Firestore: ${e.message}", Toast.LENGTH_SHORT).show()
                                            }
                                    }
                                }
                                //upload da imagem e salva o usuário
                                uploadImageAndSaveUser(imageUri)

                            } else {
                                Log.e("TESTE", "Falha na criação do usuário no Firebase Authentication")
                                Toast.makeText(context, "Erro ao criar usuário", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
                .addOnFailureListener { e ->
                    Log.e("TESTE", "Erro ao verificar nome de usuário", e)
                    Toast.makeText(context, "Erro ao verificar nome de usuário: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        } catch (e: CampoInvalidoException) {
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Log.e("TESTE", "Erro inesperado: ${e.message}", e)
        }
    }

    fun updateUser(
        uid_user: String,
        nome_usuario: String,
        nome: String,
        dataNascimento: String,
        telefone: String,
        imageUri: Uri?, //parâmetro para a URI da imagem
        context: Context
    ){
        try {
            //validações dos campos de entrada
            ValidationUtils.validarCampo(nome_usuario, "nome_usuario")
            ValidationUtils.validarCampo(nome, "nome")
            ValidationUtils.validarCampo(dataNascimento, "data_nascimento")
            ValidationUtils.validarCampo(telefone, "telefone")

            //convertendo dataNascimento para timestamp
            val dateFormat = SimpleDateFormat("dd/MM/yyyy")
            val data = dateFormat.parse(dataNascimento)
            val timestamp = Timestamp(Date(data.time))

            //função para atualizar os dados do usuário no Firestore
            fun updateUserData(imageUrl: String?) {
                val updatedData = mutableMapOf<String, Any>(
                    "nome_usuario" to nome_usuario,
                    "nome" to nome,
                    "dataNascimento" to timestamp,
                    "telefone" to telefone
                )

                // Se houver uma nova URL de imagem, atualiza também
                imageUrl?.let { updatedData["imageUrl"] = it }

                //atualizando os dados do usuário no Firestore
                db.collection("usuarios")
                    .document(uid_user)
                    .update(updatedData)
                    .addOnSuccessListener {
                        Toast.makeText(context, "Usuário atualizado com sucesso!", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener { e ->
                        Log.e("TESTE", "Erro ao atualizar usuário", e)
                        Toast.makeText(context, "Erro ao atualizar usuário: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            }

            //verificando se tem uma nova imagem para upload
            if (imageUri != null) {
                val storageRef = storage.reference.child("usuarios/$uid_user.jpg")
                storageRef.putFile(imageUri)
                    .addOnSuccessListener {
                        storageRef.downloadUrl.addOnSuccessListener { uri ->
                            //chama a função para salvar os dados do usuário com a nova imagem
                            updateUserData(uri.toString())
                        }
                    }
                    .addOnFailureListener { e ->
                        Log.e("TESTE", "Erro ao fazer upload da imagem", e)
                        Toast.makeText(context, "Erro ao fazer upload da imagem: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            } else {
                //apenas atualiza os dados do usuário sem modificar a URL da imagem
                updateUserData(null)
            }

        } catch (e: CampoInvalidoException) {
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Log.e("TESTE", "Erro inesperado: ${e.message}", e)
        }
    }

    /*fun updateUserEmail(
        uid_user: String,
        newEmail: String,
        password: String, //reautenticar o usuário antes da troca de email
        context: Context,
        onSuccess: () -> Unit, //callback para quando a operação for bem-sucedida
        onFailure: (String) -> Unit //callback para quando a operação falhar
    ) {
        //reautentica o usuário antes de alterar o e-mail
        val credential = AuthService.getEmailCredential(user.email!!, password)

        user.reauthenticate(credential)
            .addOnSuccessListener {
                //atualiza o e-mail
                user.updateEmail(newEmail)
                    .addOnSuccessListener {
                        //atualiza no firestore também
                        db.collection("usuarios")
                            .document(user.uid)
                            .update("email", newEmail)
                            .addOnSuccessListener {
                                Toast.makeText(context, "Email atualizado com sucesso!", Toast.LENGTH_SHORT).show()
                                onSuccess()
                            }
                            .addOnFailureListener { e ->
                                    Log.e("UPDATE_EMAIL", "Erro ao atualizar o e-mail no Firestore", e)
                                    onFailure("Erro ao atualizar o e-mail no Firestore: ${e.message}")
                            }
                    }
                    .addOnFailureListener { e ->
                        Log.e("UPDATE_EMAIL", "Erro ao atualizar o e-mail no Firebase Authentication", e)
                        onFailure("Erro ao atualizar o e-mail: ${e.message}")
                    }
            }
            .addOnFailureListener { e ->
                Log.e("REAUTHENTICATE", "Erro ao reautenticar o usuário", e)
                onFailure("Erro ao reautenticar o usuário: ${e.message}")
            }
    }*/

}
