package com.aliny.palmpet.data.model

import java.time.LocalDate

class Compromisso (val id_compromisso : Int,
                   val id_pet : Int,
                   val id_tutor1 : Int,
                   val id_tutor2 : Int,
                   var tipo : String,
                   var data : LocalDate,
                   var lembrete : Boolean,
                   var endereco : String,
                   var descricao : String){

}