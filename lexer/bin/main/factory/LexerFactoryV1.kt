package factory
import main.kotlin.lexer.DefaultLexer
import main.kotlin.lexer.Lexer

class LexerFactoryV1 : LexerFactory {
    override fun create(): Lexer {
        val provider = ConfiguredTokens.providerV1()

        return DefaultLexer(provider)
    }
}

// lo que se repite en ambas factories, se va a repetir siempre, abstraer
