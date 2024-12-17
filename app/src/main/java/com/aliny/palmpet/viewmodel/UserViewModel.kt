package com.aliny.palmpet.viewmodel

import Usuario
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class UserViewModel : ViewModel() {

    val _userData = MutableLiveData<Usuario?>()
    val userData: LiveData<Usuario?> get() = _userData
    val _thirdUserData = MutableLiveData<Usuario?>() //LiveData para dados de terceiros
    val thirdUserData: LiveData<Usuario?> get() = _thirdUserData //exposição pública do LiveData de terceiro
    val _userList = MutableLiveData<List<Usuario>>()
    val userList: LiveData<List<Usuario>> get() = _userList

    var cachedUser: Usuario? = null

    init {
        checkLoginStatus()
    }

    fun checkLoginStatus() {
        val user = Firebase.auth.currentUser
        if (user != null) {
            if (cachedUser == null) {
                fetchUserData(user.uid)
            } else {
                _userData.postValue(cachedUser)
            }
        } else {
            _userData.postValue(null)
        }
    }

    fun fetchUserData(uid: String) {
        val db = Firebase.firestore
        db.collection("usuarios").document(uid).get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val usuario = document.toObject(Usuario::class.java)
                    cachedUser = usuario
                    _userData.postValue(usuario)
                } else {
                    _userData.postValue(null)
                }
            }
            .addOnFailureListener {
                _userData.postValue(null)
            }
    }

    //carregar dados de um usuário terceiro
    fun loadUserById(uid: String) {
        val db = Firebase.firestore
        db.collection("usuarios").document(uid).get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val usuario = document.toObject(Usuario::class.java)
                    _thirdUserData.postValue(usuario) //atualiza os dados
                } else {
                    _thirdUserData.postValue(null) //caso o usuário não exista
                }
            }
            .addOnFailureListener {
                _thirdUserData.postValue(null) //caso haja erro na busca
            }
    }

    //função para pesquisar usuários
    fun searchUsers(query: String) {
        val db = Firebase.firestore
        db.collection("usuarios")
            //seja maior ou igual ao valor da query, comparar os valores alfabeticamente
            .whereGreaterThanOrEqualTo("nome_usuario", query)
            //assegura que todos os nomes de usuário começando com o query sejam retornados por busca fuzzy
            .whereLessThanOrEqualTo("nome_usuario", query + "\uf8ff")
            .get()
            .addOnSuccessListener { documents ->
                val users = documents.mapNotNull { it.toObject(Usuario::class.java) }
                _userList.postValue(users)
            }
            .addOnFailureListener {
                _userList.postValue(emptyList())
            }
    }
}
