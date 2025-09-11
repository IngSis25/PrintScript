package test.parserTest

import DefaultParser
import builders.PrintBuilder
import main.kotlin.lexer.*
import org.example.LiteralNumber
import rules.PrintlnRule
import rules.RuleMatcher
import types.PunctuationType
import kotlin.test.*

class DefaultParserFailureTests {
    @Test
    fun parse_should_throw_exception_on_syntax_error() {
        // Arrange - tokens that don't match any rule
        val tokens =
            listOf(
                Token(IdentifierType, "invalid", 1, 1),
                Token(IdentifierType, "syntax", 1, 9),
            )

        val ruleMatcher = RuleMatcher(listOf(PrintlnRule(PrintBuilder())))
        val parser = DefaultParser(ruleMatcher)

        // Act & Assert
        val exception =
            assertFailsWith<IllegalArgumentException> {
                parser.parse(tokens)
            }

        assertTrue(exception.message!!.contains("Syntax error"))
        assertTrue(exception.message!!.contains("position 0"))
    }

    @Test
    fun parse_should_throw_exception_with_correct_position() {
        // Arrange - tokens that start valid but become invalid
        val tokens =
            listOf(
                Token(IdentifierType, "println", 1, 1),
                Token(PunctuationType, "(", 1, 8),
                Token(LiteralNumber, "42", 1, 9),
                Token(IdentifierType, "invalid", 1, 12), // This should cause failure
            )

        val ruleMatcher = RuleMatcher(listOf(PrintlnRule(PrintBuilder())))
        val parser = DefaultParser(ruleMatcher)

        // Act & Assert
        val exception =
            assertFailsWith<IllegalArgumentException> {
                parser.parse(tokens)
            }

        assertTrue(exception.message!!.contains("Syntax error"))
        assertTrue(exception.message!!.contains("position 0"))
    }

    @Test
    fun parse_should_handle_empty_token_list() {
        // Arrange
        val tokens = emptyList<Token>()
        val ruleMatcher = RuleMatcher(listOf(PrintlnRule(PrintBuilder())))
        val parser = DefaultParser(ruleMatcher)

        // Act
        val result = parser.parse(tokens)

        // Assert
        assertTrue(result.isEmpty())
    }

    @Test
    fun parse_should_handle_single_invalid_token() {
        // Arrange
        val tokens =
            listOf(
                Token(IdentifierType, "unknown", 1, 1),
            )

        val ruleMatcher = RuleMatcher(listOf(PrintlnRule(PrintBuilder())))
        val parser = DefaultParser(ruleMatcher)

        // Act & Assert
        val exception =
            assertFailsWith<IllegalArgumentException> {
                parser.parse(tokens)
            }

        assertTrue(exception.message!!.contains("Syntax error"))
    }
}
