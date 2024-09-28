package com.aliny.palmpet.data.model

import com.google.firebase.Timestamp

data class Pet(
    val id_pet: String = "",
    var id_tutor1: String = "",
    var id_tutor2: String? = null,
    var nome: String = "",
    var data_nascimento: Timestamp = Timestamp.now(),
    var especie: String = "",
    var raca: String = "",
    var castrado: Boolean = false,
    var peso: Float = 0.0f,
    var sexo: String = "",
    var cor: String = "",
    var tipo_pelagem: String = "",
    var ja_cruzou: Boolean? = null,
    var teve_filhote: Boolean? = null,
    var data_cio: Timestamp? = Timestamp.now(),
    var imageUrl: String? = null
) {
    // Construtor padrão sem argumentos
    constructor() : this("", "", null, "", Timestamp.now(), "", "", false, 0.0f, "", "", "", null, null, null, null)

    override fun toString(): String {
        return "Nome do Pet: $nome\n" +
                "Tutor 1: $id_tutor1\n" +
                "Tutor 2: $id_tutor2\n" +
                "Data de Nascimento: $data_nascimento\n" +
                "Espécie: $especie\n" +
                "Raça: $raca\n" +
                "Castrado: $castrado\n" +
                "Peso: $peso\n" +
                "Sexo: $sexo\n" +
                "Cor: $cor\n" +
                "Tipo de pelagem: $tipo_pelagem\n" +
                "Já cruzou: $ja_cruzou\n" +
                "Já teve filhote: $teve_filhote\n" +
                "Data do cio: $data_cio\n"
    }
}
