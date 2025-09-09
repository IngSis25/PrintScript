package org.example

import main.kotlin.analyzer.*
import org.example.ast.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class IdentifierFormatRuleTest {
    @Test
    fun `should pass valid camelCase identifiers when camelCase is required`() {
        val config = IdentifierFormatConfig(enabled = true, format = IdentifierFormat.CAMEL_CASE)
        val rule = IdentifierFormatRule(config)
        val symbolTable = SymbolTable()
        val position = SourcePosition(1, 1)

        val node = IdentifierNode("myVariable")
        val diagnostics = rule.analyze(node, symbolTable, position)

        assertTrue(diagnostics.isEmpty())
    }

    @Test
    fun `should fail invalid camelCase identifiers`() {
        val config = IdentifierFormatConfig(enabled = true, format = IdentifierFormat.CAMEL_CASE)
        val rule = IdentifierFormatRule(config)
        val symbolTable = SymbolTable()
        val position = SourcePosition(1, 1)

        val node = IdentifierNode("my_variable")
        val diagnostics = rule.analyze(node, symbolTable, position)

        assertFalse(diagnostics.isEmpty())
        assertEquals("INVALID_IDENTIFIER_FORMAT", diagnostics[0].code)
        assertTrue(diagnostics[0].message.contains("my_variable"))
        assertTrue(diagnostics[0].suggestions.isNotEmpty())
    }

    @Test
    fun `should pass valid snake_case identifiers when snake_case is required`() {
        val config = IdentifierFormatConfig(enabled = true, format = IdentifierFormat.SNAKE_CASE)
        val rule = IdentifierFormatRule(config)
        val symbolTable = SymbolTable()
        val position = SourcePosition(1, 1)

        val node = IdentifierNode("my_variable")
        val diagnostics = rule.analyze(node, symbolTable, position)

        assertTrue(diagnostics.isEmpty())
    }

    @Test
    fun `should fail invalid snake_case identifiers`() {
        val config = IdentifierFormatConfig(enabled = true, format = IdentifierFormat.SNAKE_CASE)
        val rule = IdentifierFormatRule(config)
        val symbolTable = SymbolTable()
        val position = SourcePosition(1, 1)

        val node = IdentifierNode("myVariable")
        val diagnostics = rule.analyze(node, symbolTable, position)

        assertFalse(diagnostics.isEmpty())
        assertEquals("INVALID_IDENTIFIER_FORMAT", diagnostics[0].code)
    }

    @Test
    fun `should pass valid PascalCase identifiers when PascalCase is required`() {
        val config = IdentifierFormatConfig(enabled = true, format = IdentifierFormat.PASCAL_CASE)
        val rule = IdentifierFormatRule(config)
        val symbolTable = SymbolTable()
        val position = SourcePosition(1, 1)

        val node = IdentifierNode("MyVariable")
        val diagnostics = rule.analyze(node, symbolTable, position)

        assertTrue(diagnostics.isEmpty())
    }

    @Test
    fun `should fail invalid PascalCase identifiers`() {
        val config = IdentifierFormatConfig(enabled = true, format = IdentifierFormat.PASCAL_CASE)
        val rule = IdentifierFormatRule(config)
        val symbolTable = SymbolTable()
        val position = SourcePosition(1, 1)

        val node = IdentifierNode("myVariable")
        val diagnostics = rule.analyze(node, symbolTable, position)

        assertFalse(diagnostics.isEmpty())
        assertEquals("INVALID_IDENTIFIER_FORMAT", diagnostics[0].code)
    }

    @Test
    fun `should not validate when rule is disabled`() {
        val config = IdentifierFormatConfig(enabled = false, format = IdentifierFormat.CAMEL_CASE)
        val rule = IdentifierFormatRule(config)
        val symbolTable = SymbolTable()
        val position = SourcePosition(1, 1)

        val node = IdentifierNode("bad_name")
        val diagnostics = rule.analyze(node, symbolTable, position)

        assertTrue(diagnostics.isEmpty())
    }

    @Test
    fun `should validate variable declaration identifiers`() {
        val config = IdentifierFormatConfig(enabled = true, format = IdentifierFormat.CAMEL_CASE)
        val rule = IdentifierFormatRule(config)
        val symbolTable = SymbolTable()
        val position = SourcePosition(1, 1)

        val node =
            VariableDeclarationNode(
                identifier = IdentifierNode("bad_name"),
                varType = "NUMBER",
                value = null,
            )
        val diagnostics = rule.analyze(node, symbolTable, position)

        assertFalse(diagnostics.isEmpty())
        assertEquals("INVALID_IDENTIFIER_FORMAT", diagnostics[0].code)
    }

    @Test
    fun `should validate assignment identifiers`() {
        val config = IdentifierFormatConfig(enabled = true, format = IdentifierFormat.CAMEL_CASE)
        val rule = IdentifierFormatRule(config)
        val symbolTable = SymbolTable()
        val position = SourcePosition(1, 1)

        val node =
            AssignmentNode(
                identifier = IdentifierNode("bad_name"),
                value = LiteralNode("42", LiteralNumber),
            )
        val diagnostics = rule.analyze(node, symbolTable, position)

        assertFalse(diagnostics.isEmpty())
        assertEquals("INVALID_IDENTIFIER_FORMAT", diagnostics[0].code)
    }

    @Test
    fun `should provide correct suggestions for camelCase conversion`() {
        val config = IdentifierFormatConfig(enabled = true, format = IdentifierFormat.CAMEL_CASE)
        val rule = IdentifierFormatRule(config)
        val symbolTable = SymbolTable()
        val position = SourcePosition(1, 1)

        val node = IdentifierNode("my_variable_name")
        val diagnostics = rule.analyze(node, symbolTable, position)

        assertFalse(diagnostics.isEmpty())
        assertTrue(diagnostics[0].suggestions[0].contains("myVariableName"))
    }

    @Test
    fun `should provide correct suggestions for snake_case conversion`() {
        val config = IdentifierFormatConfig(enabled = true, format = IdentifierFormat.SNAKE_CASE)
        val rule = IdentifierFormatRule(config)
        val symbolTable = SymbolTable()
        val position = SourcePosition(1, 1)

        val node = IdentifierNode("myVariableName")
        val diagnostics = rule.analyze(node, symbolTable, position)

        assertFalse(diagnostics.isEmpty())
        assertTrue(diagnostics[0].suggestions[0].contains("my_variable_name"))
    }

    @Test
    fun `should provide correct suggestions for PascalCase conversion`() {
        val config = IdentifierFormatConfig(enabled = true, format = IdentifierFormat.PASCAL_CASE)
        val rule = IdentifierFormatRule(config)
        val symbolTable = SymbolTable()
        val position = SourcePosition(1, 1)

        val node = IdentifierNode("myVariableName")
        val diagnostics = rule.analyze(node, symbolTable, position)

        assertFalse(diagnostics.isEmpty())
        assertTrue(diagnostics[0].suggestions[0].contains("MyVariableName"))
    }
}
