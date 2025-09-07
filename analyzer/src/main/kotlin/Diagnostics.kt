package main.kotlin.analyzer

/**
 * Represents a diagnostic message (error, warning, info) from the analyzer
 */
data class Diagnostic(
    val code: String,
    val message: String,
    val severity: DiagnosticSeverity,
    val position: SourcePosition,
    val suggestions: List<String> = emptyList(),
)

/**
 * Severity levels for diagnostics
 */
enum class DiagnosticSeverity {
    ERROR,
    WARNING,
    INFO,
}

/**
 * Represents a position in the source code
 */
data class SourcePosition(
    val line: Int,
    val column: Int,
    val length: Int = 1,
) {
    override fun toString(): String = "line $line, column $column"
}

/**
 * Result of an analysis operation
 */
data class AnalysisResult(
    val diagnostics: List<Diagnostic>,
    val success: Boolean = diagnostics.none { it.severity == DiagnosticSeverity.ERROR },
) {
    val hasErrors: Boolean get() = diagnostics.any { it.severity == DiagnosticSeverity.ERROR }
    val hasWarnings: Boolean get() = diagnostics.any { it.severity == DiagnosticSeverity.WARNING }
    val errorCount: Int get() = diagnostics.count { it.severity == DiagnosticSeverity.ERROR }
    val warningCount: Int get() = diagnostics.count { it.severity == DiagnosticSeverity.WARNING }
}
