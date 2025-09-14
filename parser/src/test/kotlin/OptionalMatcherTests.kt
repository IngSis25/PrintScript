package test.parserTest

import TokenMatcher
import main.kotlin.lexer.*
import main.kotlin.parser.ParseResult
import matchers.OptionalMatcher
import org.example.LiteralNumber
import org.example.LiteralString
import types.*
import kotlin.test.*

class OptionalMatcherTests {
    private val identifierMatcher = TokenMatcher(IdentifierType)
    private val optionalMatcher = OptionalMatcher(identifierMatcher)

    @Test
    fun should_match_when_inner_matcher_succeeds() {
        val tokens =
            listOf(
                Token(IdentifierType, "test", 1, 1),
                Token(PunctuationType, ";", 1, 5),
            )

        val result = optionalMatcher.match(tokens, 0)

        assertNotNull(result)
        assertTrue(result is ParseResult.Success)
        val success = result as ParseResult.Success
        assertNotNull(success.node)
        assertEquals(1, success.nextPosition)
    }

    @Test
    fun should_succeed_with_null_when_inner_matcher_fails() {
        val tokens =
            listOf(
                Token(LiteralNumber, "42", 1, 1),
                Token(PunctuationType, ";", 1, 3),
            )

        val result = optionalMatcher.match(tokens, 0)

        assertNotNull(result)
        assertTrue(result is ParseResult.Success)
        val success = result as ParseResult.Success
        assertNull(success.node)
        assertEquals(0, success.nextPosition) // Position doesn't advance
    }

    @Test
    fun should_succeed_with_null_when_inner_matcher_returns_failure() {
        val tokens =
            listOf(
                Token(PunctuationType, ";", 1, 1),
            )

        val result = optionalMatcher.match(tokens, 0)

        assertNotNull(result)
        assertTrue(result is ParseResult.Success)
        val success = result as ParseResult.Success
        assertNull(success.node)
        assertEquals(0, success.nextPosition) // Position doesn't advance
    }

    @Test
    fun should_succeed_with_null_when_position_out_of_bounds() {
        val tokens =
            listOf(
                Token(IdentifierType, "test", 1, 1),
            )

        val result = optionalMatcher.match(tokens, 1)

        assertNotNull(result)
        assertTrue(result is ParseResult.Success)
        val success = result as ParseResult.Success
        assertNull(success.node)
        assertEquals(1, success.nextPosition) // Position doesn't advance
    }

    @Test
    fun should_succeed_with_null_when_empty_token_list() {
        val tokens = emptyList<Token>()

        val result = optionalMatcher.match(tokens, 0)

        assertNotNull(result)
        assertTrue(result is ParseResult.Success)
        val success = result as ParseResult.Success
        assertNull(success.node)
        assertEquals(0, success.nextPosition) // Position doesn't advance
    }

    @Test
    fun should_match_from_different_position() {
        val tokens =
            listOf(
                Token(LiteralNumber, "42", 1, 1),
                Token(IdentifierType, "test", 1, 4),
                Token(PunctuationType, ";", 1, 8),
            )

        val result = optionalMatcher.match(tokens, 1)

        assertNotNull(result)
        assertTrue(result is ParseResult.Success)
        val success = result as ParseResult.Success
        assertNotNull(success.node)
        assertEquals(2, success.nextPosition)
    }

    @Test
    fun should_work_with_different_matcher_types() {
        val stringMatcher = TokenMatcher(LiteralString)
        val optionalStringMatcher = OptionalMatcher(stringMatcher)

        val tokens =
            listOf(
                Token(LiteralString, "\"hello\"", 1, 1),
                Token(PunctuationType, ";", 1, 8),
            )

        val result = optionalStringMatcher.match(tokens, 0)

        assertNotNull(result)
        assertTrue(result is ParseResult.Success)
        val success = result as ParseResult.Success
        assertNotNull(success.node)
        assertEquals(1, success.nextPosition)
    }

    @Test
    fun should_work_with_complex_matcher() {
        val sequenceMatcher =
            matchers.SequenceMatcher(
                listOf(
                    TokenMatcher(IdentifierType),
                    TokenMatcher(AssignmentType),
                    TokenMatcher(LiteralNumber),
                ),
            )
        val optionalSequenceMatcher = OptionalMatcher(sequenceMatcher)

        val tokens =
            listOf(
                Token(IdentifierType, "x", 1, 1),
                Token(AssignmentType, "=", 1, 3),
                Token(LiteralNumber, "42", 1, 5),
                Token(PunctuationType, ";", 1, 7),
            )

        val result = optionalSequenceMatcher.match(tokens, 0)

        assertNotNull(result)
        assertTrue(result is ParseResult.Success)
        val success = result as ParseResult.Success
        assertNotNull(success.node)
        assertEquals(3, success.nextPosition)
    }

    @Test
    fun should_handle_nested_optional_matchers() {
        val innerOptionalMatcher = OptionalMatcher(TokenMatcher(LiteralNumber))
        val outerOptionalMatcher = OptionalMatcher(innerOptionalMatcher)

        val tokens =
            listOf(
                Token(LiteralNumber, "42", 1, 1),
                Token(PunctuationType, ";", 1, 3),
            )

        val result = outerOptionalMatcher.match(tokens, 0)

        assertNotNull(result)
        assertTrue(result is ParseResult.Success)
        val success = result as ParseResult.Success
        assertNotNull(success.node)
        assertEquals(1, success.nextPosition)
    }
}
