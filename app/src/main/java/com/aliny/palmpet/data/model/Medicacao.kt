package com.aliny.palmpet.data.model
import com.google.firebase.Timestamp

data class Medicacao(
    val id_medicacao: String = "",
    var id_pet: String = "",
    var id_tutor1: String = "",
    var id_tutor2: String? = null,
    var nome: String = "",
    var tipo: String = "",
    var data: Timestamp = Timestamp.now(),
    var dose_reforco: Timestamp? = null,
    var observacoes: String = "",
    var lembrete: Boolean = false
) {
    //construtor padrão sem argumentos
    constructor() : this("", "", "", null, "", "", Timestamp.now(), null, "", false)

    override fun toString(): String {
        return "Nome da medicação: $nome\n" +
                "ID Pet: $id_pet\n" +
                "Tutor 1: $id_tutor1\n" +
                "Tutor 2: $id_tutor2\n" +
                "Tipo: $tipo\n" +
                "Data: $data\n" +
                "Dose de Reforço: $dose_reforco\n" +
                "Observações: $observacoes\n" +
                "Lembrete: $lembrete\n"
    }
}
