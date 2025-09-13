package main.kotlin.lexer

import main.kotlin.lexer.*
import types.PunctuationType

fun main() {
    val baseProvider = ConfiguredTokens.providerV1()

    val ignored =
        listOf(
            TokenRule(Regex("\\G[ \\t]+"), PunctuationType, ignore = true), // espacios/tabs
            TokenRule(Regex("\\G(?:\\r?\\n)+"), PunctuationType, ignore = true), // saltos de línea
            TokenRule(Regex("\\G//.*(?:\\r?\\n|$)"), PunctuationType, ignore = true), // comentarios //
        )

    val tokenProvider = TokenProvider(ignored + baseProvider.rules())
    val lexer = DefaultLexer(tokenProvider)

    val code = "x = 2;"

    val tokens = lexer.tokenize(code)

    for (token in tokens) {
        println("${token.type} -> '${token.value}' (linea ${token.line}, col ${token.column})")
    }
}
