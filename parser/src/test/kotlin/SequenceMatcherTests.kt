package test.parserTest

import main.kotlin.lexer.*
import main.kotlin.parser.ParseResult
import matchers.SequenceMatcher
import matchers.TokenMatcher
import types.IdentifierType
import types.OperatorType
import kotlin.test.*

class SequenceMatcherTests {
    @Test
    fun sequenceMatcher_should_match_correct_token_sequence() {
        // Arrange - matcher para patrón: number + number
        val matcher =
            SequenceMatcher(
                listOf(
                    TokenMatcher(LiteralNumber),
                    TokenMatcher(OperatorType),
                    TokenMatcher(LiteralNumber),
                ),
            )

        val tokens =
            listOf(
                Token(LiteralNumber, "12", 1, 1),
                Token(OperatorType, "+", 1, 4),
                Token(LiteralNumber, "8", 1, 6),
            )

        // Act
        val result = matcher.match(tokens, 0)

        // Assert
        assertNotNull(result)
        assertTrue(result is ParseResult.Success)

        val success = result as ParseResult.Success
        assertEquals(3, success.nextPosition) // consumió los 3 tokens
        assertEquals(3, success.node.size) // devolvió los 3 tokens
    }

    @Test
    fun sequenceMatcher_should_not_match_incomplete_sequence() {
        // Arrange - matcher espera 3 tokens pero solo hay 2
        val matcher =
            SequenceMatcher(
                listOf(
                    TokenMatcher(LiteralNumber),
                    TokenMatcher(OperatorType),
                    TokenMatcher(LiteralNumber),
                ),
            )

        val tokens =
            listOf(
                Token(LiteralNumber, "12", 1, 1),
                Token(OperatorType, "+", 1, 4),
                // Falta el tercer token
            )

        // Act
        val result = matcher.match(tokens, 0)

        // Assert
        assertNull(result) // no debería matchear
    }

    @Test
    fun sequenceMatcher_should_not_match_wrong_token_types() {
        // Arrange - matcher para number + number, pero hay string + number
        val matcher =
            SequenceMatcher(
                listOf(
                    TokenMatcher(LiteralNumber),
                    TokenMatcher(OperatorType),
                    TokenMatcher(LiteralNumber),
                ),
            )

        val tokens =
            listOf(
                Token(LiteralString, "\"12\"", 1, 1), // tipo incorrecto
                Token(OperatorType, "+", 1, 5),
                Token(LiteralNumber, "8", 1, 7),
            )

        // Act
        val result = matcher.match(tokens, 0)

        // Assert
        assertNull(result) // no debería matchear
    }

    @Test
    fun sequenceMatcher_should_match_single_token_sequence() {
        // Arrange - matcher para un solo token
        val matcher =
            SequenceMatcher(
                listOf(
                    TokenMatcher(IdentifierType),
                ),
            )

        val tokens =
            listOf(
                Token(IdentifierType, "variable", 1, 1),
            )

        // Act
        val result = matcher.match(tokens, 0)

        // Assert
        assertNotNull(result)
        assertTrue(result is ParseResult.Success)

        val success = result as ParseResult.Success
        assertEquals(1, success.nextPosition)
        assertEquals(1, success.node.size)
    }

    @Test
    fun sequenceMatcher_should_work_from_different_positions() {
        // Arrange - matcher para identifier
        val matcher =
            SequenceMatcher(
                listOf(
                    TokenMatcher(IdentifierType),
                ),
            )

        val tokens =
            listOf(
                Token(LiteralNumber, "123", 1, 1),
                Token(IdentifierType, "variable", 1, 5), // posición 1
                Token(OperatorType, "+", 1, 13),
            )

        // Act - empezar desde posición 1
        val result = matcher.match(tokens, 1)

        // Assert
        assertNotNull(result)
        assertTrue(result is ParseResult.Success)

        val success = result as ParseResult.Success
        assertEquals(2, success.nextPosition) // de posición 1 a 2
    }
}
