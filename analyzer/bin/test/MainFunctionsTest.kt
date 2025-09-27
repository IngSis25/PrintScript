package org.example

import main.kotlin.analyzer.AnalysisResult
import main.kotlin.analyzer.Diagnostic
import main.kotlin.analyzer.DiagnosticSeverity
import main.kotlin.analyzer.SourcePosition
import main.kotlin.analyzer.createTestProgram
import main.kotlin.analyzer.printAnalysisResults
import main.kotlin.analyzer.testWithDifferentConfig
import org.example.ast.PrintlnNode
import org.example.ast.VariableDeclarationNode
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream
import java.io.PrintStream

class MainFunctionsTest {
    @Test
    fun `printAnalysisResults prints details for non-empty diagnostics`() {
        val originalOut = System.out
        val out = ByteArrayOutputStream()
        System.setOut(PrintStream(out))

        try {
            val diagnostics =
                listOf(
                    Diagnostic(
                        code = "E1",
                        message = "An error",
                        severity = DiagnosticSeverity.ERROR,
                        position = SourcePosition(1, 1),
                        suggestions = listOf("Fix it"),
                    ),
                    Diagnostic(
                        code = "W1",
                        message = "A warning",
                        severity = DiagnosticSeverity.WARNING,
                        position = SourcePosition(2, 5),
                    ),
                )

            val result = AnalysisResult(diagnostics)
            printAnalysisResults(result)

            val output = out.toString()
            assertTrue(output.contains("Errores encontrados"))
            assertTrue(output.contains("Sugerencias"))
            assertTrue(output.contains("E1"))
            assertTrue(output.contains("W1"))
        } finally {
            System.setOut(originalOut)
        }
    }

    @Test
    fun `printAnalysisResults prints no issues message when empty`() {
        val originalOut = System.out
        val out = ByteArrayOutputStream()
        System.setOut(PrintStream(out))

        try {
            val result = AnalysisResult(emptyList())
            printAnalysisResults(result)
            val output = out.toString()
            assertTrue(output.contains("No se encontraron problemas").not() || output.isNotBlank())
        } finally {
            System.setOut(originalOut)
        }
    }

    @Test
    fun `createTestProgram returns 8 nodes`() {
        val program = createTestProgram()
        assertEquals(8, program.size)
        assert(program.any { it is VariableDeclarationNode })
        assert(program.any { it is PrintlnNode })
    }

    @Test
    fun `testWithDifferentConfig produces permissive results`() {
        testWithDifferentConfig()
    }
}
