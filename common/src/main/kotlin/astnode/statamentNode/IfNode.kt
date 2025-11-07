package org.example.astnode.statamentNode

import org.example.Lexer.Location
import org.example.astnode.ASTNode
import org.example.astnode.astNodeVisitor.ASTNodeVisitor
import org.example.astnode.astNodeVisitor.VisitorResult
import org.example.astnode.expressionNodes.BooleanExpressionNode
import org.example.astnode.statamentNode.StatementNode

class IfNode(
    override val type: String,
    override val location: Location,
    val boolean: BooleanExpressionNode,
    val ifStatements: List<ASTNode>,
) : StatementNode {
    override fun accept(visitor: ASTNodeVisitor): VisitorResult = visitor.visit(this)
}

class ElseNode(
    override val type: String,
    override val location: Location,
    val elseStatements: List<ASTNode>,
) : StatementNode {
    override fun accept(visitor: ASTNodeVisitor): VisitorResult = visitor.visit(this)
}

class CompleteIfNode(
    override val type: String,
    override val location: Location,
    val ifNode: IfNode,
    val elseNode: ElseNode?,
) : StatementNode {
    override fun accept(visitor: ASTNodeVisitor): VisitorResult = visitor.visit(this)
}
