import main.kotlin.lexer.TokenProvider
import main.kotlin.lexer.TokenRule
import org.example.LiteralNumber
import org.example.LiteralString
import org.example.TokenType
import types.*
import types.IdentifierType

object ConfiguredTokens {
    // Reglas que se ignoran (espacios, saltos de línea, comentarios)
    val IGNORED_RULES: List<TokenRule> =
        listOf(
            TokenRule(Regex("\\G[ \\t]+"), PunctuationType, ignore = true),
            TokenRule(Regex("\\G(?:\\r?\\n)+"), PunctuationType, ignore = true),
            TokenRule(Regex("\\G//.*(?:\\r?\\n|$)"), PunctuationType, ignore = true),
            TokenRule(Regex("\\s+"), WhitespaceType, ignore = true),
        )

    // Versión 1 configuración de tokens
    val V1: Map<String, TokenType> =
        linkedMapOf(
            // Palabras clave
            "\\bnumber\\b" to NumberType,
            "\\bstring\\b" to StringType,
            "\\blet\\b|\\bvar\\b" to ModifierType, // modificadores
            "\\bprintln\\b" to PrintlnType, // función println
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
            ":" to PunctuationType, // dos puntos para declaraciones de tipo
            ";" to PunctuationType,
            "\\(" to PunctuationType, // paréntesis de apertura
            "\\)" to PunctuationType, // paréntesis de cierre
        )

    // Método para obtener el provider listo para usar (incluye reglas ignoradas)
    fun providerV1(): TokenProvider = TokenProvider(IGNORED_RULES + TokenProvider.fromMap(V1).rules())

    val V1_1: Map<String, TokenType> =
        linkedMapOf(
            // Palabras clave específicas primero (más específicas)
            "\\bif\\b" to IfType, // if
            "\\belse\\b" to ElseType,
            "\\breadInput\\b" to ReadInputType, // readInput
            "\\breadEnv\\b" to ReadEnvType, // función readEnv
            "\\bBoolean\\b|\\bboolean\\b" to BooleanType,
            "\\btrue\\b|\\bfalse\\b" to LiteralBoolean,
            "\\bconst\\b" to ModifierType, // vale la pena separar el token type de let y const??
            // Palabras clave de V1
            "\\bnumber\\b" to NumberType,
            "\\bstring\\b" to StringType,
            "\\blet\\b|\\bvar\\b" to ModifierType, // modificadores
            "\\bprintln\\b" to PrintlnType, // función println
            // Operadores y asignación
            "=" to AssignmentType, // asignación
            "==|!=|<=|>=" to OperatorType, // comparaciones
            "[+\\-*/<>]" to OperatorType, // operadores simples
            // Literales
            "\"([^\"\\\\]|\\\\.)*\"" to LiteralString, // cadenas con escapes
            "[0-9]+(?:\\.[0-9]+)?" to LiteralNumber, // números enteros/decimales
            // Identificadores (menos específico, va al final)
            "[A-Za-z_][A-Za-z_0-9]*" to IdentifierType, // variables, funciones
            // Puntuación
            ":" to PunctuationType, // dos puntos para declaraciones de tipo
            ";" to PunctuationType,
            "\\(" to PunctuationType, // paréntesis de apertura
            "\\)" to PunctuationType, // paréntesis de cierre
            "\\{" to PunctuationType,
            "\\}" to PunctuationType,
        )

    fun providerV11(): TokenProvider = TokenProvider(IGNORED_RULES + TokenProvider.fromMap(V1_1).rules())
}
