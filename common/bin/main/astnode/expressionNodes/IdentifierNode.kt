package org.example.astnode.expressionNodes

import org.example.Lexer.Location
import org.example.astnode.astNodeVisitor.ASTNodeVisitor
import org.example.astnode.astNodeVisitor.VisitorResult

class IdentifierNode(
    override val type: String,
    override val location: Location,
    val name: String,
    var dataType: String,
    val kind: String, // let o const
) : ExpressionNode {
    override fun accept(visitor: ASTNodeVisitor): VisitorResult = visitor.visit(this)

    override fun getType(symbolTable: MutableMap<String, Pair<String, LiteralValue>>): String = symbolTable[name]?.second?.getType() ?: dataType

    override fun toString(): String = name
}
