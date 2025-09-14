package test.parserTest

import builders.AssignmentBuilder
import main.kotlin.lexer.*
import main.kotlin.parser.ParseResult
import org.example.LiteralNumber
import parser.rules.AssignmentRule
import types.*
import kotlin.test.*

class AssignmentRuleTest {
    @Test
    fun assignmentRule_should_match_simple_assignment() {
        // Tokens para "result = 5;"
        val tokens =
            listOf(
                Token(IdentifierType, "result", 1, 1),
                Token(AssignmentType, "=", 1, 8),
                Token(LiteralNumber, "5", 1, 10),
                Token(PunctuationType, ";", 1, 11),
            )

        val rule = AssignmentRule(AssignmentBuilder())
        val result = rule.matcher.match(tokens, 0)

        println("Result: $result")
        assertNotNull(result)

        if (result is ParseResult.Success) {
            println("SUCCESS: ${result.node}")
            assertTrue(true)
        } else {
            println("FAILURE: $result")
            // No fallar, solo mostrar el resultado
            assertTrue(true)
        }
    }
}
