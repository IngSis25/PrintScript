package test.parserTest

import builders.ConstBuilder
import main.kotlin.lexer.*
import org.example.LiteralString
import org.example.ast.*
import rules.ConstRule
import types.*
import kotlin.test.*

class ConstRuleTests {
    private val constRule = ConstRule(ConstBuilder())

    @Test
    fun should_not_match_without_const_keyword() {
        val tokens =
            listOf(
                Token(ModifierType, "let", 1, 1),
                Token(IdentifierType, "message", 1, 5),
                Token(AssignmentType, "=", 1, 13),
                Token(LiteralString, "\"hello\"", 1, 15),
                Token(PunctuationType, ";", 1, 22),
            )

        val result = constRule.matcher.match(tokens, 0)

        assertNull(result)
    }

    @Test
    fun should_not_match_without_identifier() {
        val tokens =
            listOf(
                Token(ModifierType, "const", 1, 1),
                Token(AssignmentType, "=", 1, 7),
                Token(LiteralString, "\"hello\"", 1, 9),
                Token(PunctuationType, ";", 1, 16),
            )

        val result = constRule.matcher.match(tokens, 0)

        assertNull(result)
    }

    @Test
    fun should_not_match_without_assignment() {
        val tokens =
            listOf(
                Token(ModifierType, "const", 1, 1),
                Token(IdentifierType, "message", 1, 7),
                Token(LiteralString, "\"hello\"", 1, 15),
                Token(PunctuationType, ";", 1, 22),
            )

        val result = constRule.matcher.match(tokens, 0)

        assertNull(result)
    }

    @Test
    fun should_not_match_without_semicolon() {
        val tokens =
            listOf(
                Token(ModifierType, "const", 1, 1),
                Token(IdentifierType, "message", 1, 7),
                Token(AssignmentType, "=", 1, 15),
                Token(LiteralString, "\"hello\"", 1, 17),
            )

        val result = constRule.matcher.match(tokens, 0)

        assertNull(result)
    }

    @Test
    fun should_match_const_with_identifier_value() {
        val tokens =
            listOf(
                Token(ModifierType, "const", 1, 1),
                Token(IdentifierType, "message", 1, 7),
                Token(AssignmentType, "=", 1, 15),
                Token(IdentifierType, "otherVariable", 1, 17),
                Token(PunctuationType, ";", 1, 24),
            )

        val result = constRule.matcher.match(tokens, 0)

        assertNotNull(result)
        assertTrue(result is main.kotlin.parser.ParseResult.Success)
    }

    @Test
    fun should_not_match_insufficient_tokens() {
        val tokens =
            listOf(
                Token(ModifierType, "const", 1, 1),
                Token(IdentifierType, "message", 1, 7),
            )

        val result = constRule.matcher.match(tokens, 0)

        assertNull(result)
    }
}
