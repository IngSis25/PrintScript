package org.example.astnode.expressionNodes

import org.example.Lexer.Location
import org.example.astnode.astNodeVisitor.ASTNodeVisitor
import org.example.astnode.astNodeVisitor.VisitorResult

class ReadInputNode(
    override val type: String,
    override val location: Location,
    val message: ExpressionNode,
) : ExpressionNode {
    override fun accept(visitor: ASTNodeVisitor): VisitorResult = visitor.visit(this)

    override fun getType(symbolTable: MutableMap<String, Pair<String, LiteralValue>>): String = "string" // readInput siempre devuelve un string (el intérprete lo convierte automáticamente si es necesario)

    override fun toString(): String = "readInput($message)"
}
