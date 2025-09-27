package test.lexerTest

import main.kotlin.lexer.DefaultLexer
import main.kotlin.lexer.TokenProvider
import main.kotlin.lexer.TokenRule
import org.example.TokenType
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class DefaultLexerTest {
    @Test
    fun tokenize_let_keyword_with_no_whitespace() {
        val keywordType =
            object : TokenType {
                override val name = "KEYWORD"
            }

        val keywordRule = TokenRule(Regex("let\\b"), keywordType, ignore = false)

        val provider = TokenProvider(listOf(keywordRule))

        val lexer = DefaultLexer(provider)

        val tokens = lexer.tokenize("let")

        assertEquals(1, tokens.size)
        assertEquals("let", tokens[0].value)
        assertEquals("KEYWORD", tokens[0].type.name)
        assertEquals(1, tokens[0].line)
        assertEquals(1, tokens[0].column)
    }
}
