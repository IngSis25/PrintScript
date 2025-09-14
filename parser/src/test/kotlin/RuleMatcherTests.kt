package test.parserTest

import builders.ExpressionBuilder
import builders.PrintBuilder
import main.kotlin.lexer.*
import main.kotlin.parser.ParseResult
import org.example.LiteralNumber
import org.example.LiteralString
import rules.ExpressionRule
import rules.PrintlnRule
import rules.RuleMatcher
import types.IdentifierType
import types.OperatorType
import types.PrintlnType
import types.PunctuationType
import kotlin.test.*

class RuleMatcherTests {
    @Test
    fun ruleMatcher_should_try_rules_in_order() {
        // Arrange - rules that could both match the same input
        val printRule = PrintlnRule(PrintBuilder())
        val expressionRule = ExpressionRule(ExpressionBuilder())

        // PrintlnRule should match first since it's more specific
        val ruleMatcher = RuleMatcher(listOf(printRule, expressionRule))

        val tokens =
            listOf(
                Token(PrintlnType, "println", 1, 1),
                Token(PunctuationType, "(", 1, 8),
                Token(LiteralString, "\"test\"", 1, 9),
                Token(PunctuationType, ")", 1, 15),
                Token(PunctuationType, ";", 1, 16),
            )

        // Act
        val result = ruleMatcher.matchNext(tokens, 0)

        // Assert
        assertNotNull(result)
        assertTrue(result is ParseResult.Success)

        val success = result as ParseResult.Success
        assertEquals(5, success.nextPosition)
        assertEquals(printRule, success.node.rule)
    }

    @Test
    fun ruleMatcher_should_return_failure_when_no_rules_match() {
        // Arrange
        val ruleMatcher = RuleMatcher(listOf(PrintlnRule(PrintBuilder())))

        val tokens =
            listOf(
                Token(IdentifierType, "unknown", 1, 1),
                Token(PunctuationType, "(", 1, 8),
            )

        // Act
        val result = ruleMatcher.matchNext(tokens, 0)

        // Assert
        assertNotNull(result)
        assertTrue(result is ParseResult.Failure)

        val failure = result as ParseResult.Failure
        assertTrue(failure.message.contains("No matching rule"))
        assertTrue(failure.message.contains("position 0"))
        assertEquals(0, failure.position)
    }

    @Test
    fun ruleMatcher_should_handle_empty_rules_list() {
        // Arrange
        val ruleMatcher = RuleMatcher(emptyList())

        val tokens =
            listOf(
                Token(IdentifierType, "test", 1, 1),
            )

        // Act
        val result = ruleMatcher.matchNext(tokens, 0)

        // Assert
        assertNotNull(result)
        assertTrue(result is ParseResult.Failure)
    }

    @Test
    fun ruleMatcher_should_continue_on_rule_failure() {
        // Arrange - first rule fails, second rule succeeds
        val failingRule = PrintlnRule(PrintBuilder())
        val succeedingRule = ExpressionRule(ExpressionBuilder())

        val ruleMatcher = RuleMatcher(listOf(failingRule, succeedingRule))

        val tokens =
            listOf(
                Token(LiteralNumber, "42", 1, 1),
                Token(OperatorType, "+", 1, 4),
                Token(LiteralNumber, "8", 1, 6),
            )

        // Act
        val result = ruleMatcher.matchNext(tokens, 0)

        // Assert
        assertNotNull(result)
        assertTrue(result is ParseResult.Success)

        val success = result as ParseResult.Success
        assertEquals(3, success.nextPosition)
        assertEquals(succeedingRule, success.node.rule)
    }
}
