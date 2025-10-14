package test.parserTest

import builders.PrintBuilder
import main.kotlin.lexer.*
import main.kotlin.parser.ParseResult
import org.example.LiteralString
import rules.PrintlnRule
import types.IdentifierType
import types.PrintlnType
import types.PunctuationType
import kotlin.test.*

class ParserRuleTests {
    @Test
    fun printlnRule_should_match_simple_string_pattern() {
        // Arrange - similar a tus tests unitarios del lexer
        val rule = PrintlnRule(PrintBuilder())
        val tokens =
            listOf(
                Token(PrintlnType, "println", 1, 1),
                Token(PunctuationType, "(", 1, 8),
                Token(LiteralString, "\"test\"", 1, 9),
                Token(PunctuationType, ")", 1, 15),
                Token(PunctuationType, ";", 1, 16),
            )

        // Act
        val result = rule.matcher.match(tokens, 0)

        // Assert
        assertNotNull(result)
        assertTrue(result is ParseResult.Success)

        val success = result as ParseResult.Success
        assertEquals(5, success.nextPosition) // consumió todos los tokens
        assertEquals(5, success.node.size) // devolvió todos los tokens
    }

    @Test
    fun printlnRule_should_not_match_without_semicolon() {
        // Arrange
        val rule = PrintlnRule(PrintBuilder())
        val tokens =
            listOf(
                Token(PrintlnType, "println", 1, 1),
                Token(PunctuationType, "(", 1, 8),
                Token(LiteralString, "\"test\"", 1, 9),
                Token(PunctuationType, ")", 1, 15),
                // Falta el ;
            )

        // Act
        val result = rule.matcher.match(tokens, 0)

        // Assert
        assertNull(result) // no debería matchear
    }

    @Test
    fun printlnRule_should_not_match_different_function() {
        // Arrange
        val rule = PrintlnRule(PrintBuilder())
        val tokens =
            listOf(
                Token(IdentifierType, "print", 1, 1), // no es "println"
                Token(PunctuationType, "(", 1, 6),
                Token(LiteralString, "\"test\"", 1, 7),
                Token(PunctuationType, ")", 1, 13),
                Token(PunctuationType, ";", 1, 14),
            )

        // Act
        val result = rule.matcher.match(tokens, 0)

        // Assert
        assertNull(result) // no debería matchear
    }
}
