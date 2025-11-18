package main.kotlin.analyzer

import WarningInfo
import org.example.astnode.ASTNode
import org.example.astnode.astNodeVisitor.VisitorResult
import org.example.iterator.PrintScriptIterator

class DefaultAnalyzer(
    override val version: String,
    override val nodeIterator: PrintScriptIterator<ASTNode>,
) : Analyzer {
    override fun analyze(
        ast: List<ASTNode>,
        config: AnalyzerConfig,
        version: String,
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
            visitRecursive(node, visitors)
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

    private fun visitRecursive(
        node: ASTNode,
        visitors: List<visitors.AnalyzerVisitor>,
    ) {
        visitors.forEach { visitor ->
            visitor.visit(node)
        }

        // Visitar recursivamente los nodos hijos
        when (node) {
            is org.example.astnode.statamentNode.VariableDeclarationNode -> {
                visitRecursive(node.init, visitors)
            }
            is org.example.astnode.statamentNode.AssignmentNode -> {
                visitRecursive(node.identifier, visitors)
                visitRecursive(node.value, visitors)
            }
            is org.example.astnode.statamentNode.PrintStatementNode -> {
                visitRecursive(node.value, visitors)
            }
            is org.example.astnode.expressionNodes.BinaryExpressionNode -> {
                visitRecursive(node.left, visitors)
                visitRecursive(node.right, visitors)
            }
            is org.example.astnode.expressionNodes.ReadInputNode -> {
                visitRecursive(node.message, visitors)
            }
            // Otros tipos de nodos se pueden agregar aquí según sea necesario
        }
    }
}
