package org.example

import main.kotlin.analyzer.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class DiagnosticTest {
    @Test
    fun `should create diagnostic with all properties`() {
        val position = SourcePosition(10, 5, 3)
        val suggestions = listOf("Fix this", "Try that")

        val diagnostic =
            Diagnostic(
                code = "TEST_ERROR",
                message = "Test error message",
                severity = DiagnosticSeverity.ERROR,
                position = position,
                suggestions = suggestions,
            )

        assertEquals("TEST_ERROR", diagnostic.code)
        assertEquals("Test error message", diagnostic.message)
        assertEquals(DiagnosticSeverity.ERROR, diagnostic.severity)
        assertEquals(position, diagnostic.position)
        assertEquals(suggestions, diagnostic.suggestions)
    }

    @Test
    fun `should create diagnostic with default suggestions`() {
        val position = SourcePosition(1, 1)

        val diagnostic =
            Diagnostic(
                code = "TEST_ERROR",
                message = "Test error message",
                severity = DiagnosticSeverity.WARNING,
                position = position,
            )

        assertTrue(diagnostic.suggestions.isEmpty())
    }

    @Test
    fun `should format source position correctly`() {
        val position = SourcePosition(10, 5)
        assertEquals("line 10, column 5", position.toString())
    }

    @Test
    fun `should create analysis result with diagnostics`() {
        val diagnostics =
            listOf(
                Diagnostic(
                    code = "ERROR_1",
                    message = "Error 1",
                    severity = DiagnosticSeverity.ERROR,
                    position = SourcePosition(1, 1),
                ),
                Diagnostic(
                    code = "WARNING_1",
                    message = "Warning 1",
                    severity = DiagnosticSeverity.WARNING,
                    position = SourcePosition(2, 1),
                ),
            )

        val result = AnalysisResult(diagnostics)

        assertEquals(diagnostics, result.diagnostics)
        assertFalse(result.success)
        assertTrue(result.hasErrors)
        assertTrue(result.hasWarnings)
        assertEquals(1, result.errorCount)
        assertEquals(1, result.warningCount)
    }

    @Test
    fun `should create successful analysis result`() {
        val diagnostics =
            listOf(
                Diagnostic(
                    code = "WARNING_1",
                    message = "Warning 1",
                    severity = DiagnosticSeverity.WARNING,
                    position = SourcePosition(1, 1),
                ),
            )

        val result = AnalysisResult(diagnostics)

        assertTrue(result.success)
        assertFalse(result.hasErrors)
        assertTrue(result.hasWarnings)
        assertEquals(0, result.errorCount)
        assertEquals(1, result.warningCount)
    }

    @Test
    fun `should create empty analysis result`() {
        val result = AnalysisResult(emptyList())

        assertTrue(result.success)
        assertFalse(result.hasErrors)
        assertFalse(result.hasWarnings)
        assertEquals(0, result.errorCount)
        assertEquals(0, result.warningCount)
    }
}
