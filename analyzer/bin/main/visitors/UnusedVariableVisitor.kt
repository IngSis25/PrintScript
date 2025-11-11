package org.checkvisitors

import WarningInfo
import main.kotlin.analyzer.SourcePosition
import main.kotlin.analyzer.SymbolTable
import org.example.ast.IdentifierNode
import org.example.ast.VariableDeclarationNode
import org.example.astnode.ASTNode
import org.example.astnode.astNodeVisitor.VisitorResult
import visitors.AnalyzerVisitor

class UnusedVariableVisitor : AnalyzerVisitor {
    private val declared = mutableSetOf<Pair<String, SourcePosition>>()
    private val used = mutableSetOf<String>()
    private val warnings = mutableListOf<WarningInfo>()
    private var symbolTable: SymbolTable? = null

    override fun setSymbolTable(table: SymbolTable) {
        symbolTable = table
    }

    override fun visit(node: ASTNode): VisitorResult {
        when (node) {
            is VariableDeclarationNode ->
                declared.add(
                    node.identifier.name to SourcePosition(node.location.line, node.location.column),
                )
            is IdentifierNode -> used.add(node.name)
        }
        return VisitorResult.Empty
    }

    override fun checkWarnings(): VisitorResult {
        declared.forEach { (name, pos) ->
            if (name !in used) {
                warnings.add(
                    WarningInfo(
                        code = "UNUSED_VAR",
                        message = "Variable '$name' declarada pero no utilizada",
                        position = pos,
                    ),
                )
            }
        }
        return VisitorResult.ListResult(warnings)
    }
}
