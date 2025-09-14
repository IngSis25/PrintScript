package factory

import DefaultLexer
import lexer.TokenRule
import main.kotlin.lexer.Lexer
import main.kotlin.lexer.TokenProvider
import types.PunctuationType

class LexerFactoryV11 : LexerFactory {
    override fun create(): Lexer {
        val ignored =
            listOf(
                TokenRule(Regex("\\G[ \\t]+"), PunctuationType, ignore = true),
                TokenRule(Regex("\\G(?:\\r?\\n)+"), PunctuationType, ignore = true),
                TokenRule(Regex("\\G//.*(?:\\r?\\n|$)"), PunctuationType, ignore = true),
            )
        val provider = TokenProvider(ignored + ConfiguredTokens.providerV11().rules())
        return DefaultLexer(provider)
    }
}
