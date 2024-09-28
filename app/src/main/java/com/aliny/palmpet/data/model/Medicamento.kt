package com.aliny.palmpet.data.model
import java.time.LocalDate

class Medicamento (val id_medicamento : Int,
                   val id_pet : Int,
                   val id_tutor1 : Int,
                   val id_tutor2 : Int,
                   var nome : String,
                   var tipo : String,
                   var data: LocalDate,
                   var dose_reforco : LocalDate,
                   var observacoes : String,
                   var lembrete : Boolean) {

}