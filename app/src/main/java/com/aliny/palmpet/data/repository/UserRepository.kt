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
        nome_usuario: String,
        nome: String,
        dataNascimento: String,
        telefone: String,
        email: String,
        imageUri: Uri?, //parâmetro para a URI da imagem
        context: Context
    ){

    }

}
