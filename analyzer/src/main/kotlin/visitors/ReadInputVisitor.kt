package org.checkvisitors

import WarningInfo
import main.kotlin.analyzer.SourcePosition
import org.example.astnode.ASTNode
import org.example.astnode.astNodeVisitor.VisitorResult
import org.example.astnode.expressionNodes.IdentifierNode
import org.example.astnode.expressionNodes.LiteralNode
import org.example.astnode.expressionNodes.ReadInputNode
import visitors.AnalyzerVisitor

class ReadInputVisitor(
    private val checkComplexExpressions: Boolean,
) : AnalyzerVisitor {
    private val warnings = mutableListOf<WarningInfo>()

    override fun visit(node: ASTNode): VisitorResult {
        if (checkComplexExpressions && node is ReadInputNode) {
            // Verificar si el mensaje es una expresión compleja (no es variable ni literal)
            val message = node.message
            if (message !is IdentifierNode && message !is LiteralNode) {
                warnings.add(
                    WarningInfo(
                        code = "READ_INPUT",
                        message =
                            "Location:(line: ${node.location.getLine()}, column: ${node.location.getColumn()})," +
                                " readInput message must be a variable or literal",
                        position = SourcePosition(node.location.getLine(), node.location.getColumn()),
                    ),
                )
            }
        }
        return VisitorResult.Empty
    }

    override fun checkWarnings(): VisitorResult = VisitorResult.ListResult(warnings)
}
