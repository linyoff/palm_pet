package com.aliny.palmpet.data.model

class Veterinario (val id_veterinario : String,
                   var nome: String,
                   var clinica : String,
                   var telefone : String,
                   var email : String,
                   var endereco : String,
                   var pontuacao : Int){

    override fun toString(): String {
        return "Veterinario: " +
                "Nome: $nome\n" +
                "Clinica: $clinica\n" +
                "Telefone: $telefone\n" +
                "Email: $email\n" +
                "Endereco: $endereco\n" +
                "Pontuacao: $pontuacao\n"
    }

}