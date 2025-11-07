package org.example.astnode.expressionNodes

import lexer.Location
import org.example.astnode.astNodeVisitor.ASTNodeVisitor
import org.example.astnode.astNodeVisitor.VisitorResult

class ReadEnvNode(
    override val type: String,
    override val location: Location,
    val variableName: String,
) : ExpressionNode {
    override fun accept(visitor: ASTNodeVisitor): VisitorResult = visitor.visit(this)

    override fun getType(symbolTable: MutableMap<String, Pair<String, LiteralValue>>): String = "Undefined"

    override fun toString(): String = "readEnv($variableName)"
}
