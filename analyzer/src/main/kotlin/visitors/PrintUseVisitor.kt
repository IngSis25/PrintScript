package org.checkvisitors

import WarningInfo
import main.kotlin.analyzer.SourcePosition
import org.example.astnode.ASTNode
import org.example.astnode.astNodeVisitor.VisitorResult
import org.example.astnode.expressionNodes.IdentifierNode
import org.example.astnode.expressionNodes.LiteralNode
import org.example.astnode.statamentNode.PrintStatementNode
import visitors.AnalyzerVisitor

class PrintUseVisitor(
    private val checkComplexExpressions: Boolean,
) : AnalyzerVisitor {
    private val warnings = mutableListOf<WarningInfo>()

    override fun visit(node: ASTNode): VisitorResult {
        if (checkComplexExpressions && node is PrintStatementNode) {
            // Verificar si la expresión es compleja (no es variable ni literal)
            val value = node.value
            if (value !is IdentifierNode && value !is LiteralNode) {
                warnings.add(
                    WarningInfo(
                        code = "PRINT_USE",
                        message =
                            "Location:(line: ${node.location.getLine()}, column: ${node.location.getColumn()}), " +
                                "Print statement should be called as a variable or Literal.",
                        position = SourcePosition(node.location.getLine(), node.location.getColumn()),
                    ),
                )
            }
        }
        return VisitorResult.Empty
    }

    override fun checkWarnings(): VisitorResult = VisitorResult.ListResult(warnings)
}
