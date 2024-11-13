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
}
