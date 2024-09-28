package com.aliny.palmpet.viewmodel

import Usuario
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aliny.palmpet.data.model.Pet
import com.aliny.palmpet.data.repository.PetRepository

class PetViewModel : ViewModel() {

    private val _pets = MutableLiveData<List<Pet>>()
    val pets: LiveData<List<Pet>> get() = _pets

    private val _pet = MutableLiveData<Pet?>()  //livedata para um pet individual
    val pet: LiveData<Pet?> get() = _pet

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    fun loadPets(userData: Usuario) {
        PetRepository.getPetsForLoggedUser(userData, { petList ->
            _pets.value = petList
        }, { exception ->
            Log.e("PetViewModel", "Erro ao buscar pets: ${exception.message}")
            _error.value = "Erro ao buscar pets: ${exception.message}"
        })
    }

    fun loadPetById(petId: String) {
        PetRepository.getPetById(petId, { pet ->
            if (pet != null) {
                _pet.value = pet  //atualiza apenas o pet individual
            } else {
                _error.value = "Pet não encontrado"
            }
        }, { exception ->
            Log.e("PetViewModel", "Erro ao buscar pet: ${exception.message}")
            _error.value = "Erro ao buscar pet: ${exception.message}"
        })
    }

    fun clearPets() {
        _pets.value = emptyList()  //limpa a lista de pets
    }

}

