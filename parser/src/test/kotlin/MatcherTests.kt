package test.parserTest

import main.kotlin.lexer.*
import main.kotlin.parser.ParseResult
import matchers.TokenMatcher
import org.example.LiteralNumber
import types.PunctuationType
import kotlin.test.*

class MatcherTests {
    @Test
    fun tokenMatcher_should_match_correct_token_type() {
        // Arrange - similar a tus tests de tipos
        val matcher = TokenMatcher(IdentifierType)
        val tokens =
            listOf(
                Token(IdentifierType, "variable", 1, 1),
                Token(PunctuationType, ";", 1, 9),
            )

        // Act
        val result = matcher.match(tokens, 0)

        // Assert
        assertNotNull(result)
        assertTrue(result is ParseResult.Success)

        val success = result as ParseResult.Success
        assertEquals(tokens[0], success.node) // devuelve el token correcto
        assertEquals(1, success.nextPosition) // avanza una posición
    }

    @Test
    fun tokenMatcher_should_not_match_wrong_token_type() {
        // Arrange
        val matcher = TokenMatcher(IdentifierType)
        val tokens =
            listOf(
                Token(LiteralNumber, "123", 1, 1), // tipo incorrecto
                Token(PunctuationType, ";", 1, 4),
            )

        // Act
        val result = matcher.match(tokens, 0)

        // Assert
        assertNull(result) // no debería matchear
    }

    @Test
    fun tokenMatcher_should_return_null_when_no_tokens_left() {
        // Arrange
        val matcher = TokenMatcher(IdentifierType)
        val tokens = listOf<Token>() // lista vacía

        // Act
        val result = matcher.match(tokens, 0)

        // Assert
        assertNull(result) // no hay tokens para matchear
    }

    @Test
    fun tokenMatcher_should_return_null_when_position_out_of_bounds() {
        // Arrange
        val matcher = TokenMatcher(IdentifierType)
        val tokens =
            listOf(
                Token(IdentifierType, "variable", 1, 1),
            )

        // Act
        val result = matcher.match(tokens, 5) // posición fuera de rango

        // Assert
        assertNull(result) // posición inválida
    }
}
