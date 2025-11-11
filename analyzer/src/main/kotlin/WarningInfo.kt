import main.kotlin.analyzer.SourcePosition

data class WarningInfo(
    val message: String,
    val position: SourcePosition,
    val code: String = "visitor-warning",
)
