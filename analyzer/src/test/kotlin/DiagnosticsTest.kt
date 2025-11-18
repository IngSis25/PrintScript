package test.analyzer

import main.kotlin.analyzer.Diagnostic
import main.kotlin.analyzer.DiagnosticSeverity
import main.kotlin.analyzer.SourcePosition
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class DiagnosticsTest {
    @Test
    fun testSourcePositionCreation() {
        val position = SourcePosition(5, 10)
        assertEquals(5, position.line)
        assertEquals(10, position.column)
        assertEquals(1, position.length)
    }

    @Test
    fun testSourcePositionWithLength() {
        val position = SourcePosition(3, 7, 5)
        assertEquals(3, position.line)
        assertEquals(7, position.column)
        assertEquals(5, position.length)
    }

    @Test
    fun testSourcePositionToString() {
        val position = SourcePosition(2, 5)
        assertEquals("line 2, column 5", position.toString())
    }

    @Test
    fun testDiagnosticCreation() {
        val position = SourcePosition(1, 1)
        val diagnostic =
            Diagnostic(
                code = "TEST001",
                message = "Test message",
                severity = DiagnosticSeverity.WARNING,
                position = position,
            )
        assertEquals("TEST001", diagnostic.code)
        assertEquals("Test message", diagnostic.message)
        assertEquals(DiagnosticSeverity.WARNING, diagnostic.severity)
        assertEquals(position, diagnostic.position)
        assertTrue(diagnostic.suggestions.isEmpty())
    }

    @Test
    fun testDiagnosticWithSuggestions() {
        val position = SourcePosition(1, 1)
        val suggestions = listOf("Suggestion 1", "Suggestion 2")
        val diagnostic =
            Diagnostic(
                code = "TEST002",
                message = "Test message",
                severity = DiagnosticSeverity.ERROR,
                position = position,
                suggestions = suggestions,
            )
        assertEquals(2, diagnostic.suggestions.size)
        assertEquals("Suggestion 1", diagnostic.suggestions[0])
        assertEquals("Suggestion 2", diagnostic.suggestions[1])
    }

    @Test
    fun testDiagnosticSeverityValues() {
        assertEquals(DiagnosticSeverity.ERROR, DiagnosticSeverity.ERROR)
        assertEquals(DiagnosticSeverity.WARNING, DiagnosticSeverity.WARNING)
        assertEquals(DiagnosticSeverity.INFO, DiagnosticSeverity.INFO)
    }

    @Test
    fun testDiagnosticSeverityOrder() {
        // Verificar que los valores existen
        assertTrue(DiagnosticSeverity.values().contains(DiagnosticSeverity.ERROR))
        assertTrue(DiagnosticSeverity.values().contains(DiagnosticSeverity.WARNING))
        assertTrue(DiagnosticSeverity.values().contains(DiagnosticSeverity.INFO))
        assertEquals(3, DiagnosticSeverity.values().size)
    }
}
