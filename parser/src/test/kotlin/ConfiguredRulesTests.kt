package test.parserTest

import builders.*
import parser.rules.AssignmentRule
import rules.ExpressionRule
import rules.PrintlnRule
import rules.VariableDeclarationRule
import kotlin.test.*

class ConfiguredRulesTests {
    @Test
    fun v1_should_contain_expected_rules() {
        val rules = ConfiguredRules.V1

        assertEquals(4, rules.size)

        // Check that all expected rule types are present
        assertTrue(rules.any { it is PrintlnRule })
        assertTrue(rules.any { it is VariableDeclarationRule })
        assertTrue(rules.any { it is AssignmentRule })
        assertTrue(rules.any { it is ExpressionRule })
    }

    @Test
    fun v1_rules_should_have_correct_builders() {
        val rules = ConfiguredRules.V1

        // Check that rules have the expected builders
        val printlnRule = rules.find { it is PrintlnRule } as PrintlnRule
        assertTrue(printlnRule.builder is PrintBuilder)

        val varDeclRule = rules.find { it is VariableDeclarationRule } as VariableDeclarationRule
        assertTrue(varDeclRule.builder is VariableDeclarationBuilder)

        val assignmentRule = rules.find { it is AssignmentRule } as AssignmentRule
        assertTrue(assignmentRule.builder is AssignmentBuilder)

        val expressionRule = rules.find { it is ExpressionRule } as ExpressionRule
        assertTrue(expressionRule.builder is ExpressionBuilder)
    }

    @Test
    fun v1_should_be_immutable() {
        val rules1 = ConfiguredRules.V1
        val rules2 = ConfiguredRules.V1

        // Should be the same instance (object)
        assertSame(rules1, rules2)
    }
}
