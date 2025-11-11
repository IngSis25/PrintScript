package org.example.astnode.statamentNode

import org.example.Lexer.Location
import org.example.astnode.astNodeVisitor.ASTNodeVisitor
import org.example.astnode.astNodeVisitor.VisitorResult
import org.example.astnode.expressionNodes.ExpressionNode
import org.example.astnode.statamentNode.StatementNode

class PrintStatementNode(
    override val type: String,
    override val location: Location,
    val value: ExpressionNode,
) : StatementNode {
    override fun accept(visitor: ASTNodeVisitor): VisitorResult = visitor.visit(this)
}
