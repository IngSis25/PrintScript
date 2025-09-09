package org.example

import main.kotlin.analyzer.*
import org.example.ast.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class PrintlnRestrictionRuleTest {
    @Test
    fun `should allow println with identifier`() {
        val config = PrintlnRestrictionConfig(enabled = true, allowOnlyIdentifiersAndLiterals = true)
        val rule = PrintlnRestrictionRule(config)
        val symbolTable = SymbolTable()
        val position = SourcePosition(1, 1)

        val node = PrintlnNode(value = IdentifierNode("myVariable"))
        val diagnostics = rule.analyze(node, symbolTable, position)

        assertTrue(diagnostics.isEmpty())
    }

    @Test
    fun `should allow println with literal`() {
        val config = PrintlnRestrictionConfig(enabled = true, allowOnlyIdentifiersAndLiterals = true)
        val rule = PrintlnRestrictionRule(config)
        val symbolTable = SymbolTable()
        val position = SourcePosition(1, 1)

        val node = PrintlnNode(value = LiteralNode("Hello", LiteralString))
        val diagnostics = rule.analyze(node, symbolTable, position)

        assertTrue(diagnostics.isEmpty())
    }

    @Test
    fun `should reject println with complex expression`() {
        val config = PrintlnRestrictionConfig(enabled = true, allowOnlyIdentifiersAndLiterals = true)
        val rule = PrintlnRestrictionRule(config)
        val symbolTable = SymbolTable()
        val position = SourcePosition(1, 1)

        val node =
            PrintlnNode(
                value =
                    BinaryOpNode(
                        left = IdentifierNode("x"),
                        operator = "+",
                        right = LiteralNode("1", LiteralNumber),
                    ),
            )
        val diagnostics = rule.analyze(node, symbolTable, position)

        assertFalse(diagnostics.isEmpty())
        assertEquals("PRINTLN_COMPLEX_EXPRESSION", diagnostics[0].code)
        assertTrue(diagnostics[0].message.contains("complex expressions"))
        assertTrue(diagnostics[0].suggestions.isNotEmpty())
    }

    @Test
    fun `should not validate when rule is disabled`() {
        val config = PrintlnRestrictionConfig(enabled = false, allowOnlyIdentifiersAndLiterals = true)
        val rule = PrintlnRestrictionRule(config)
        val symbolTable = SymbolTable()
        val position = SourcePosition(1, 1)

        val node =
            PrintlnNode(
                value =
                    BinaryOpNode(
                        left = IdentifierNode("x"),
                        operator = "+",
                        right = LiteralNode("1", LiteralNumber),
                    ),
            )
        val diagnostics = rule.analyze(node, symbolTable, position)

        assertTrue(diagnostics.isEmpty())
    }

    @Test
    fun `should not validate when allowOnlyIdentifiersAndLiterals is false`() {
        val config = PrintlnRestrictionConfig(enabled = true, allowOnlyIdentifiersAndLiterals = false)
        val rule = PrintlnRestrictionRule(config)
        val symbolTable = SymbolTable()
        val position = SourcePosition(1, 1)

        val node =
            PrintlnNode(
                value =
                    BinaryOpNode(
                        left = IdentifierNode("x"),
                        operator = "+",
                        right = LiteralNode("1", LiteralNumber),
                    ),
            )
        val diagnostics = rule.analyze(node, symbolTable, position)

        assertTrue(diagnostics.isEmpty())
    }

    @Test
    fun `should ignore non-println nodes`() {
        val config = PrintlnRestrictionConfig(enabled = true, allowOnlyIdentifiersAndLiterals = true)
        val rule = PrintlnRestrictionRule(config)
        val symbolTable = SymbolTable()
        val position = SourcePosition(1, 1)

        val node =
            VariableDeclarationNode(
                identifier = IdentifierNode("x"),
                varType = "NUMBER",
                value = null,
            )
        val diagnostics = rule.analyze(node, symbolTable, position)

        assertTrue(diagnostics.isEmpty())
    }
}
