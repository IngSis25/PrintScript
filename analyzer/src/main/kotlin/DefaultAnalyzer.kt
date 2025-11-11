package main.kotlin.analyzer

import WarningInfo
import org.example.astnode.ASTNode
import org.example.astnode.astNodeVisitor.VisitorResult
import org.example.iterator.PrintScriptIterator

class DefaultAnalyzer(
    string: String,
    nodeIterator: PrintScriptIterator<ASTNode>,
) {
    fun analyze(
        ast: List<ASTNode>,
        config: AnalyzerConfig,
        version: String = "1.0",
    ): AnalysisResult {
        @Suppress("UNCHECKED_CAST")
        val nodes = ast as? List<ASTNode> ?: ast.map { it as ASTNode }

        val symbolTable = SymbolTableBuilder.build(nodes.toMutableList())

        val visitors =
            AnalyzerVisitorsFactory()
                .createAnalyzerVisitorsFromJson(version, config.jsonConfig)
        visitors.forEach { visitor ->
            try {
                visitor.setSymbolTable(symbolTable)
            } catch (_: Throwable) {
            }
        }

        nodes.forEach { node ->
            visitors.forEach { visitor ->
                visitor.visit(node)
            }
        }

        val diagnostics = mutableListOf<Diagnostic>()
        visitors.forEach { visitor ->
            val result = visitor.checkWarnings()
            if (result is VisitorResult.ListResult) {
                // asumimos que ListResult.value es List<WarningInfo>
                @Suppress("UNCHECKED_CAST")
                val list = result.value as? List<WarningInfo>
                list?.forEach { w ->
                    diagnostics.add(
                        Diagnostic(
                            code = w.code,
                            message = w.message,
                            severity = DiagnosticSeverity.WARNING,
                            position = w.position,
                        ),
                    )
                }
            }
        }
        val limited = if (config.maxErrors > 0) diagnostics.take(config.maxErrors) else diagnostics
        return AnalysisResult(limited)
    }
}
