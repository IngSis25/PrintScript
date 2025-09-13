import types.IdentifierType
import main.kotlin.lexer.Token
import main.kotlin.lexer.TokenProvider
import org.example.LiteralNumber
import org.example.LiteralString
import org.example.TokenType
import types.*

object ConfiguredTokens {
    // Versión 1 configuración de tokens
    val V1: Map<String, TokenType> =
        linkedMapOf(
            // Palabras clave
            "\\bnumber\\b" to NumberType,
            "\\bstring\\b" to StringType,
            "\\blet\\b|\\bvar\\b" to ModifierType, // modificadores

            // Operadores y asignación
            "=" to AssignmentType, // asignación
            "==|!=|<=|>=" to OperatorType, // comparaciones
            "[+\\-*/<>]" to OperatorType, // operadores simples
            // Literales
            "\"([^\"\\\\]|\\\\.)*\"" to LiteralString, // cadenas con escapes
            "[0-9]+(?:\\.[0-9]+)?" to LiteralNumber, // números enteros/decimales
            // Identificadores
            "[A-Za-z_][A-Za-z_0-9]*" to IdentifierType, // variables, funciones
            // Puntuación
            ";" to PunctuationType,
            "\\(" to PunctuationType, // paréntesis de apertura
            "\\)" to PunctuationType, // paréntesis de cierre
        )

    // Método para obtener el provider listo para usar
    fun providerV1(): TokenProvider = TokenProvider.fromMap(V1)

    val V1_1: Map<String, TokenType> =
        V1 + linkedMapOf(
            "\\bif\\b" to IfType, // if
            "\\breadInput\\b" to ReadInputType, // readInput
            "\\bboolean\\b" to BooleanType,
            "\\belse\\b" to ElseType,
            "\\breadEnv\\b" to ReadEnvType,       // función readEnv
            "\\{" to PunctuationType,
            "\\}" to PunctuationType,
            "\\bconst\b" to ModifierType, // vale la pena separar el token type de let y const??
        )

    fun providerV1_1(): TokenProvider = TokenProvider.fromMap(V1_1)

}
