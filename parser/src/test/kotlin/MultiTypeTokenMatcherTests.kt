package test.parserTest

import BooleanType
import main.kotlin.lexer.*
import main.kotlin.parser.ParseResult
import matchers.MultiTypeTokenMatcher
import org.example.LiteralNumber
import org.example.LiteralString
import types.*
import kotlin.test.*

class MultiTypeTokenMatcherTests {
    private val multiTypeMatcher =
        MultiTypeTokenMatcher(
            listOf(LiteralString, LiteralNumber, IdentifierType),
        )

    @Test
    fun should_match_string_literal() {
        val tokens =
            listOf(
                Token(LiteralString, "\"hello\"", 1, 1),
                Token(PunctuationType, ";", 1, 8),
            )

        val result = multiTypeMatcher.match(tokens, 0)

        assertNotNull(result)
        assertTrue(result is ParseResult.Success)
        val success = result as ParseResult.Success
        assertEquals(LiteralString, success.node.type)
        assertEquals("\"hello\"", success.node.value)
        assertEquals(1, success.nextPosition)
    }

    @Test
    fun should_match_number_literal() {
        val tokens =
            listOf(
                Token(LiteralNumber, "42", 1, 1),
                Token(PunctuationType, ";", 1, 3),
            )

        val result = multiTypeMatcher.match(tokens, 0)

        assertNotNull(result)
        assertTrue(result is ParseResult.Success)
        val success = result as ParseResult.Success
        assertEquals(LiteralNumber, success.node.type)
        assertEquals("42", success.node.value)
        assertEquals(1, success.nextPosition)
    }

    @Test
    fun should_match_identifier() {
        val tokens =
            listOf(
                Token(IdentifierType, "variable", 1, 1),
                Token(PunctuationType, ";", 1, 9),
            )

        val result = multiTypeMatcher.match(tokens, 0)

        assertNotNull(result)
        assertTrue(result is ParseResult.Success)
        val success = result as ParseResult.Success
        assertEquals(IdentifierType, success.node.type)
        assertEquals("variable", success.node.value)
        assertEquals(1, success.nextPosition)
    }

    @Test
    fun should_not_match_unsupported_token_type() {
        val tokens =
            listOf(
                Token(OperatorType, "+", 1, 1),
                Token(PunctuationType, ";", 1, 2),
            )

        val result = multiTypeMatcher.match(tokens, 0)

        assertNull(result)
    }

    @Test
    fun should_not_match_punctuation() {
        val tokens =
            listOf(
                Token(PunctuationType, ";", 1, 1),
            )

        val result = multiTypeMatcher.match(tokens, 0)

        assertNull(result)
    }

    @Test
    fun should_not_match_boolean_type() {
        val tokens =
            listOf(
                Token(BooleanType, "true", 1, 1),
            )

        val result = multiTypeMatcher.match(tokens, 0)

        assertNull(result)
    }

    @Test
    fun should_not_match_when_position_out_of_bounds() {
        val tokens =
            listOf(
                Token(LiteralString, "\"hello\"", 1, 1),
            )

        val result = multiTypeMatcher.match(tokens, 1)

        assertNull(result)
    }

    @Test
    fun should_not_match_empty_token_list() {
        val tokens = emptyList<Token>()

        val result = multiTypeMatcher.match(tokens, 0)

        assertNull(result)
    }

    @Test
    fun should_match_from_different_position() {
        val tokens =
            listOf(
                Token(OperatorType, "+", 1, 1),
                Token(LiteralNumber, "42", 1, 3),
                Token(PunctuationType, ";", 1, 5),
            )

        val result = multiTypeMatcher.match(tokens, 1)

        assertNotNull(result)
        assertTrue(result is ParseResult.Success)
        val success = result as ParseResult.Success
        assertEquals(LiteralNumber, success.node.type)
        assertEquals("42", success.node.value)
        assertEquals(2, success.nextPosition)
    }

    @Test
    fun should_work_with_single_type() {
        val singleTypeMatcher = MultiTypeTokenMatcher(listOf(LiteralString))

        val tokens =
            listOf(
                Token(LiteralString, "\"test\"", 1, 1),
                Token(PunctuationType, ";", 1, 7),
            )

        val result = singleTypeMatcher.match(tokens, 0)

        assertNotNull(result)
        assertTrue(result is ParseResult.Success)
        val success = result as ParseResult.Success
        assertEquals(LiteralString, success.node.type)
        assertEquals("\"test\"", success.node.value)
        assertEquals(1, success.nextPosition)
    }

    @Test
    fun should_work_with_empty_type_list() {
        val emptyTypeMatcher = MultiTypeTokenMatcher(emptyList())

        val tokens =
            listOf(
                Token(LiteralString, "\"test\"", 1, 1),
            )

        val result = emptyTypeMatcher.match(tokens, 0)

        assertNull(result)
    }

    @Test
    fun should_work_with_many_types() {
        val manyTypeMatcher =
            MultiTypeTokenMatcher(
                listOf(
                    LiteralString,
                    LiteralNumber,
                    IdentifierType,
                    BooleanType,
                    OperatorType,
                ),
            )

        val tokens =
            listOf(
                Token(BooleanType, "true", 1, 1),
                Token(PunctuationType, ";", 1, 5),
            )

        val result = manyTypeMatcher.match(tokens, 0)

        assertNotNull(result)
        assertTrue(result is ParseResult.Success)
        val success = result as ParseResult.Success
        assertEquals(BooleanType, success.node.type)
        assertEquals("true", success.node.value)
        assertEquals(1, success.nextPosition)
    }
}
