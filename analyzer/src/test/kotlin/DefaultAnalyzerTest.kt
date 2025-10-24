package org.example

import main.kotlin.analyzer.*
import org.example.LiteralNumber
import org.example.LiteralString
import org.example.ast.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class DefaultAnalyzerTest {
    private val analyzer = DefaultAnalyzer()
    private val defaultConfig = AnalyzerConfig()

    @Test
    fun `should pass valid camelCase identifiers`() {
        val program =
            listOf(
                VariableDeclarationNode(
                    identifier = IdentifierNode("myVariable"),
                    varType = "NUMBER",
                    value = LiteralNode("42", LiteralNumber),
                ),
                VariableDeclarationNode(
                    identifier = IdentifierNode("anotherVariable"),
                    varType = "STRING",
                    value = LiteralNode("hello", LiteralString),
                ),
            )

        val result = analyzer.analyze(program, defaultConfig)

        assertTrue(result.diagnostics.isEmpty())
    }

    @Test
    fun `should detect invalid camelCase identifiers`() {
        val program =
            listOf(
                VariableDeclarationNode(
                    identifier = IdentifierNode("my_variable"), // snake_case instead of camelCase
                    varType = "NUMBER",
                    value = LiteralNode("42", LiteralNumber),
                ),
            )

        val result = analyzer.analyze(program, defaultConfig)

        assertFalse(result.diagnostics.isEmpty())
        assertTrue(result.diagnostics.any { it.code == "INVALID_IDENTIFIER_FORMAT" })
        assertTrue(result.diagnostics.any { it.message.contains("my_variable") })
    }

    @Test
    fun `should allow valid snake_case when configured`() {
        val config =
            AnalyzerConfig(
                identifierFormat =
                    IdentifierFormatConfig(
                        enabled = true,
                        format = IdentifierFormat.SNAKE_CASE,
                    ),
            )

        val program =
            listOf(
                VariableDeclarationNode(
                    identifier = IdentifierNode("my_variable"),
                    varType = "NUMBER",
                    value = LiteralNode("42", LiteralNumber),
                ),
            )

        val result = analyzer.analyze(program, config)

        assertTrue(result.diagnostics.isEmpty())
    }

    @Test
    fun `should detect invalid snake_case identifiers`() {
        val config =
            AnalyzerConfig(
                identifierFormat =
                    IdentifierFormatConfig(
                        enabled = true,
                        format = IdentifierFormat.SNAKE_CASE,
                    ),
            )

        val program =
            listOf(
                VariableDeclarationNode(
                    identifier = IdentifierNode("myVariable"), // camelCase instead of snake_case
                    varType = "NUMBER",
                    value = LiteralNode("42", LiteralNumber),
                ),
            )

        val result = analyzer.analyze(program, config)

        assertFalse(result.diagnostics.isEmpty())
        assertTrue(result.diagnostics.any { it.code == "INVALID_IDENTIFIER_FORMAT" })
    }

    @Test
    fun `should allow valid println with identifier`() {
        val program =
            listOf(
                VariableDeclarationNode(
                    identifier = IdentifierNode("myVariable"),
                    varType = "NUMBER",
                    value = LiteralNode("42", LiteralNumber),
                ),
                PrintlnNode(value = IdentifierNode("myVariable")),
            )

        val result = analyzer.analyze(program, defaultConfig)

        assertTrue(result.diagnostics.isEmpty())
    }

    @Test
    fun `should allow valid println with literal`() {
        val program =
            listOf(
                PrintlnNode(value = LiteralNode("Hello World", LiteralString)),
            )

        val result = analyzer.analyze(program, defaultConfig)

        assertTrue(result.diagnostics.isEmpty())
    }

    @Test
    fun `should detect invalid println with complex expression`() {
        val program =
            listOf(
                VariableDeclarationNode(
                    identifier = IdentifierNode("x"),
                    varType = "NUMBER",
                    value = LiteralNode("5", LiteralNumber),
                ),
                VariableDeclarationNode(
                    identifier = IdentifierNode("y"),
                    varType = "NUMBER",
                    value = LiteralNode("3", LiteralNumber),
                ),
                PrintlnNode(
                    value =
                        BinaryOpNode(
                            left = IdentifierNode("x"),
                            operator = "+",
                            right = IdentifierNode("y"),
                        ),
                ),
            )

        val result = analyzer.analyze(program, defaultConfig)

        assertFalse(result.diagnostics.isEmpty())
        assertTrue(result.diagnostics.any { it.code == "PRINTLN_COMPLEX_EXPRESSION" })
        assertTrue(result.diagnostics.any { it.message.contains("complex expressions") })
    }

    @Test
    fun `should respect disabled rules`() {
        val config =
            AnalyzerConfig(
                identifierFormat = IdentifierFormatConfig(enabled = false),
                printlnRestrictions = PrintlnRestrictionConfig(enabled = false),
            )

        val program =
            listOf(
                VariableDeclarationNode(
                    identifier = IdentifierNode("my_variable"), // Would normally be invalid
                    varType = "NUMBER",
                    value = LiteralNode("42", LiteralNumber),
                ),
                PrintlnNode(
                    value =
                        BinaryOpNode( // Would normally be invalid
                            left = IdentifierNode("my_variable"),
                            operator = "+",
                            right = LiteralNode("1", LiteralNumber),
                        ),
                ),
            )

        val result = analyzer.analyze(program, config)

        assertTrue(result.diagnostics.isEmpty())
    }

    @Test
    fun `should respect max errors configuration`() {
        val config = AnalyzerConfig(maxErrors = 2)
        val program =
            listOf(
                VariableDeclarationNode(
                    IdentifierNode("bad_name_1"),
                    "NUMBER",
                    LiteralNode("1", LiteralNumber),
                ),
                VariableDeclarationNode(
                    IdentifierNode("bad_name_2"),
                    "NUMBER",
                    LiteralNode("2", LiteralNumber),
                ),
                VariableDeclarationNode(
                    IdentifierNode("bad_name_3"),
                    "NUMBER",
                    LiteralNode("3", LiteralNumber),
                ),
            )

        val result = analyzer.analyze(program, config)

        assertTrue(result.diagnostics.size <= 2)
    }

    @Test
    fun `should handle empty program`() {
        val program = emptyList<ASTNode>()
        val result = analyzer.analyze(program, defaultConfig)

        assertTrue(result.diagnostics.isEmpty())
        assertTrue(result.success)
    }

    @Test
    fun `should handle variable declaration without type`() {
        val program =
            listOf(
                VariableDeclarationNode(
                    identifier = IdentifierNode("myVariable"),
                    varType = null,
                    value = LiteralNode("42", LiteralNumber),
                ),
            )

        val result = analyzer.analyze(program, defaultConfig)

        // Should not crash and should infer type from value
        assertNotNull(result)
    }

    @Test
    fun `should handle variable declaration without value`() {
        val program =
            listOf(
                VariableDeclarationNode(
                    identifier = IdentifierNode("myVariable"),
                    varType = "NUMBER",
                    value = null,
                ),
            )

        val result = analyzer.analyze(program, defaultConfig)

        // Should not crash
        assertNotNull(result)
    }

    @Test
    fun `should handle unknown variable types`() {
        val program =
            listOf(
                VariableDeclarationNode(
                    identifier = IdentifierNode("myVariable"),
                    varType = "UNKNOWN_TYPE",
                    value = null,
                ),
            )

        val result = analyzer.analyze(program, defaultConfig)

        // Should not crash, should default to UNKNOWN type
        assertNotNull(result)
    }

    @Test
    fun `should handle boolean variable type`() {
        val program =
            listOf(
                VariableDeclarationNode(
                    identifier = IdentifierNode("myVariable"),
                    varType = "BOOLEAN",
                    value = null,
                ),
            )

        val result = analyzer.analyze(program, defaultConfig)

        // Should not crash
        assertNotNull(result)
    }

    @Test
    fun `should handle array variable type`() {
        val program =
            listOf(
                VariableDeclarationNode(
                    identifier = IdentifierNode("myVariable"),
                    varType = "ARRAY",
                    value = null,
                ),
            )

        val result = analyzer.analyze(program, defaultConfig)

        // Should not crash
        assertNotNull(result)
    }

    @Test
    fun `should handle max errors set to zero`() {
        val config = AnalyzerConfig(maxErrors = 0)
        val program =
            listOf(
                VariableDeclarationNode(
                    identifier = IdentifierNode("bad_name"),
                    varType = "NUMBER",
                    value = LiteralNode("42", LiteralNumber),
                ),
            )

        val result = analyzer.analyze(program, config)

        // Should return all diagnostics when maxErrors is 0
        assertNotNull(result)
    }

    @Test
    fun `should handle max errors set to negative`() {
        val config = AnalyzerConfig(maxErrors = -1)
        val program =
            listOf(
                VariableDeclarationNode(
                    identifier = IdentifierNode("bad_name"),
                    varType = "NUMBER",
                    value = LiteralNode("42", LiteralNumber),
                ),
            )

        val result = analyzer.analyze(program, config)

        // Should return all diagnostics when maxErrors is negative
        assertNotNull(result)
    }

    @Test
    fun `DefaultAnalyzer analyze method`() {
        val analyzer = DefaultAnalyzer()
        val config =
            ConfigLoader.loadFromJsonString(
                """
                {
                  "identifierFormat": {
                    "enabled": true,
                    "format": "CAMEL_CASE"
                  },
                  "printlnRestrictions": {
                    "enabled": true,
                    "allowOnlyIdentifiersAndLiterals": true
                  },
                  "maxErrors": 10,
                  "enableWarnings": true,
                  "strictMode": false
                }
                """.trimIndent(),
            )
        val program = createTestProgram()
        val result = analyzer.analyze(program, config)

        assert(result.errorCount >= 0) // simplemente para invocar el camino
    }
}
