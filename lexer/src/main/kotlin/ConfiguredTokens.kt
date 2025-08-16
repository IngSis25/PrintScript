package main.kotlin.lexer

import lexer.AssignmentType
import lexer.ModifierType

object ConfiguredTokens{

    // Versión 1 configuración de tokens
    val V1: Map<String, TokenType> = linkedMapOf(
        // Palabras clave
        "\\bnumber\\b"            to NumberType,
        "\\bstring\\b"            to StringType,
        "\\bconst\\b|\\blet\\b|\\bvar\\b" to ModifierType, // modificadores

        // Operadores y asignación
        "="                       to AssignmentType,       // asignación
        "==|!=|<=|>="             to OperatorType,         // comparaciones
        "[+\\-*/<>]"              to OperatorType,         // operadores simples

        // Literales
        "\"([^\"\\\\]|\\\\.)*\""  to LiteralString,         // cadenas con escapes
        "[0-9]+(?:\\.[0-9]+)?"    to LiteralNumber,         // números enteros/decimales

        // Identificadores
        "[A-Za-z_][A-Za-z_0-9]*"  to IdentifierType,        // variables, funciones

        // Puntuación
        ";"                       to PunctuationType,
        "\\("                     to PunctuationType, // paréntesis de apertura
        "\\)"                     to PunctuationType, // paréntesis de cierre
    )

    // Método para obtener el provider listo para usar
    fun providerV1(): TokenProvider = TokenProvider.fromMap(V1)
}
