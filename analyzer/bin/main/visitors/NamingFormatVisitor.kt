package org.checkvisitors

import WarningInfo
import main.kotlin.analyzer.SourcePosition
import org.example.astnode.ASTNode
import org.example.astnode.astNodeVisitor.VisitorResult
import org.example.astnode.statamentNode.VariableDeclarationNode
import visitors.AnalyzerVisitor

class NamingFormatVisitor(
    private val patternName: String,
    private val regexPattern: String,
) : AnalyzerVisitor {
    private val warnings = mutableListOf<WarningInfo>()

    override fun visit(node: ASTNode): VisitorResult {
        if (node is VariableDeclarationNode) {
            val name = node.identifier.name
            if (!Regex(regexPattern).matches(name)) {
                val identifierLocation = node.identifier.location
                warnings.add(
                    WarningInfo(
                        code = "NAMING_FORMAT",
                        message = "Location:(line: ${identifierLocation.getLine()}, column: ${identifierLocation
                            .getColumn()}), Declaration's identifier '$name' does not match the pattern $patternName",
                        position =
                            SourcePosition(
                                identifierLocation.getLine(),
                                identifierLocation
                                    .getColumn(),
                            ),
                    ),
                )
            }
        }
        return VisitorResult.Empty
    }

    override fun checkWarnings(): VisitorResult = VisitorResult.ListResult(warnings)
}
