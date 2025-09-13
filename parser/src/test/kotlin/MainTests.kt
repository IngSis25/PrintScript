package test.parserTest

import main.kotlin.lexer.*
import org.example.LiteralNumber
import types.ModifierType
import types.PunctuationType
import kotlin.test.*

class MainTests {
    @Test
    fun main_should_handle_whitespace_and_comments() {
        // Arrange
        val baseProvider = ConfiguredTokens.providerV1()

        val ignored =
            listOf(
                TokenRule(Regex("\\G[ \\t]+"), PunctuationType, ignore = true),
                TokenRule(Regex("\\G(?:\\r?\\n)+"), PunctuationType, ignore = true),
                TokenRule(Regex("\\G//.*(?:\\r?\\n|$)"), PunctuationType, ignore = true),
            )

        val tokenProvider = TokenProvider(ignored + baseProvider.rules())
        val lexer = DefaultLexer(tokenProvider)

        val code =
            """
            // This is a comment
            let x = 42;    // Another comment
            """.trimIndent()

        // Act
        val tokens = lexer.tokenize(code)

        // Assert
        assertTrue(tokens.isNotEmpty())

        // Comments should be ignored, so we should only have meaningful tokens
        val meaningfulTokens = tokens.filter { !it.value.startsWith("//") }
        assertTrue(meaningfulTokens.any { it.type == ModifierType })
        assertTrue(meaningfulTokens.any { it.type == IdentifierType })
        assertTrue(meaningfulTokens.any { it.type == LiteralNumber })
    }

    @Test
    fun main_should_parse_multiple_statements() {
        // Arrange
        val baseProvider = ConfiguredTokens.providerV1()

        val ignored =
            listOf(
                TokenRule(Regex("\\G[ \\t]+"), PunctuationType, ignore = true),
                TokenRule(Regex("\\G(?:\\r?\\n)+"), PunctuationType, ignore = true),
                TokenRule(Regex("\\G//.*(?:\\r?\\n|$)"), PunctuationType, ignore = true),
            )

        val tokenProvider = TokenProvider(ignored + baseProvider.rules())
        val lexer = DefaultLexer(tokenProvider)

        val code =
            """
            let a = 1;
            let b = 2;
            println(a + b);
            """.trimIndent()

        // Act
        val tokens = lexer.tokenize(code)

        // Assert
        assertTrue(tokens.isNotEmpty())

        // Should have multiple variable declarations and a print statement
        val semicolons = tokens.count { it.value == ";" }
        assertTrue(semicolons >= 3) // At least 3 statements
    }

    @Test
    fun main_should_handle_empty_code() {
        // Arrange
        val baseProvider = ConfiguredTokens.providerV1()

        val ignored =
            listOf(
                TokenRule(Regex("\\G[ \\t]+"), PunctuationType, ignore = true),
                TokenRule(Regex("\\G(?:\\r?\\n)+"), PunctuationType, ignore = true),
                TokenRule(Regex("\\G//.*(?:\\r?\\n|$)"), PunctuationType, ignore = true),
            )

        val tokenProvider = TokenProvider(ignored + baseProvider.rules())
        val lexer = DefaultLexer(tokenProvider)

        val code = ""

        // Act
        val tokens = lexer.tokenize(code)

        // Assert
        assertTrue(tokens.isEmpty())
    }
}
