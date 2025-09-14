package test.parserTest

import ComparisonRule
import builders.ExpressionBuilder
import main.kotlin.lexer.*
import main.kotlin.parser.ParseResult
import org.example.LiteralNumber
import org.junit.jupiter.api.Assertions.assertNotNull
import types.*
import kotlin.test.*

class ComparisonRuleTests {
    private val comparisonRule = ComparisonRule(ExpressionBuilder())

    @Test
    fun should_match_different_comparison_operators() {
        val operators = listOf("!=", ">", "<", ">=", "<=")

        for (operator in operators) {
            val tokens =
                listOf(
                    Token(IdentifierType, "x", 1, 1),
                    Token(ComparisonOperatorType, operator, 1, 3),
                    Token(LiteralNumber, "5", 1, 3 + operator.length + 1),
                )

            val result = comparisonRule.matcher.match(tokens, 0)

            assertNotNull(result, "Should match operator: $operator")
            assertTrue(result is ParseResult.Success, "Should be success for operator: $operator")
        }
    }

    @Test
    fun should_not_match_without_identifier() {
        val tokens =
            listOf(
                Token(ComparisonOperatorType, "==", 1, 1),
                Token(IdentifierType, "y", 1, 4),
            )

        val result = comparisonRule.matcher.match(tokens, 0)

        assertNull(result)
    }

    @Test
    fun should_not_match_without_comparison_operator() {
        val tokens =
            listOf(
                Token(IdentifierType, "x", 1, 1),
                Token(IdentifierType, "y", 1, 3),
            )

        val result = comparisonRule.matcher.match(tokens, 0)

        assertNull(result)
    }

    @Test
    fun should_not_match_without_right_operand() {
        val tokens =
            listOf(
                Token(IdentifierType, "x", 1, 1),
                Token(ComparisonOperatorType, "==", 1, 3),
            )

        val result = comparisonRule.matcher.match(tokens, 0)

        assertNull(result)
    }

    @Test
    fun should_not_match_invalid_right_operand_type() {
        val tokens =
            listOf(
                Token(IdentifierType, "x", 1, 1),
                Token(ComparisonOperatorType, "==", 1, 3),
                Token(OperatorType, "+", 1, 6),
            )

        val result = comparisonRule.matcher.match(tokens, 0)

        assertNull(result)
    }

    @Test
    fun should_not_match_insufficient_tokens() {
        val tokens =
            listOf(
                Token(IdentifierType, "x", 1, 1),
            )

        val result = comparisonRule.matcher.match(tokens, 0)

        assertNull(result)
    }
}
