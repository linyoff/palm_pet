package com.aliny.palmpet.data.model

import java.time.LocalDate


class MedContinuo (val id_medCont : Int,
                   val id_pet : Int,
                   val id_tutor1 : Int,
                   val id_tutor2 : Int,
                   var nome : String,
                   var tipo : String,
                   var data: LocalDate,
                   var horario : LocalDate,
                   var observacoes : String,
                   var lembrete : Boolean,
                   var check : Boolean) {

}