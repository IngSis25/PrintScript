package factory

import main.kotlin.lexer.Lexer

// cada factory usa sus reglas de configuredTokens
interface LexerFactory {
    fun create(): Lexer
}
