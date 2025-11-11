package org.example.formatter

import org.example.astnode.ASTNode
import org.example.astnode.expressionNodes.BinaryExpressionNode
import org.example.astnode.expressionNodes.IdentifierNode
import org.example.astnode.expressionNodes.LiteralNode
import org.example.astnode.expressionNodes.ReadEnvNode
import org.example.astnode.expressionNodes.ReadInputNode
import org.example.astnode.statamentNode.AssignmentNode
import org.example.astnode.statamentNode.CompleteIfNode
import org.example.astnode.statamentNode.IfNode
import org.example.astnode.statamentNode.PrintStatementNode
import org.example.astnode.statamentNode.VariableDeclarationNode
import org.example.formatter.config.FormatterConfig

// org.example.formatter.FormatterVisitor SIMPLE
// Sin métodos visit separados, más directo y fácil de leer

class FormatterVisitor(
    private val config: FormatterConfig,
    private val outputCode: StringBuilder,
) {
    private val handler = Handler(config, outputCode, this)

    fun visit(node: ASTNode) {
        when (node) {
            is BinaryExpressionNode -> handler.handleBinaryExpression(node)
            is LiteralNode -> handler.handleLiteral(node)
            is IdentifierNode -> handler.handleIdentifier(node)
            is PrintStatementNode -> handler.handlePrintStatement(node)
            is VariableDeclarationNode -> handler.handleDeclaration(node)
            is AssignmentNode -> handler.handleAssignation(node)
            is ReadEnvNode -> handler.handleReadEnv(node)
            is ReadInputNode -> handler.handleReadInput(node)
            is CompleteIfNode -> handler.handleCompleteIfNode(node)
            is IfNode -> handler.handleIfNode(node)
            else -> {} // Para otros tipos de nodos no manejados
        }
    }
}
