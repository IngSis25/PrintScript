package org.checkvisitors

import WarningInfo
import main.kotlin.analyzer.SourcePosition
import org.example.astnode.ASTNode
import org.example.astnode.astNodeVisitor.VisitorResult
import org.example.astnode.expressionNodes.ReadInputNode
import visitors.AnalyzerVisitor

class ReadInputVisitor(
    private val readInputAllowed: Boolean,
) : AnalyzerVisitor {
    private val warnings = mutableListOf<WarningInfo>()

    override fun visit(node: ASTNode): VisitorResult {
        if (!readInputAllowed && node is ReadInputNode) {
            warnings.add(
                WarningInfo(
                    code = "READ_INPUT",
                    message = "Uso de 'readInput/readln' no permitido",
                    position = SourcePosition(node.location.line, node.location.column),
                ),
            )
        }
        return VisitorResult.Empty
    }

    override fun checkWarnings(): VisitorResult = VisitorResult.ListResult(warnings)
}
