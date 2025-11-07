package org.example.astnode.expressionNodes

import org.example.Lexer.Location
import org.example.astnode.ASTNode
import org.example.astnode.astNodeVisitor.ASTNodeVisitor
import org.example.astnode.astNodeVisitor.VisitorResult

interface ExpressionNode : ASTNode {
    override val type: String
    override val location: Location

    override fun accept(visitor: ASTNodeVisitor): VisitorResult

    fun getType(symbolTable: MutableMap<String, Pair<String, LiteralValue>>): String

    override fun toString(): String
}
