package org.example.astnode.expressionNodes

import lexer.Location
import org.example.astnode.astNodeVisitor.ASTNodeVisitor
import org.example.astnode.astNodeVisitor.VisitorResult

class BooleanExpressionNode(
    override val type: String,
    override val location: Location,
    val bool: ExpressionNode,
) : ExpressionNode {
    override fun accept(visitor: ASTNodeVisitor): VisitorResult = visitor.visit(this)

    override fun getType(symbolTable: MutableMap<String, Pair<String, LiteralValue>>): String =
        bool.getType(symbolTable)

    override fun toString(): String = bool.toString()
}
