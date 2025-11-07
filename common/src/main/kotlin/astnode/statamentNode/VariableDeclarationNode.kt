package org.example.astnode.statamentNode

import lexer.Location
import org.example.astnode.astNodeVisitor.ASTNodeVisitor
import org.example.astnode.astNodeVisitor.VisitorResult
import org.example.astnode.expressionNodes.ExpressionNode
import org.example.astnode.expressionNodes.IdentifierNode
import org.example.astnode.statamentNode.StatementNode

class VariableDeclarationNode(
    override val type: String,
    override val location: Location,
    val identifier: IdentifierNode,
    val init: ExpressionNode,
    val kind: String,
) : StatementNode {
    override fun accept(visitor: ASTNodeVisitor): VisitorResult = visitor.visit(this)
}
