package test.parserTest

import builders.PrintBuilder
import main.kotlin.lexer.*
import org.example.LiteralString
import rules.MatchedRule
import rules.PrintlnRule
import types.PunctuationType
import kotlin.test.*

class MatchedRuleTests {
    @Test
    fun matchedRule_should_contain_rule_and_tokens() {
        // Arrange
        val rule = PrintlnRule(PrintBuilder())
        val tokens =
            listOf(
                Token(IdentifierType, "println", 1, 1),
                Token(PunctuationType, "(", 1, 8),
                Token(LiteralString, "\"test\"", 1, 9),
                Token(PunctuationType, ")", 1, 15),
                Token(PunctuationType, ";", 1, 16),
            )

        // Act
        val matchedRule = MatchedRule(rule, tokens)

        // Assert
        assertEquals(rule, matchedRule.rule)
        assertEquals(tokens, matchedRule.matchedTokens)
        assertEquals(5, matchedRule.matchedTokens.size)
    }

    @Test
    fun matchedRule_should_allow_empty_tokens() {
        // Arrange
        val rule = PrintlnRule(PrintBuilder())
        val tokens = emptyList<Token>()

        // Act
        val matchedRule = MatchedRule(rule, tokens)

        // Assert
        assertEquals(rule, matchedRule.rule)
        assertTrue(matchedRule.matchedTokens.isEmpty())
    }

    @Test
    fun matchedRule_should_preserve_token_order() {
        // Arrange
        val rule = PrintlnRule(PrintBuilder())
        val tokens =
            listOf(
                Token(IdentifierType, "println", 1, 1),
                Token(PunctuationType, "(", 1, 8),
                Token(LiteralString, "\"hello\"", 1, 9),
                Token(PunctuationType, ")", 1, 16),
                Token(PunctuationType, ";", 1, 17),
            )

        // Act
        val matchedRule = MatchedRule(rule, tokens)

        // Assert
        assertEquals("println", matchedRule.matchedTokens[0].value)
        assertEquals("(", matchedRule.matchedTokens[1].value)
        assertEquals("\"hello\"", matchedRule.matchedTokens[2].value)
        assertEquals(")", matchedRule.matchedTokens[3].value)
        assertEquals(";", matchedRule.matchedTokens[4].value)
    }

    @Test
    fun matchedRule_should_be_immutable() {
        // Arrange
        val rule = PrintlnRule(PrintBuilder())
        val originalTokens =
            listOf(
                Token(IdentifierType, "println", 1, 1),
                Token(PunctuationType, "(", 1, 8),
            )

        // Act
        val matchedRule = MatchedRule(rule, originalTokens)
        val retrievedTokens = matchedRule.matchedTokens

        // Assert
        assertEquals(originalTokens, retrievedTokens)
        // Verify it's the same reference (immutable)
        assertTrue(retrievedTokens === originalTokens)
    }
}
