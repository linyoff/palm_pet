package com.aliny.palmpet.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aliny.palmpet.data.model.Medicacao
import com.aliny.palmpet.data.repository.MedRepository

class MedViewModel : ViewModel() {

    private val _medicacoes = MutableLiveData<List<Medicacao>>()
    val medicacoes: LiveData<List<Medicacao>> get() = _medicacoes

    private val _medicacao = MutableLiveData<Medicacao?>()  //LiveData para medicação individual
    val medicacao: LiveData<Medicacao?> get() = _medicacao

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    //carrega as medicações do tutor pelo id do pet
    fun loadMedicacoesByPetId(petId: String) {
        MedRepository.getMedicacoesForPet(petId, { medicacaoList ->
            _medicacoes.value = medicacaoList
        }, { exception ->
            Log.e("MedicacaoViewModel", "Erro ao buscar medicações: ${exception.message}")
            _error.value = "Erro ao buscar medicações: ${exception.message}"
        })
    }

    //carrega uma medicação específica por ID
    fun loadMedicacaoById(medicacaoId: String) {
        MedRepository.getMedicacaoById(medicacaoId, { medicacao ->
            if (medicacao != null) {
                _medicacao.value = medicacao  //atualiza apenas a medicação
            } else {
                _error.value = "Medicação não encontrada"
            }
        }, { exception ->
            Log.e("MedicacaoViewModel", "Erro ao buscar medicação: ${exception.message}")
            _error.value = "Erro ao buscar medicação: ${exception.message}"
        })
    }

}
