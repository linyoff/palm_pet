import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.*

data class Usuario(
    var id_usuario: String = "",
    var nome_usuario: String = "",
    var nome: String = "",
    var data_nascimento: Timestamp = Timestamp.now(),
    var telefone: String = "",
    var email: String = "",
    var imageUrl: String? = null
) {
    // Construtor padrão sem argumentos
    constructor() : this("", "", "", Timestamp.now(), "", "", null)

    override fun toString(): String {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val dataNascimentoStr = dateFormat.format(data_nascimento.toDate())

        return "ID de usuário: $id_usuario\n" +
                "Nome de usuário: $nome_usuario\n" +
                "Nome: $nome\n" +
                "Data de nascimento: $dataNascimentoStr\n" +
                "Telefone: $telefone\n" +
                "Email: $email\n"
    }
}

