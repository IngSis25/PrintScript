package org.example.formatter

import org.example.astnode.ASTNode
import org.example.astnode.astNodeVisitor.ASTNodeVisitor
import org.example.astnode.astNodeVisitor.VisitorResult
import org.example.astnode.expressionNodes.BinaryExpressionNode
import org.example.astnode.expressionNodes.BooleanExpressionNode
import org.example.astnode.expressionNodes.ExpressionNode
import org.example.astnode.expressionNodes.IdentifierNode
import org.example.astnode.expressionNodes.LiteralNode
import org.example.astnode.expressionNodes.LiteralValue
import org.example.astnode.statamentNode.AssignmentNode
import org.example.astnode.statamentNode.CompleteIfNode
import org.example.astnode.statamentNode.ElseNode
import org.example.astnode.statamentNode.IfNode
import org.example.astnode.statamentNode.PrintStatementNode
import org.example.astnode.statamentNode.VariableDeclarationNode

class FormatterVisitor : ASTNodeVisitor {
    override fun visit(node: ASTNode): VisitorResult =
        when (node) {
            is AssignmentNode -> visitAssignmentNode(node)
            is PrintStatementNode -> visitPrintStatementNode(node)
            is VariableDeclarationNode -> visitVariableDeclarationNode(node)
            is LiteralNode -> visitLiteralNode(node)
            is BinaryExpressionNode -> visitBinaryExpressionNode(node)
            is IdentifierNode -> visitIdentifierNode(node)
            is IfNode -> visitIfNode(node)
            is ElseNode -> visitElseNode(node)
            is CompleteIfNode -> visitCompleteIfNode(node)
            else -> VisitorResult.Empty
        }

    private fun visitCompleteIfNode(node: CompleteIfNode): VisitorResult {
        val ifNode = node.ifNode
        val elseNode = node.elseNode
        val result =
            visitIfNode(ifNode).toString() +
                elseNode?.let { visitElseNode(it) }.toString()
        return VisitorResult.StringResult(result)
    }

    private fun visitIfNode(node: IfNode): VisitorResult {
        val result =
            "if (${getExpression(node.boolean)})" +
                " {" + getStatements(node.ifStatements) + "}"
        return VisitorResult.StringResult(result)
    }

    private fun visitElseNode(node: ElseNode): VisitorResult {
        val result = " else {" + getStatements(node.elseStatements) + "}"
        return VisitorResult.StringResult(result)
    }

    private fun visitAssignmentNode(node: AssignmentNode): VisitorResult {
        val result = "${node.identifier.name} = ${getExpression(node.value)};"
        return VisitorResult.StringResult(result)
    }

    private fun visitPrintStatementNode(node: PrintStatementNode): VisitorResult {
        val result = "println(${getExpression(node.value)});"
        return VisitorResult.StringResult(result)
    }

    private fun visitVariableDeclarationNode(node: VariableDeclarationNode): VisitorResult {
        val result: String =
            node.kind + " " + node.identifier.name +
                ": " + node.identifier.dataType +
                " = " + getExpression(node.init) + ";"
        return VisitorResult.StringResult(result)
    }

    private fun visitLiteralNode(node: LiteralNode): VisitorResult =
        VisitorResult.StringResult(node.value.toString() + ";")

    private fun visitBinaryExpressionNode(node: BinaryExpressionNode): VisitorResult {
        val result: String =
            "${getExpression(node.left)} " +
                "${node.operator} ${getExpression(node.right)}" + ";"
        return VisitorResult.StringResult(result)
    }

    private fun visitIdentifierNode(node: IdentifierNode): VisitorResult = VisitorResult.StringResult(node.name + ";")

    private fun getExpression(init: ExpressionNode): String =
        when (init) {
            is LiteralNode -> {
                if (init.value is LiteralValue.StringValue) {
                    "\"${init.value}\""
                } else {
                    init.value.toString()
                }
            }
            is BinaryExpressionNode ->
                "${getExpression(init.left)} " +
                    "${init.operator} ${getExpression(init.right)}"
            is IdentifierNode -> init.name
            is BooleanExpressionNode -> getExpression(init.bool)
            else -> throw Exception("Unsupported expression")
        }

    private fun getStatements(nodes: List<ASTNode>): String {
        var result = ""
        nodes.forEach {
            result += visit(it).toString()
        }
        return result
    }
}
