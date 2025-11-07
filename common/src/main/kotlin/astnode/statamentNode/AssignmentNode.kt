package org.example.astnode.statamentNode

import org.example.Lexer.Location
import org.example.astnode.astNodeVisitor.ASTNodeVisitor
import org.example.astnode.astNodeVisitor.VisitorResult
import org.example.astnode.expressionNodes.ExpressionNode
import org.example.astnode.expressionNodes.IdentifierNode
import org.example.astnode.statamentNode.StatementNode

class AssignmentNode(
    override val type: String,
    override val location: Location,
    val value: ExpressionNode,
    val identifier: IdentifierNode,
) : StatementNode {
    override fun accept(visitor: ASTNodeVisitor): VisitorResult = visitor.visit(this)
}
