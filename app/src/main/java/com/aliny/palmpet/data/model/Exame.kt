package com.aliny.palmpet.data.model

import java.io.File
import java.time.LocalDate

class Exame(val id_exame : Int,
            val id_veterinario : Int,
            val id_pet : Int,
            val id_tutor1 : Int,
            val id_tutor2 : Int,
            var data : LocalDate,
            var tipo : String,
            var laboratorio : String,
            var observacoes : String,
            var resultado : File
) {


}