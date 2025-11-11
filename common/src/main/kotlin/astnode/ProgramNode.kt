package org.example.astnode

import org.example.Lexer.Location
import org.example.astnode.ASTNode
import org.example.astnode.astNodeVisitor.ASTNodeVisitor
import org.example.astnode.astNodeVisitor.VisitorResult

class ProgramNode(
    override val type: String,
    override val location: Location,
    val statements: List<ASTNode>,
) : ASTNode {
    override fun accept(visitor: ASTNodeVisitor): VisitorResult = visitor.visit(this)
}
