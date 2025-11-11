package org.checkvisitors

import WarningInfo
import main.kotlin.analyzer.SourcePosition
import main.kotlin.analyzer.SymbolTable
import org.example.astnode.ASTNode
import org.example.astnode.astNodeVisitor.VisitorResult
import org.example.astnode.expressionNodes.IdentifierNode
import org.example.astnode.statamentNode.AssignmentNode
import org.example.astnode.statamentNode.VariableDeclarationNode
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
            is VariableDeclarationNode -> {
                declared.add(
                    node.identifier.name to SourcePosition(node.location.getLine(), node.location.getColumn()),
                )
                // Visitar recursivamente el valor para detectar uso de otras variables
                visitRecursive(node.init)
            }
            is AssignmentNode -> {
                // Marcar el identificador asignado como usado
                used.add(node.identifier.name)
                // Visitar el valor del assignment
                visitRecursive(node.value)
            }
            is IdentifierNode -> used.add(node.name)
        }
        return VisitorResult.Empty
    }

    private fun visitRecursive(node: ASTNode?) {
        if (node == null) return
        when (node) {
            is IdentifierNode -> used.add(node.name)
            // Podríamos agregar más casos aquí si es necesario
        }
    }

    override fun checkWarnings(): VisitorResult {
        declared.forEach { (name, pos) ->
            if (name !in used) {
                warnings.add(
                    WarningInfo(
                        code = "UNUSED_VAR",
                        message = "Variable '$name' is declared but never used.",
                        position = pos,
                    ),
                )
            }
        }
        return VisitorResult.ListResult(warnings)
    }
}
