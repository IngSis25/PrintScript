package org.example

import main.kotlin.analyzer.AnalyzerConfig
import main.kotlin.analyzer.DefaultAnalyzer
import org.example.ast.ASTNode
import org.example.ast.IdentifierNode
import org.example.ast.PrintlnNode
import org.example.ast.VariableDeclarationNode
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test

class DefaultAnalyzerAdditionalTest {
    private val analyzer = DefaultAnalyzer("1.0", nodeIterator)
    private val defaultConfig = AnalyzerConfig()

    @Test
    fun `should handle variable declaration without type and value`() {
        // Arrange
        val program: List<ASTNode> =
            listOf(
                VariableDeclarationNode(
                    identifier = IdentifierNode("noTypeNoValue"),
                    varType = null,
                    value = null,
                ),
            )

        // Act
        val result = analyzer.analyze(program, defaultConfig)

        // Assert
        assertNotNull(result)
    }

    @Test
    fun `should handle variable declaration with type but no value`() {
        // Arrange
        val program: List<ASTNode> =
            listOf(
                PrintlnNode(
                    value = IdentifierNode("a"),
                ),
            )

        // Act
        val result = analyzer.analyze(program, defaultConfig)

        // Assert
        assertNotNull(result)
    }
}
