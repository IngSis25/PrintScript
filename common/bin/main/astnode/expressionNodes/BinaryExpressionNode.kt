package org.example.astnode.expressionNodes

import org.example.Lexer.Location
import org.example.astnode.astNodeVisitor.ASTNodeVisitor
import org.example.astnode.astNodeVisitor.VisitorResult

class BinaryExpressionNode(
    override val type: String,
    override val location: Location,
    val left: ExpressionNode,
    val right: ExpressionNode,
    val operator: String,
) : ExpressionNode {
    override fun accept(visitor: ASTNodeVisitor): VisitorResult = visitor.visit(this)

    override fun getType(symbolTable: MutableMap<String, Pair<String, LiteralValue>>): String {
        val leftType = left.getType(symbolTable)
        val rightType = right.getType(symbolTable)
        val validTypes = listOf("string", "number")

        for (type in listOf(leftType, rightType)) {
            if (type !in validTypes) {
                throw Exception("Invalid type $type in binary expression")
            }
        }

        return if (leftType == "string" || rightType == "string") {
            "string"
        } else {
            "number"
        }
    }

    override fun toString(): String = "$left $operator $right"
}
