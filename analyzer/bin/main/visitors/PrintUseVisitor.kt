package org.checkvisitors

import WarningInfo
import main.kotlin.analyzer.SourcePosition
import org.example.astnode.ASTNode
import org.example.astnode.astNodeVisitor.VisitorResult
import org.example.astnode.statamentNode.PrintStatementNode
import visitors.AnalyzerVisitor

class PrintUseVisitor(
    private val printAllowed: Boolean,
) : AnalyzerVisitor {
    private val warnings = mutableListOf<WarningInfo>()

    override fun visit(node: ASTNode): VisitorResult {
        if (!printAllowed && node is PrintStatementNode) {
            warnings.add(
                WarningInfo(
                    code = "PRINT_USE",
                    message = "Uso de 'print/println' no permitido",
                    position = SourcePosition(node.location.line, node.location.column),
                ),
            )
        }
        return VisitorResult.Empty
    }

    override fun checkWarnings(): VisitorResult = VisitorResult.ListResult(warnings)
}
