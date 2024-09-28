package com.aliny.palmpet.data.model

import java.time.LocalDate

class Consulta (val id_consulta : Int,
                var id_pet : Int,
                var id_tutor1 : Int,
                var id_tutor2 : Int,
                var id_veterinario : Int,
                var data : LocalDate,
                var observacoes : String){

    override fun toString(): String {
        return "Consulta: " +
                "Pet: $id_pet\n" +
                "Tutor: $id_tutor1\n" +
                "Tutor 2: $id_tutor2\n" +
                "Veterinário: $id_veterinario\n" +
                "Data: $data\n" +
                "Observações: $observacoes"
    }
}