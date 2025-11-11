package test.analyzer

import main.kotlin.analyzer.AnalysisResult
import main.kotlin.analyzer.Diagnostic
import main.kotlin.analyzer.DiagnosticSeverity
import main.kotlin.analyzer.SourcePosition
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class AnalysisResultTest {
    @Test
    fun testAnalysisResultSuccessWithNoDiagnostics() {
        val result = AnalysisResult(emptyList())
        assertTrue(result.success)
        assertFalse(result.hasErrors)
        assertFalse(result.hasWarnings)
        assertEquals(0, result.errorCount)
        assertEquals(0, result.warningCount)
    }

    @Test
    fun testAnalysisResultSuccessWithWarnings() {
        val diagnostics =
            listOf(
                Diagnostic(
                    code = "W001",
                    message = "Warning 1",
                    severity = DiagnosticSeverity.WARNING,
                    position = SourcePosition(1, 1),
                ),
                Diagnostic(
                    code = "W002",
                    message = "Warning 2",
                    severity = DiagnosticSeverity.WARNING,
                    position = SourcePosition(2, 1),
                ),
            )
        val result = AnalysisResult(diagnostics)
        assertTrue(result.success)
        assertFalse(result.hasErrors)
        assertTrue(result.hasWarnings)
        assertEquals(0, result.errorCount)
        assertEquals(2, result.warningCount)
    }

    @Test
    fun testAnalysisResultFailureWithErrors() {
        val diagnostics =
            listOf(
                Diagnostic(
                    code = "E001",
                    message = "Error 1",
                    severity = DiagnosticSeverity.ERROR,
                    position = SourcePosition(1, 1),
                ),
                Diagnostic(
                    code = "E002",
                    message = "Error 2",
                    severity = DiagnosticSeverity.ERROR,
                    position = SourcePosition(2, 1),
                ),
            )
        val result = AnalysisResult(diagnostics)
        assertFalse(result.success)
        assertTrue(result.hasErrors)
        assertFalse(result.hasWarnings)
        assertEquals(2, result.errorCount)
        assertEquals(0, result.warningCount)
    }

    @Test
    fun testAnalysisResultWithMixedDiagnostics() {
        val diagnostics =
            listOf(
                Diagnostic(
                    code = "E001",
                    message = "Error 1",
                    severity = DiagnosticSeverity.ERROR,
                    position = SourcePosition(1, 1),
                ),
                Diagnostic(
                    code = "W001",
                    message = "Warning 1",
                    severity = DiagnosticSeverity.WARNING,
                    position = SourcePosition(2, 1),
                ),
                Diagnostic(
                    code = "I001",
                    message = "Info 1",
                    severity = DiagnosticSeverity.INFO,
                    position = SourcePosition(3, 1),
                ),
            )
        val result = AnalysisResult(diagnostics)
        assertFalse(result.success)
        assertTrue(result.hasErrors)
        assertTrue(result.hasWarnings)
        assertEquals(1, result.errorCount)
        assertEquals(1, result.warningCount)
    }

    @Test
    fun testAnalysisResultWithInfoOnly() {
        val diagnostics =
            listOf(
                Diagnostic(
                    code = "I001",
                    message = "Info 1",
                    severity = DiagnosticSeverity.INFO,
                    position = SourcePosition(1, 1),
                ),
                Diagnostic(
                    code = "I002",
                    message = "Info 2",
                    severity = DiagnosticSeverity.INFO,
                    position = SourcePosition(2, 1),
                ),
            )
        val result = AnalysisResult(diagnostics)
        assertTrue(result.success)
        assertFalse(result.hasErrors)
        assertFalse(result.hasWarnings)
        assertEquals(0, result.errorCount)
        assertEquals(0, result.warningCount)
    }

    @Test
    fun testAnalysisResultSuccessProperty() {
        val errorDiagnostics =
            listOf(
                Diagnostic(
                    code = "E001",
                    message = "Error",
                    severity = DiagnosticSeverity.ERROR,
                    position = SourcePosition(1, 1),
                ),
            )
        val errorResult = AnalysisResult(errorDiagnostics)
        assertFalse(errorResult.success)

        val warningDiagnostics =
            listOf(
                Diagnostic(
                    code = "W001",
                    message = "Warning",
                    severity = DiagnosticSeverity.WARNING,
                    position = SourcePosition(1, 1),
                ),
            )
        val warningResult = AnalysisResult(warningDiagnostics)
        assertTrue(warningResult.success)
    }

    @Test
    fun testAnalysisResultDiagnosticsList() {
        val diagnostics =
            listOf(
                Diagnostic(
                    code = "W001",
                    message = "Warning",
                    severity = DiagnosticSeverity.WARNING,
                    position = SourcePosition(1, 1),
                ),
            )
        val result = AnalysisResult(diagnostics)
        assertEquals(1, result.diagnostics.size)
        assertEquals("W001", result.diagnostics[0].code)
    }
}
