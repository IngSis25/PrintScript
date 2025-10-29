package test.parserTest

import main.kotlin.lexer.*
import main.kotlin.parser.ParseResult
import matchers.BlockMatcher
import types.*
import kotlin.test.*

class BlockMatcherTests {
    private val blockMatcher = BlockMatcher()

    @Test
    fun should_match_simple_block() {
        val tokens =
            listOf(
                Token(PunctuationType, "{", 1, 1),
                Token(IdentifierType, "x", 1, 3),
                Token(PunctuationType, "}", 1, 4),
            )

        val result = blockMatcher.match(tokens, 0)

        assertNotNull(result)
        assertTrue(result is ParseResult.Success)
        val success = result as ParseResult.Success
        assertEquals(1, success.node.size) // Only the inner token
        assertEquals(3, success.nextPosition)
    }

    @Test
    fun should_match_empty_block() {
        val tokens =
            listOf(
                Token(PunctuationType, "{", 1, 1),
                Token(PunctuationType, "}", 1, 2),
            )

        val result = blockMatcher.match(tokens, 0)

        assertNotNull(result)
        assertTrue(result is ParseResult.Success)
        val success = result as ParseResult.Success
        assertEquals(0, success.node.size) // No inner tokens
        assertEquals(2, success.nextPosition)
    }

    @Test
    fun should_match_block_with_multiple_statements() {
        val tokens =
            listOf(
                Token(PunctuationType, "{", 1, 1),
                Token(IdentifierType, "x", 1, 3),
                Token(AssignmentType, "=", 1, 5),
                Token(LiteralNumber, "5", 1, 7),
                Token(PunctuationType, ";", 1, 8),
                Token(PunctuationType, "}", 1, 10),
            )

        val result = blockMatcher.match(tokens, 0)

        assertNotNull(result)
        assertTrue(result is ParseResult.Success)
        val success = result as ParseResult.Success
        assertEquals(4, success.node.size) // x, =, 5, ;
        assertEquals(6, success.nextPosition)
    }

    @Test
    fun should_not_match_without_opening_brace() {
        val tokens =
            listOf(
                Token(IdentifierType, "x", 1, 1),
                Token(PunctuationType, "}", 1, 2),
            )

        val result = blockMatcher.match(tokens, 0)

        assertNull(result)
    }

    @Test
    fun should_not_match_without_closing_brace() {
        val tokens =
            listOf(
                Token(PunctuationType, "{", 1, 1),
                Token(IdentifierType, "x", 1, 3),
            )

        val result = blockMatcher.match(tokens, 0)

        assertNotNull(result)
        assertTrue(result is ParseResult.Failure)
        val failure = result as ParseResult.Failure
        assertTrue(failure.message.contains("Se esperaba '}'"))
    }

    @Test
    fun should_not_match_wrong_opening_token() {
        val tokens =
            listOf(
                Token(IdentifierType, "x", 1, 1),
                Token(PunctuationType, "}", 1, 2),
            )

        val result = blockMatcher.match(tokens, 0)

        assertNull(result)
    }

    @Test
    fun should_not_match_wrong_opening_token_type() {
        val tokens =
            listOf(
                Token(PunctuationType, "(", 1, 1),
                Token(IdentifierType, "x", 1, 3),
                Token(PunctuationType, "}", 1, 4),
            )

        val result = blockMatcher.match(tokens, 0)

        assertNull(result)
    }

    @Test
    fun should_not_match_when_position_out_of_bounds() {
        val tokens =
            listOf(
                Token(PunctuationType, "{", 1, 1),
                Token(PunctuationType, "}", 1, 2),
            )

        val result = blockMatcher.match(tokens, 2)

        assertNull(result)
    }

    @Test
    fun should_not_match_empty_token_list() {
        val tokens = emptyList<Token>()

        val result = blockMatcher.match(tokens, 0)

        assertNull(result)
    }

    @Test
    fun should_match_from_different_position() {
        val tokens =
            listOf(
                Token(IdentifierType, "before", 1, 1),
                Token(PunctuationType, "{", 1, 8),
                Token(IdentifierType, "x", 1, 10),
                Token(PunctuationType, "}", 1, 11),
            )

        val result = blockMatcher.match(tokens, 1)

        assertNotNull(result)
        assertTrue(result is ParseResult.Success)
        val success = result as ParseResult.Success
        assertEquals(1, success.node.size) // Only the inner token
        assertEquals(4, success.nextPosition)
    }
}
