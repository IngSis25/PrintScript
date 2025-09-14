package factory

import main.kotlin.lexer.DefaultLexer
import main.kotlin.lexer.Lexer

class LexerFactoryV11 : LexerFactory {
    override fun create(): Lexer {
        val provider = ConfiguredTokens.providerV11()

        return DefaultLexer(provider)
    }
}
