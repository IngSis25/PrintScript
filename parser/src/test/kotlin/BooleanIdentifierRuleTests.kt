package test.parserTest

import builders.ExpressionBuilder
import main.kotlin.lexer.*
import main.kotlin.parser.ParseResult
import rules.booleanExpressions.BooleanIdentifierRule
import types.*
import kotlin.test.*

class BooleanIdentifierRuleTests {
    private val booleanIdentifierRule = BooleanIdentifierRule(ExpressionBuilder())

    @Test
    fun should_match_simple_identifier() {
        val tokens =
            listOf(
                Token(IdentifierType, "isValid", 1, 1),
            )

        val result = booleanIdentifierRule.matcher.match(tokens, 0)

        assertNotNull(result)
        assertTrue(result is ParseResult.Success)
        val success = result as ParseResult.Success
        assertEquals(1, success.nextPosition)
    }

    @Test
    fun should_match_boolean_like_identifier() {
        val tokens =
            listOf(
                Token(IdentifierType, "hasPermission", 1, 1),
            )

        val result = booleanIdentifierRule.matcher.match(tokens, 0)

        assertNotNull(result)
        assertTrue(result is ParseResult.Success)
        val success = result as ParseResult.Success
        assertEquals(1, success.nextPosition)
    }

    @Test
    fun should_match_single_character_identifier() {
        val tokens =
            listOf(
                Token(IdentifierType, "x", 1, 1),
            )

        val result = booleanIdentifierRule.matcher.match(tokens, 0)

        assertNotNull(result)
        assertTrue(result is ParseResult.Success)
        val success = result as ParseResult.Success
        assertEquals(1, success.nextPosition)
    }

    @Test
    fun should_not_match_non_identifier_token() {
        val tokens =
            listOf(
                Token(LiteralNumber, "42", 1, 1),
            )

        val result = booleanIdentifierRule.matcher.match(tokens, 0)

        assertNull(result)
    }

    @Test
    fun should_not_match_string_literal() {
        val tokens =
            listOf(
                Token(LiteralString, "\"hello\"", 1, 1),
            )

        val result = booleanIdentifierRule.matcher.match(tokens, 0)

        assertNull(result)
    }

    @Test
    fun should_not_match_operator() {
        val tokens =
            listOf(
                Token(OperatorType, "+", 1, 1),
            )

        val result = booleanIdentifierRule.matcher.match(tokens, 0)

        assertNull(result)
    }

    @Test
    fun should_not_match_punctuation() {
        val tokens =
            listOf(
                Token(PunctuationType, ";", 1, 1),
            )

        val result = booleanIdentifierRule.matcher.match(tokens, 0)

        assertNull(result)
    }

    @Test
    fun should_not_match_empty_token_list() {
        val tokens = emptyList<Token>()

        val result = booleanIdentifierRule.matcher.match(tokens, 0)

        assertNull(result)
    }

    @Test
    fun should_not_match_when_position_out_of_bounds() {
        val tokens =
            listOf(
                Token(IdentifierType, "test", 1, 1),
            )

        val result = booleanIdentifierRule.matcher.match(tokens, 1)

        assertNull(result)
    }
}
