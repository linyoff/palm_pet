package com.aliny.palmpet.data.model

class Noticia (val id_noticia : Int, var texto: String){

    override fun toString(): String {
        return "Noticia: $texto"
    }
}