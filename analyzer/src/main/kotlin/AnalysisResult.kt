import main.kotlin.analyzer.Diagnostic
import main.kotlin.analyzer.DiagnosticSeverity

data class AnalysisResult(
    val diagnostics: List<Diagnostic>,
    val success: Boolean = diagnostics.none { it.severity == DiagnosticSeverity.ERROR },
)
