package com.aliny.palmpet.data.auth

import com.aliny.palmpet.MainActivity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

object AuthService {
    val auth = Firebase.auth

    fun criarUsuarioComEmailESenha(email: String, senha: String, onComplete: (Boolean, String?) -> Unit) {
        auth.createUserWithEmailAndPassword(email, senha)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid
                    onComplete(true, userId)
                } else {
                    onComplete(false, null)
                }
            }
    }

    fun enviarEmailVerificacao(context: Context) {
        val user = auth.currentUser
        user?.sendEmailVerification()
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(context, "Email de verificação enviado para ${user.email}", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Falha ao enviar email de verificação: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    fun getEmailCredential(email: String, password: String): AuthCredential {
        return EmailAuthProvider.getCredential(email, password)
    }

    fun loginComEmailESenha(email: String, senha: String, context: Context) {
        auth.signInWithEmailAndPassword(email, senha)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    if (user != null) {
                        Toast.makeText(context, "Login bem-sucedido", Toast.LENGTH_SHORT).show()
                        val intent = Intent(context, MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        context.startActivity(intent)
                    } else {
                        Toast.makeText(context, "Usuário não autenticado", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    val errorMessage = when (task.exception?.message) {
                        "The supplied auth credential is incorrect, malformed or has expired." -> "Email ou senha incorretos"
                        else -> "Falha no login: ${task.exception?.message}"
                    }
                    Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                    Log.e("Login", errorMessage)
                }
            }
    }



}
