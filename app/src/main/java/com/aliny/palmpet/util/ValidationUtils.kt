package com.aliny.palmpet.util

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import java.time.LocalDate

class CampoInvalidoException(message: String) : IllegalArgumentException(message)

object ValidationUtils {

    fun validarCampo(valor: Any, nomeCampo: String) {
        if (valor is String && valor.isBlank()) {
            throw CampoInvalidoException("O campo $nomeCampo não pode estar vazio.")
        } else if (valor is Int && valor <= 0) {
            throw CampoInvalidoException("O campo $nomeCampo deve ser um valor positivo.")
        } else if (valor is Float && valor <= 0) {
            throw CampoInvalidoException("O campo $nomeCampo deve ser um valor positivo.")
        } else if (valor is LocalDate && valor.isAfter(LocalDate.now())) {
            throw CampoInvalidoException("O campo $nomeCampo deve ser uma data válida.")
        }
    }
}
